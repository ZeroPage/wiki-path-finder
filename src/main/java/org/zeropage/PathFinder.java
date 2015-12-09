package org.zeropage;

/**
 * Defines path searching algorithm with given two nodes.
 */
public interface PathFinder {
    Path getPath(String from, String to) throws Exception;
}
