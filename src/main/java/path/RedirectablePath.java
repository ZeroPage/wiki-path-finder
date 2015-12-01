package path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class RedirectablePath implements Path {
    private ArrayList<RedirectableNode> redirectableNodeArray;

    private ArrayList<String> pathArray;
    private ArrayList<String> redirectablePathArray;

    public RedirectablePath(ArrayList<RedirectableNode> redirectableNodeArray) {
        this.redirectableNodeArray = redirectableNodeArray;
        pathArray = new ArrayList<String>();
        for(RedirectableNode node: redirectableNodeArray)
        {
            if(node.redirecting == false)
            {
                pathArray.add(node.name);
            }
        }
        redirectablePathArray = new ArrayList<String>();
        for(RedirectableNode node: redirectableNodeArray)
        {
            redirectablePathArray.add(node.name);
        }
    }

    @Override
    public String toString() {
        String s = new String();
        for(RedirectableNode node: redirectableNodeArray)
        {
            if(node.redirecting)
            {
                s += "[" + node.name + " 'redirected']";
            }
            else {
                s += "[" + node.name + "]";
            }
        }

        return s;
    }

    @Override
    public Iterator<String> getPathIterator() {

        return new UnmodifiableIterator<String>(pathArray.iterator());
    }

    @Override
    public int length() {
        return pathArray.size();
    }

    public Iterator<String> getRedirectablePathIterator() {
        return new UnmodifiableIterator<String>(redirectablePathArray.iterator());
    }

    public int redirectableLength() {
        return redirectablePathArray.size();
    }
}
