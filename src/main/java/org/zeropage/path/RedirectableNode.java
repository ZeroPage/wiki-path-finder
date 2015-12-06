package org.zeropage.path;

public class RedirectableNode {
    public RedirectableNode(boolean redirecting_, String name_)
    {
        this.redirecting = redirecting_;
        this.name = name_;
    }

    public boolean redirecting;
    public String name;
}
