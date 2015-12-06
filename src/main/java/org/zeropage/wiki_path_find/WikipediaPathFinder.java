package org.zeropage.wiki_path_find;

import org.zeropage.CacheLinkSource;
import org.zeropage.LinkSource;
import org.zeropage.MemCacheStorage;
import org.zeropage.PathFinder;
import org.zeropage.log.Logger;
import org.zeropage.path.RedirectableNode;
import org.zeropage.path.RedirectablePath;
import org.zeropage.wiki_api.RedirectedException;
import org.zeropage.wiki_api.WikipediaApi;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WikipediaPathFinder implements PathFinder {
    private LinkSource linkSource;
    private LinkSource backlinkSource;
    private WikipediaApi api;

    private Logger logger;

    public WikipediaPathFinder(WikipediaApi api) {
        this.api = api;
        linkSource = new CacheLinkSource(new WikipediaLinkSource(api), new MemCacheStorage());
        backlinkSource = new CacheLinkSource(new WikipediaBacklinkSource(api), new MemCacheStorage());

        logger = Logger.getInstance();
    }

    @Override
    public RedirectablePath getPath(String from, String to) throws Exception {
        ConcurrentMap<String, String> frontParents = new ConcurrentHashMap<>();
        ConcurrentMap<String, String> backParents = new ConcurrentHashMap<>();
        Queue<String> frontNextQueue = new LinkedList<>();
        Queue<String> backNextQueue = new LinkedList<>();

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
                    }
                } catch (Exception e) {
                    status = ThreadStatus.ERROR;
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
