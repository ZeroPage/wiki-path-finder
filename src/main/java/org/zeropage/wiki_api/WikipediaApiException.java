package org.zeropage.wiki_api;

/**
 * Exception caused by WikipediaApi class.
 */
public class WikipediaApiException extends RuntimeException {
    public WikipediaApiException() {

    }

    public WikipediaApiException(String message) {
        super(message);
    }
}
