package org.zeropage.path;

import java.util.Iterator;

/**
 * Class implements Iterator
 * This is Iterator doesn't remove list element.
 */
public class UnmodifiableIterator implements Iterator<String> {
    /**
     * This is base Iterator, remove() works.
     */
    Iterator<String> base;

    /**
     * Class constructor get base iterator.
     */
    public UnmodifiableIterator(Iterator<String> base) {
        this.base = base;
    }

    /**
     * same with base iterator.
     * @return boolean
     */
    @Override
    public boolean hasNext() {
        return base.hasNext();
    }

    /**
     * same with base iterator.
     * @return String
     */
    @Override
    public String next() {
        return base.next();
    }

    /**
     * This do nothing because Path must not be removed.
     */
    @Override
    public void remove() {
    }
}
