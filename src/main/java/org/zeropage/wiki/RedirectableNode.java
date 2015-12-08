package org.zeropage.wiki;

/**
 * Class for graph node including redirection flag.
 */
public class RedirectableNode {
    public RedirectableNode(boolean redirecting_, String name_) {
        this.redirecting = redirecting_;
        this.name = name_;
    }

    public boolean redirecting;
    public String name;
}
