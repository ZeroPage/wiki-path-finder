package org.zeropage.wiki_api;

public class PageNotFoundException extends WikipediaApiException {
    public PageNotFoundException() {

    }

    public PageNotFoundException(String pageName) {
        super(String.format("%s is not found", pageName));
    }
}
