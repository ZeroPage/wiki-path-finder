import org.junit.Before;
import org.junit.Test;
import path.RedirectablePath;
import wiki_api.WikipediaApi;

import static org.junit.Assert.*;

public class WikipediaPathFinderTest {
    private WikipediaPathFinder pathFinder;

    @Before
    public void setUp() throws Exception {
        pathFinder = new WikipediaPathFinder(new WikipediaApi(WikipediaApi.Language.EN));
    }

    @Test
    public void testGetPath() throws Exception {
        RedirectablePath path = pathFinder.getPath("Apple", "Banana");
        assertEquals(path.length(), 3);
    }
}