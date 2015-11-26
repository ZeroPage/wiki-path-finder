package path;

import java.util.Iterator;

public class UnmodifiableIterator<S> implements Iterator {
    Iterator base;

    public UnmodifiableIterator(Iterator base) {
        this.base = base;
    }

    @Override
    public boolean hasNext() {
        return base.hasNext();
    }

    @Override
    public Object next() {
        return base.next();
    }

    @Override
    public void remove() {
    }
}
