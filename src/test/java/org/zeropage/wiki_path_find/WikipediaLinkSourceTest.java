package org.zeropage.wiki_path_find;

import org.junit.Before;
import org.junit.Test;
import org.zeropage.PathFinder;
import org.zeropage.SimplePathFinder;
import org.zeropage.wiki_path_find.WikipediaLinkSource;
import org.zeropage.path.Path;
import org.zeropage.wiki_api.WikipediaApi;

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