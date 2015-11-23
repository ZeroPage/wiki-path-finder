import wiki_api.WikipediaApi;

import java.util.Set;

public class WikipediaBacklinkSource implements LinkSource {
    public WikipediaBacklinkSource(WikipediaApi api) {

    }

    @Override
    public Set<String> getLinks(String from) {
        return null;
    }
}
