package org.zeropage.wiki_path_find;

import org.zeropage.LinkSource;
import org.zeropage.wiki_api.PageNotFoundException;
import org.zeropage.wiki_api.WikipediaApi;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WikipediaLinkSource implements LinkSource {
    private WikipediaApi api;

    public WikipediaLinkSource(WikipediaApi api) {
        this.api = api;
    }

    @Override
    public Set<String> getLinks(String from) throws IOException {
        try {
            return new HashSet<>(Arrays.asList(api.getLinks(from)));
        } catch (PageNotFoundException e) {
            return null;
        }
    }
}