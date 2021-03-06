package org.zeropage.wiki;

import org.zeropage.*;
import org.zeropage.cache.CacheLinkSource;
import org.zeropage.cache.MemCacheStorage;
import org.zeropage.cache.SqliteCacheStorage;
import org.zeropage.log.Logger;
import org.zeropage.wiki.api.RedirectedException;
import org.zeropage.wiki.api.WikipediaApi;
import org.zeropage.wiki.api.WikipediaApiException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Find a path between 2 pages with given Wikipedia API.
 * Uses the optimized searching method and caching to improve performance.
 * And also can handles redirection problems.
 */
public class WikipediaPathFinder implements PathFinder {
    public static final String FRONTLINK_CACHE_NAME = "front.db";
    public static final String BACKLINK_CACHE_NAME = "back.db";

    private LinkSource linkSource;
    private LinkSource backlinkSource;
    private WikipediaApi api;

    private Logger logger;

    /**
     * Find a path between 2 pages with given API. Memory caching is enabled.
     *
     * @param api Wikipedia API to use.
     * @throws SQLException           never thrown.
     * @throws ClassNotFoundException never thrown.
     */
    public WikipediaPathFinder(WikipediaApi api) throws SQLException, ClassNotFoundException {
        this(api, null);
    }

    /**
     * Find a path between 2 pages with given API. Memory and file chching is enabled.
     *
     * @param api      Wikipedia API to use.
     * @param cacheDir root directory for caching files.
     * @throws SQLException           failed to open file cache.
     * @throws ClassNotFoundException not able to use file cache.
     */
    public WikipediaPathFinder(WikipediaApi api, File cacheDir) throws SQLException, ClassNotFoundException {
        this.api = api;
        linkSource = new WikipediaLinkSource(api);
        backlinkSource = new WikipediaBacklinkSource(api);

        if (cacheDir != null) {
            SqliteCacheStorage sqlite = new SqliteCacheStorage(new File(cacheDir, FRONTLINK_CACHE_NAME));
            linkSource = new CacheLinkSource(linkSource, sqlite);

            sqlite = new SqliteCacheStorage(new File(cacheDir, BACKLINK_CACHE_NAME));
            backlinkSource = new CacheLinkSource(backlinkSource, sqlite);
        }

        linkSource = new CacheLinkSource(linkSource, new MemCacheStorage());
        backlinkSource = new CacheLinkSource(backlinkSource, new MemCacheStorage());

        logger = Logger.getInstance();
    }

    /**
     * Find a path between 2 pages. Parameters should be normalized and not redirected.
     *
     * @param from Starting page.
     * @param to   Destination page.
     * @return The path between 2 pages. If path is not found, null is returned.
     * @throws Exception unexpected exception happened
     */
    @Override
    public RedirectablePath getPath(String from, String to) throws Exception {
        ConcurrentMap<String, String> frontParents = new ConcurrentHashMap<>();
        ConcurrentMap<String, String> backParents = new ConcurrentHashMap<>();
        Queue<String> frontNextQueue = new LinkedList<>();
        Queue<String> backNextQueue = new LinkedList<>();

        try {
            api.validate(from);
            api.validate(to);
        } catch (WikipediaApiException e) {
            logException(e);

            throw e;
        }

        frontNextQueue.add(from);
        frontParents.put(from, "");

        backNextQueue.add(to);
        backParents.put(to, "");

        int step = 0;
        String stopover = null;

        while (!frontNextQueue.isEmpty() || !backNextQueue.isEmpty()) {
            step++;

            Queue<String> nextQueue;
            ConcurrentMap<String, String> parents;
            Map<String, String> targets;
            LinkSource source;

            if (step % 2 == 1) {
                // search from front
                nextQueue = frontNextQueue;
                parents = frontParents;
                targets = backParents;
                source = linkSource;
            } else {
                // search from back
                nextQueue = backNextQueue;
                parents = backParents;
                targets = frontParents;
                source = backlinkSource;
            }

            logStep(nextQueue, step);
            nextQueue = concurrentSearch(nextQueue, parents, targets, source);

            // update queue
            if (step % 2 == 1) {
                frontNextQueue = nextQueue;
            } else {
                backNextQueue = nextQueue;
            }

            // front and back is connected by result
            stopover = getDuplicatedKey(frontParents, backParents);

            if (stopover != null) {
                break;
            }
        }

        if (stopover == null) {
            return null;
        }

        // concat both results
        ArrayList<String> frontResult = concatPathList(getPathList(frontParents, from, stopover),
                getPathList(backParents, to, stopover));

        return new RedirectablePath(toRedirectableList(frontResult));
    }

    private Queue<String> concurrentSearch(Queue<String> queue, ConcurrentMap<String, String> parents,
                                           Map<String, String> targets, LinkSource source) throws Exception {
        int coreCount = Runtime.getRuntime().availableProcessors();
        int queueSizePerThread = queue.size() / coreCount;

        logCoreCount(coreCount);

        SearchThread[] threads = new SearchThread[coreCount];

        for (int i = 0; i < coreCount; i++) {
            Queue<String> subQueue;

            if (i != coreCount - 1) {
                subQueue = new LinkedList<>();
                for (int j = 0; j < queueSizePerThread; j++) {
                    subQueue.add(queue.poll());
                }

            } else {
                subQueue = queue;
            }

            threads[i] = new SearchThread(subQueue, parents, targets, source);
            threads[i].start();
        }

        return joinSearchThreads(threads);
    }

    private Queue<String> joinSearchThreads(SearchThread[] threads) throws Exception {
        while (!isAllFinished(threads)) {
            for (SearchThread thread : threads) {
                if (thread.getStatus() == ThreadStatus.FOUND || thread.getStatus() == ThreadStatus.ERROR) {
                    for (SearchThread toInterrupt : threads) {
                        toInterrupt.interrupt();
                    }
                }
            }

            Thread.sleep(100);
        }

        Queue<String> mergedQueue = new LinkedList<>();
        for (SearchThread thread : threads) {
            if (thread.getStatus() != ThreadStatus.NOT_FOUND) {
                return null;
            }

            mergedQueue.addAll(thread.getNextQueue());
        }

        return mergedQueue;
    }

    private boolean isAllFinished(SearchThread[] threads) {
        for (SearchThread thread : threads) {
            if (!thread.getStatus().isFinished()) {
                return false;
            }
        }

        return true;
    }


    // concat two path lists. If frontResult is 'A-B-C' and backResult is 'D-E-C', result is 'A-B-C-E-D'
    private ArrayList<String> concatPathList(ArrayList<String> frontResult, ArrayList<String> backResult) {
        ArrayList<String> result = new ArrayList<>(frontResult);
        List<String> tempList = backResult.subList(0, backResult.size() - 1);

        Collections.reverse(tempList);
        result.addAll(tempList);

        return result;
    }

    // return the key contained by both maps. If not found, return null
    private String getDuplicatedKey(Map<String, String> frontParents, Map<String, String> backParents) {
        Set<String> copiedSet = new HashSet<>(frontParents.keySet());
        copiedSet.retainAll(backParents.keySet());

        if (copiedSet.isEmpty()) {
            return null;
        }

        return copiedSet.iterator().next();
    }

    // follow parents from 'to' until it reaches 'from'
    private ArrayList<String> getPathList(Map<String, String> parents, String from, String to) {
        ArrayList<String> path = new ArrayList<>();

        String current = to;

        while (!current.equals(from)) {
            path.add(current);
            current = parents.get(current);
        }

        path.add(current);
        Collections.reverse(path);

        return path;
    }

    // transform string list to redirectable list
    private ArrayList<RedirectableNode> toRedirectableList(ArrayList<String> original) throws IOException {
        ArrayList<RedirectableNode> result = new ArrayList<>();

        for (String s : original) {
            result.add(new RedirectableNode(api.resolve(s) != null, s));
        }

        return result;
    }

    private void logCoreCount(int coreCount) {
        logger.info(String.format("running on %d cores", coreCount));
    }

    private void logCurrentNode(String currentNode) {
        logger.debug(String.format("%s", currentNode));
    }

    private void logStep(Queue<String> queue, int step) {
        logger.info(String.format("Step %d, Queue size: %d", step, queue.size()));
    }

    private void logException(Exception e) {
        logger.error(e.toString());

        for (StackTraceElement trace : e.getStackTrace()) {
            logger.debug(trace.toString());
        }
    }

    // get links from queue and add them to the parents map. One time use only thread.
    private class SearchThread extends Thread {
        private Queue<String> queue;
        private LinkSource source;
        private ConcurrentMap<String, String> parents;
        private Map<String, String> targets;

        private Queue<String> nextQueue = null;
        private ThreadStatus status = ThreadStatus.NOT_RUN;

        public SearchThread(Queue<String> queue, ConcurrentMap<String, String> parents,
                            Map<String, String> targets, LinkSource source) {
            this.queue = queue;
            this.parents = parents;
            this.targets = targets;
            this.source = source;
        }

        @Override
        public void run() {
            if (status != ThreadStatus.NOT_RUN) {
                return;
            }

            status = ThreadStatus.RUNNING;
            nextQueue = new LinkedList<>();
            boolean targetFound = false;

            while (!queue.isEmpty() & !targetFound) {
                if (Thread.interrupted()) {
                    status = ThreadStatus.ERROR;
                    return;
                }

                String currentNode = queue.poll();
                logCurrentNode(currentNode);
                Set<String> connectedLinks = null;

                try {
                    try {
                        connectedLinks = source.getLinks(currentNode);
                    } catch (RedirectedException ignored) {
                        // do special job to redirected link
                        String redirected = api.resolve(currentNode);

                        if (!parents.containsKey(redirected)) {
                            parents.put(redirected, currentNode);
                            nextQueue.add(redirected);
                            continue;
                        }
                    } catch (WikipediaApiException exception) {
                        continue;
                    }
                } catch (Exception e) {
                    status = ThreadStatus.ERROR;
                    logException(e);

                    return;
                }

                if (connectedLinks == null) {
                    continue;
                }

                // add links to the parents map if they're not added yet.
                for (String connectedNode : connectedLinks) {
                    if (!parents.containsKey(connectedNode)) {
                        parents.put(connectedNode, currentNode);
                        nextQueue.add(connectedNode);
                    }

                    if (targets.containsKey(connectedNode)) {
                        targetFound = true;
                        break;
                    }
                }
            }

            if (targetFound) {
                status = ThreadStatus.FOUND;
            } else {
                status = ThreadStatus.NOT_FOUND;
            }
        }

        public Queue<String> getNextQueue() {
            return nextQueue;
        }

        public ThreadStatus getStatus() {
            return status;
        }
    }

    private enum ThreadStatus {
        NOT_RUN, RUNNING, NOT_FOUND, FOUND, ERROR;

        boolean isFinished() {
            return this == NOT_FOUND || this == FOUND || this == ERROR;
        }
    }
}
