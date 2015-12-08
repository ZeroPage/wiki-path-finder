package org.zeropage.wiki_api;

public class NotNormalizedException extends WikipediaApiException {
    public NotNormalizedException() {

    }

    public NotNormalizedException(String pageName) {
        super(String.format("%s is not normalized", pageName));
    }

    public NotNormalizedException(String pageName, String normalizedPageName) {
        super(String.format("%s is not normalized. Try %s", pageName, normalizedPageName));
    }
}
