package org.zeropage.path;

import java.util.Iterator;

public class UnmodifiableIterator implements Iterator<String> {
    Iterator<String> base;

    public UnmodifiableIterator(Iterator<String> base) {
        this.base = base;
    }

    @Override
    public boolean hasNext() {
        return base.hasNext();
    }

    @Override
    public String next() {
        return base.next();
    }

    @Override
    public void remove() {
    }
}
