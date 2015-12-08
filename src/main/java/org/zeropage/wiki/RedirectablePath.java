package org.zeropage.wiki;

import org.zeropage.Path;
import org.zeropage.basic.UnmodifiableIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RedirectablePath implements Path {
    private List<RedirectableNode> redirectableNodeArray;

    private List<String> pathArray;
    private List<String> redirectablePathArray;

    public RedirectablePath(List<RedirectableNode> redirectableNodeArray) {
        this.redirectableNodeArray = redirectableNodeArray;
        pathArray = new ArrayList<>();
        for (RedirectableNode node : redirectableNodeArray) {
            if (!node.redirecting) {
                pathArray.add(node.name);
            }
        }
        redirectablePathArray = new ArrayList<>();
        for (RedirectableNode node : redirectableNodeArray) {
            redirectablePathArray.add(node.name);
        }
    }

    @Override
    public String toString() {
        String s = "";
        for (RedirectableNode node : redirectableNodeArray) {
            if (node.redirecting) {
                s += "[" + node.name + " 'redirected']";
            } else {
                s += "[" + node.name + "]";
            }
        }

        return s;
    }

    @Override
    public Iterator<String> getPathIterator() {

        return new UnmodifiableIterator(pathArray.iterator());
    }

    @Override
    public int length() {
        return pathArray.size();
    }

    public Iterator<String> getRedirectablePathIterator() {
        return new UnmodifiableIterator(redirectablePathArray.iterator());
    }

    public int redirectableLength() {
        return redirectablePathArray.size();
    }
}
