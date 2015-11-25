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
        return pathArray.iterator();
    }

    @Override
    public int length() {
        return pathArray.size();

    }
}
