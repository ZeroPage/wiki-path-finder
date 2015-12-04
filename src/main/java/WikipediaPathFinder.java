import log.Logger;
import path.RedirectableNode;
import path.RedirectablePath;
import wiki_api.RedirectedException;
import wiki_api.WikipediaApi;

import java.io.IOException;
import java.util.*;

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
        Map<String, String> frontParents = new HashMap<>();
        Map<String, String> backParents = new HashMap<>();
        Queue<String> frontNextQueue = new LinkedList<>();
        Queue<String> backNextQueue = new LinkedList<>();

        frontNextQueue.add(from);
        frontParents.put(from, null);

        backNextQueue.add(to);
        backParents.put(to, null);

        int step = 0;
        String stopover = null;

        while (!frontNextQueue.isEmpty() || !backNextQueue.isEmpty()) {
            step++;

            Queue<String> nextQueue;
            Map<String, String> parents;
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
            nextQueue = search(nextQueue, parents, targets, source);

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

    // get links from queue and add them to the parents map
    private Queue<String> search(Queue<String> queue, Map<String, String> parents,
                                 Map<String, String> targets, LinkSource source) throws Exception {
        Queue<String> nextQueue = new LinkedList<>();
        boolean stopSearching = false;

        while (!queue.isEmpty() & !stopSearching) {
            String currentNode = queue.poll();
            logCurrentNode(currentNode);
            Set<String> connectedLinks = null;

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
                    stopSearching = true;
                    break;
                }
            }
        }

        return nextQueue;
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

    private void logCurrentNode(String currentNode) {
        logger.debug(String.format("%s", currentNode));
    }

    private void logStep(Queue<String> queue, int step) {
        logger.info(String.format("Step %d, Queue size: %d", step, queue.size()));
    }
}
