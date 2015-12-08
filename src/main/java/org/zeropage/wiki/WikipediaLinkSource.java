package org.zeropage.wiki;

import org.zeropage.LinkSource;
import org.zeropage.wiki.api.PageNotFoundException;
import org.zeropage.wiki.api.WikipediaApi;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Get frontlinks from given Wikipedia API.
 */
public class WikipediaLinkSource implements LinkSource {
    private WikipediaApi api;

    /**
     * Get frontlinks from given Wikipedia API.
     *
     * @param api Wikipedia API to use.
     */
    public WikipediaLinkSource(WikipediaApi api) {
        this.api = api;
    }

    /**
     * Get frontlinks from given page title.
     *
     * @param from Source page title to get frontlinks. Should be normalized.
     * @return Links from given page. If page is not exist, null is returned.
     * @throws IOException Network is not available.
     */
    @Override
    public Set<String> getLinks(String from) throws IOException {
        try {
            return new HashSet<>(Arrays.asList(api.getLinks(from)));
        } catch (PageNotFoundException e) {
            return null;
        }
    }
}
