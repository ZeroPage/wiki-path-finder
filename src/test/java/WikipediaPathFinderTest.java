import org.zeropage.wiki_path_find.WikipediaPathFinder;
import org.zeropage.log.Logger;
import org.zeropage.log.OutputStreamLogListener;
import org.junit.Before;
import org.junit.Test;
import org.zeropage.path.RedirectablePath;
import org.zeropage.wiki_api.WikipediaApi;

import static org.junit.Assert.*;

public class WikipediaPathFinderTest {
    private WikipediaPathFinder pathFinder;

    @Before
    public void setUp() throws Exception {
        pathFinder = new WikipediaPathFinder(new WikipediaApi(WikipediaApi.Language.EN));
    }

    @Test
    public void testGetPath() throws Exception {
        RedirectablePath path = pathFinder.getPath("JUnit", "Banana");
        assertEquals(path.length(), 5);
    }
}