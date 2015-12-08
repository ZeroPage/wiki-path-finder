package org.zeropage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.zeropage.cache.SqliteCacheStorage;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SqliteCacheStorageTest {

    private SqliteCacheStorage cache;

    private Set<String> TEST_DATA = new HashSet<>();
    private Set<String> TEST_DATA2 = new HashSet<>();
    private String TEST_KEY = "wikifinders";
    private String TEST_KEY2 = "k`~!@#$%^&*()_+-=[]{}\\|;:'\",./<>?";
    private String TEST_DB = "tests.db";

    @Before
    public void setUp() throws Exception {
        cache = new SqliteCacheStorage(new File(TEST_DB));

        TEST_DATA.add("dog");
        TEST_DATA.add("cat");
        TEST_DATA.add("koibito");


        TEST_DATA2.add("dog`~!@#$%^&*()_+-=[]{}\\|;:'\",./<>?");
        TEST_DATA2.add("cat`~!@#$%^&*()_+-=[]{}\\|;:'\",./<>?");
        TEST_DATA2.add("`~!@#$%^&*()_+-=[]{}\\|;:'\",./<>?koibito");

    }

    @After
    public void tearDown() {
        File testfile = new File(TEST_DB);
        testfile.delete();
    }

    @Test
    public void testHasKey() throws Exception {
        cache.setData(TEST_KEY, TEST_DATA);
        assertTrue(cache.hasKey(TEST_KEY));
        assertFalse(cache.hasKey(TEST_KEY + TEST_KEY));
    }

    @Test
    public void testSetData() throws Exception {
        cache.setData(TEST_KEY2, TEST_DATA2);
    }

    @Test
    public void testSetNullData() throws Exception {
        cache.setData(TEST_KEY, null);
        cache.setData(null, TEST_DATA2);
        cache.setData(null, null);
    }

    @Test
    public void testGetData() throws Exception {
        cache.setData(TEST_KEY, TEST_DATA);
        assertArrayEquals(cache.getData(TEST_KEY).toArray(), TEST_DATA.toArray());
    }

    @Test
    public void testGetDataByNullKey() throws Exception {
        cache.getData(null).toArray();
    }

    @Test
    public void testGetSpecialCharacterData() throws Exception {
        cache.setData(TEST_KEY2, TEST_DATA2);
        assertArrayEquals(cache.getData(TEST_KEY2).toArray(), TEST_DATA2.toArray());
    }

}