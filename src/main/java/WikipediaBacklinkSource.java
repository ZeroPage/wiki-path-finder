import wiki_api.PageNotFoundException;
import wiki_api.WikipediaApi;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WikipediaBacklinkSource implements LinkSource {
    private WikipediaApi api;

    public WikipediaBacklinkSource(WikipediaApi api) {
        this.api = api;
    }

    @Override
    public Set<String> getLinks(String from) throws IOException {
        try {
            return new HashSet<>(Arrays.asList(api.getBacklinks(from)));
        } catch (PageNotFoundException e) {
            return null;
        }
    }
}
