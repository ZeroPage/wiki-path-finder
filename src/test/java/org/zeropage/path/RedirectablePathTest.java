package org.zeropage.path;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class RedirectablePathTest {
    private RedirectablePath redirectablePath;
    private final List<RedirectableNode> TEST_ARRAYLIST = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        TEST_ARRAYLIST.add(new RedirectableNode(false, "Apple"));
        TEST_ARRAYLIST.add(new RedirectableNode(true, "Fruit tree"));
        TEST_ARRAYLIST.add(new RedirectableNode(false, "tree"));
        redirectablePath = new RedirectablePath(TEST_ARRAYLIST);

    }

    @Test
    public void testGetPathIterator() throws Exception {
        Iterator<String> iterator = redirectablePath.getPathIterator();
        assertEquals(iterator.next(), "Apple");
        assertEquals(iterator.next(), "tree");
    }

    @Test
    public void testLength() throws Exception {
        assertEquals(redirectablePath.length(), 2);
    }

    @Test
    public void testGetRedirectablePathIterator() throws Exception {
        Iterator<String> iterator = redirectablePath.getRedirectablePathIterator();
        assertEquals(iterator.next(), "Apple");
        assertEquals(iterator.next(), "Fruit tree");
        assertEquals(iterator.next(), "tree");
    }

    @Test
    public void testRedirectableLength() throws Exception {
        assertEquals(redirectablePath.redirectableLength(), 3);
    }

    @Test
    public void testToString() {
        assertEquals(redirectablePath.toString(), "[Apple][Fruit tree 'redirected'][tree]");
    }

}