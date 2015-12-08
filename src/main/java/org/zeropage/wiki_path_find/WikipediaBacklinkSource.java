package org.zeropage.wiki_path_find;

import org.zeropage.LinkSource;
import org.zeropage.wiki_api.PageNotFoundException;
import org.zeropage.wiki_api.WikipediaApi;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Get backlinks from given Wikipedia API.
 */
public class WikipediaBacklinkSource implements LinkSource {
    private WikipediaApi api;

    /**
     * Get backlinks from given Wikipedia API.
     *
     * @param api Wikipedia API to use.
     */
    public WikipediaBacklinkSource(WikipediaApi api) {
        this.api = api;
    }

    /**
     * Get backlinks from given page title.
     *
     * @param from Source page title to get backlinks. Should be normalized.
     * @return Backlinks from given page. If page is not exist, null is returned.
     * @throws IOException Network is not available.
     */
    @Override
    public Set<String> getLinks(String from) throws IOException {
        try {
            return new HashSet<>(Arrays.asList(api.getBacklinks(from)));
        } catch (PageNotFoundException e) {
            return null;
        }
    }
}
