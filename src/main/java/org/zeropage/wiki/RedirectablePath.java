package org.zeropage.wiki;

import org.zeropage.Path;
import org.zeropage.basic.UnmodifiableIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class of path presentation of redirectable nodes.
 */
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

    /**
     * Get strings that describe inner nodes.
     * @return decription of path.
     */
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

    /**
     * Get iterator of string of nodes.
     * @return iterator of node names
     */
    @Override
    public Iterator<String> getPathIterator() {

        return new UnmodifiableIterator(pathArray.iterator());
    }

    /**
     * @return length of path
     */
    @Override
    public int length() {
        return pathArray.size();
    }

    /**
     * Get iterator of string of nodes including redirected.
     * @return iterator of node names
     */
    public Iterator<String> getRedirectablePathIterator() {
        return new UnmodifiableIterator(redirectablePathArray.iterator());
    }

    /**
     * @return length of path including redirected nodes
     */
    public int redirectableLength() {
        return redirectablePathArray.size();
    }
}
