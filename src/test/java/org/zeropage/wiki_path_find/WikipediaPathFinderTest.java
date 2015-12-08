package org.zeropage.wiki_path_find;

import org.zeropage.log.LogListener;
import org.zeropage.wiki_path_find.WikipediaPathFinder;
import org.zeropage.log.Logger;
import org.zeropage.log.OutputStreamLogListener;
import org.junit.Before;
import org.junit.Test;
import org.zeropage.path.RedirectablePath;
import org.zeropage.wiki_api.WikipediaApi;

import java.io.File;

import static org.junit.Assert.*;

public class WikipediaPathFinderTest {
    private WikipediaPathFinder pathFinder;
    private WikipediaPathFinder cachePathFinder;

    @Before
    public void setUp() throws Exception {
        pathFinder = new WikipediaPathFinder(new WikipediaApi(WikipediaApi.Language.KO));
        cachePathFinder = new WikipediaPathFinder(new WikipediaApi(WikipediaApi.Language.EN), new File("cache/"));
    }

    @Test
    public void testGetPath() throws Exception {
        RedirectablePath cachePath = cachePathFinder.getPath("JUnit", "Banana");
        assertEquals(cachePath.length(), 5);

        RedirectablePath path = pathFinder.getPath("A", "B");
        assertEquals(path.length(), 2);
    }
}