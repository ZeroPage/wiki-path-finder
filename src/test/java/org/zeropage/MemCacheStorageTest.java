package org.zeropage;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.zeropage.cache.MemCacheStorage;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class MemCacheStorageTest {
    MemCacheStorage cache;
    Set<String> testSample;

    @Before
    public void setUp() throws Exception {
        cache = new MemCacheStorage();
        testSample = new HashSet<>();
        testSample.add("dog");
        testSample.add("cat");
        testSample.add("koibito");
        cache.setData("white", testSample);
    }

    @Test
    public void testHasKey() throws Exception {
        assertTrue(cache.hasKey("white"));
        assertFalse(cache.hasKey("black"));
    }

    @Test
    public void testGetData() throws Exception {
        Set<String> test = cache.getData("white");
        assertThat(test, CoreMatchers.is(testSample));
    }

    @Test
    public void testSetData() throws Exception {
        HashSet<String> newValue = new HashSet<>();
        newValue.add("chistmas");
        newValue.add("wine");
        newValue.add("chocolate");

        cache.setData("white", newValue);

        assertThat(cache.getData("white"), CoreMatchers.is(CoreMatchers.not(testSample)));
    }
}