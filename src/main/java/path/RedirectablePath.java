package path;

import java.util.ArrayList;
import java.util.Iterator;

public class RedirectablePath implements Path {
    private ArrayList<RedirectableNode> redirectableNodeArray;

    public RedirectablePath(ArrayList<RedirectableNode> redirectableNodeArray) {
        this.redirectableNodeArray = redirectableNodeArray;
    }

    @Override
    public Iterator<String> getPathIterator() {
        return null;
    }

    @Override
    public int length() {
        return 0;
    }


    public Iterator<String> getRedirectablePathIterator() {
        return null;
    }
}
