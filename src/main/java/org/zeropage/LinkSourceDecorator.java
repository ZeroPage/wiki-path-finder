package org.zeropage;

/**
 * Decorator for the LinkSource object.
 */
public abstract class LinkSourceDecorator implements LinkSource {
    protected LinkSource innerSource = null;
}
