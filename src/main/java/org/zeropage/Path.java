package org.zeropage;

import java.util.Iterator;

/**
 * The result of path finding. Consists of sequential Strings.
 */
public interface Path {
    Iterator<String> getPathIterator();

    int length();
}
