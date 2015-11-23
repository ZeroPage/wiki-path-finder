import wiki_api.WikipediaApi;

import java.util.Set;

public class WikipediaLinkSource implements LinkSource {
    public WikipediaLinkSource(WikipediaApi api) {

    }

    @Override
    public Set<String> getLinks(String from) {
        return null;
    }
}
