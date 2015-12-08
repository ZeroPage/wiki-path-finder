package org.zeropage.wiki.api;

/**
 * Exception caused by page is redirected.
 */
public class RedirectedException extends WikipediaApiException {
    public RedirectedException() {

    }

    public RedirectedException(String pageName) {
        super(String.format("%s is not normalized", pageName));
    }

    public RedirectedException(String pageName, String redirectedPageName) {
        super(String.format("%s is not normalized. Try %s", pageName, redirectedPageName));
    }
}
