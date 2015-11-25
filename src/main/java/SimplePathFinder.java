import path.ConcretePath;
import path.Path;

import javax.validation.constraints.NotNull;
import java.util.*;

public class SimplePathFinder implements PathFinder {
    private LinkSource source;

    public SimplePathFinder(@NotNull LinkSource source) {
        this.source = source;
    }

    @Override
    public Path getPath(String from, String to) {
        Map<String, String> parents = new HashMap<>();
        Queue<String> currentQueue = new LinkedList<>();
        Queue<String> nextQueue;
        boolean pathFound = false;

        currentQueue.add(from);

        while (!pathFound && !currentQueue.isEmpty()) {
            nextQueue = new LinkedList<>();

            while (!pathFound && !currentQueue.isEmpty()) {
                String currentNode = currentQueue.poll();
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
        }

        if (pathFound) {
            return new ConcretePath(getResult(parents, from, to));
        } else {
            return null;
        }
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
