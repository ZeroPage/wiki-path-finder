package path;

import java.util.ArrayList;
import java.util.Iterator;

public class ConcretePath implements Path{
    private ArrayList<String> pathArray;

    public ConcretePath(ArrayList<String> pathArray) {
        this.pathArray = pathArray;
    }

    @Override
    public Iterator<String> getPathIterator() {
        return new UnmodifiableIterator<String>(pathArray.iterator());
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
