import log.Logger;
import path.Path;
import path.RedirectableNode;
import path.RedirectablePath;
import wiki_api.RedirectedException;
import wiki_api.WikipediaApi;

import java.io.IOException;
import java.util.*;

public class WikipediaPathFinder implements PathFinder {
    private WikipediaLinkSource linkSource;
    private WikipediaBacklinkSource backlinkSource;
    private WikipediaApi api;

    private Logger logger;

    public WikipediaPathFinder(WikipediaApi api) {
        this.api = api;
        linkSource = new WikipediaLinkSource(api);
        backlinkSource = new WikipediaBacklinkSource(api);

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
        String result = null;
        boolean lastStepSkipped = false;

        while (true) {
            step++;

            Queue<String> nextQueue;
            Map<String, String> parents;
            LinkSource source;

            if (step % 2 == 1) {
                // search from front
                nextQueue = frontNextQueue;
                parents = frontParents;
                source = linkSource;
            } else {
                // search from back
                nextQueue = backNextQueue;
                parents = backParents;
                source = backlinkSource;
            }

            if (!nextQueue.isEmpty()) {
                logStep(nextQueue, step);
                nextQueue = search(nextQueue, parents, source);
            } else {
                // if both queue is empty, stop the searching
                if (lastStepSkipped) {
                    break;
                } else {
                    lastStepSkipped = true;
                }
            }

            // update queue
            if (step % 2 == 1) {
                frontNextQueue = nextQueue;
            } else {
                backNextQueue = nextQueue;
            }

            // front and back is connected by result
            result = checkResult(frontParents, backParents);

            if (result != null) {
                break;
            }
        }

        if (result == null) {
            return null;
        }

        // concat both results
        ArrayList<String> frontResult = getResult(frontParents, from, result);
        ArrayList<String> backResult = getResult(backParents, to, result);
        backResult.remove(backResult.size() - 1); // remove duplicated result

        Collections.reverse(backResult);
        frontResult.addAll(backResult);

        return new RedirectablePath(toRedirectableList(frontResult));
    }

    // return the key contained by both maps. If not found, return null
    private String checkResult(Map<String, String> frontParents, Map<String, String> backParents) {
        Set<String> copiedSet = new HashSet<>(frontParents.keySet());
        copiedSet.retainAll(backParents.keySet());

        if (copiedSet.isEmpty()) {
            return null;
        }

        return copiedSet.iterator().next();
    }

    // get links from queue and add them to the parents map
    private Queue<String> search(Queue<String> queue, Map<String, String> parents, LinkSource source) throws Exception {
        Queue<String> nextQueue = new LinkedList<>();

        while (!queue.isEmpty()) {
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
            }
        }

        return nextQueue;
    }

    // follow parents from 'to' until it reaches 'from'
    private ArrayList<String> getResult(Map<String, String> parents, String from, String to) {
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
