package org.zeropage;

import org.junit.Before;
import org.junit.Test;
import org.zeropage.MockLinkSource;
import org.zeropage.SimplePathFinder;
import org.zeropage.path.Path;

import java.util.Iterator;

import static org.junit.Assert.*;

public class SimplePathFinderTest {
    private SimplePathFinder pathFinder;

    @Before
    public void setUp() throws Exception {
        pathFinder = new SimplePathFinder(new MockLinkSource());
    }

    @Test
    public void testGetPath() throws Exception {
        Path path = pathFinder.getPath("0", "4");
        Iterator<String> iterator = path.getPathIterator();

        assertEquals(iterator.next(), "0");
        assertEquals(iterator.next(), "2");
        assertEquals(iterator.next(), "3");
        assertEquals(iterator.next(), "4");
    }

    @Test
    public void testGetPathInvalidCase() throws Exception {
        assertNull(pathFinder.getPath("-", "4"));
        assertNull(pathFinder.getPath("0", "-"));
    }
}