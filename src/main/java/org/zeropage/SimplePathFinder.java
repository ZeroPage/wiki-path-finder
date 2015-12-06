package org.zeropage;

import org.zeropage.log.Logger;
import org.zeropage.path.ConcretePath;
import org.zeropage.path.Path;

import javax.validation.constraints.NotNull;
import java.util.*;

public class SimplePathFinder implements PathFinder {
    private LinkSource source;

    private Logger logger;

    public SimplePathFinder(@NotNull LinkSource source) {
        this.source = source;

        logger = Logger.getInstance();
    }

    @Override
    public Path getPath(String from, String to) throws Exception {
        Map<String, String> parents = new HashMap<>();
        Queue<String> currentQueue = new LinkedList<>();
        Queue<String> nextQueue;
        int step = 1;
        boolean pathFound = false;

        currentQueue.add(from);

        while (!pathFound && !currentQueue.isEmpty()) {
            logStep(currentQueue, step);
            nextQueue = new LinkedList<>();

            while (!pathFound && !currentQueue.isEmpty()) {
                String currentNode = currentQueue.poll();
                logCurrentNode(step, currentNode);
                Set<String> connectedLinks = source.getLinks(currentNode);

                if (connectedLinks == null) {
                    continue;
                }

                for (String connectedNode : connectedLinks) {
                    if (!parents.containsKey(connectedNode)) {
                        parents.put(connectedNode, currentNode);
                        nextQueue.add(connectedNode);
                    }

                    if (to.equals(connectedNode)) {
                        pathFound = true;
                        break;
                    }
                }
            }

            currentQueue = nextQueue;
            step++;
        }

        if (pathFound) {
            return new ConcretePath(getResult(parents, from, to));
        } else {
            return null;
        }
    }

    private void logCurrentNode(int step, String currentNode) {
        logger.debug(String.format("Step %d, %s", step, currentNode));
    }

    private void logStep(Queue<String> currentQueue, int step) {
        logger.info(String.format("Step %d, Queue size: %d", step, currentQueue.size()));
    }

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
}
