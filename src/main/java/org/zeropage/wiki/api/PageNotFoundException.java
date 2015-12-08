package org.zeropage.wiki.api;

/**
 * Exception caused by non-existing page.
 */
public class PageNotFoundException extends WikipediaApiException {
    public PageNotFoundException() {

    }

    public PageNotFoundException(String pageName) {
        super(String.format("%s is not found", pageName));
    }
}
