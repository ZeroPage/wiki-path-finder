import wiki_api.WikipediaApi;

public class WikipediaPathFinder implements PathFinder {
    private WikipediaLinkSource linkSource;
    private WikipediaBacklinkSource backlinkSource;

    public WikipediaPathFinder(WikipediaApi api) {

    }

    @Override
    public RedirectablePath getPath(String from, String to) {
        return null;
    }
}
