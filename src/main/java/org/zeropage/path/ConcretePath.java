package org.zeropage.path;

import java.util.Iterator;
import java.util.List;

/**
 * Class implements Path class.
 * This is Path that doesn't deal with redirection.
 */
public class ConcretePath implements Path {
    /**
     * This is list that contains wiki path sequentially.
     */
    private List<String> pathArray;

    /**
     * Class constructor get list of path.
     */
    public ConcretePath(List<String> pathArray) {
        this.pathArray = pathArray;
    }

    /**
     * This method is used to make iterator which has sequential path.
     * @return UnmodifiableIterator This returns iterator that can't remove data in List.
     * @see UnmodifiableIterator
     */
    @Override
    public Iterator<String> getPathIterator() {
        return new UnmodifiableIterator(pathArray.iterator());
    }

    /**
     * This method is used to get size of path.
     * @return int This returns size of list of array.
     */
    @Override
    public int length() {
        return pathArray.size();
    }

    /**
     * This method make string from path.
     * @return String This returns path in String form.
     */
    @Override
    public String toString() {
        String s = "";
        for (String string : pathArray) {
            s += "[" + string + "]";
        }
        return s;
    }
}
