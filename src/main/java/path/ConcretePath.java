package path;

import java.util.Iterator;

public class ConcretePath implements Path{
    Iterator<String> pathIterator;
    int length;

    public ConcretePath(Iterator<String> pathIterator) {
        this.pathIterator = pathIterator;
        this.length = length();
    }

    @Override
    public Iterator<String> getPathIterator() {
        return pathIterator;
    }

    @Override
    public int length() {
        length = 0;
        while(pathIterator.hasNext()){
            length++;
            pathIterator.next();
        }
        return length;
    }
}
