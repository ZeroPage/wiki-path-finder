package path;

import java.util.Iterator;
import java.util.List;

public class ConcretePath implements Path{
    private List<String> pathArray;

    public ConcretePath(List<String> pathArray) {
        this.pathArray = pathArray;
    }

    @Override
    public Iterator<String> getPathIterator() {
        return new UnmodifiableIterator<>(pathArray.iterator());
    }

    @Override
    public int length() {
        return pathArray.size();
    }

    @Override
    public String toString() {
        String s = new String();
        for(String string : pathArray) {
            s += "[" + string + "]";
        }
        return s;
    }
}
