import org.junit.Before;
import org.junit.Test;
import path.Path;
import wiki_api.WikipediaApi;

import static org.junit.Assert.*;

public class WikipediaLinkSourceTest {
    WikipediaLinkSource linkSource;
    PathFinder pathFinder;

    @Before
    public void setUp() throws Exception {
        linkSource = new WikipediaLinkSource(new WikipediaApi(WikipediaApi.Language.EN, true));
        pathFinder = new SimplePathFinder(linkSource);
    }

    @Test
    public void testGetLinks() throws Exception {
        Path path = pathFinder.getPath("Apple", "Banana");
        assertEquals(path.length(), 3);
    }
}