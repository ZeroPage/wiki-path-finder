package org.zeropage.wiki;

import org.junit.Before;
import org.junit.Test;
import org.zeropage.wiki.api.WikipediaApi;

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