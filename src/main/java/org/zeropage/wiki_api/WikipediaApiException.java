package org.zeropage.wiki_api;

public class WikipediaApiException extends RuntimeException {
    public WikipediaApiException() {

    }

    public WikipediaApiException(String message) {
        super(message);
    }
}
