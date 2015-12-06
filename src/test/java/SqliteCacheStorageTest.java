import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.zeropage.SqliteCacheStorage;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SqliteCacheStorageTest {

    private SqliteCacheStorage cache;

    private Set<String> TEST_DATA = new HashSet<>();
    private String TEST_KEY = "wikifinders";
    private String TEST_DB = "tests.db";

    @Before
    public void setUp() throws Exception {
        cache = new SqliteCacheStorage(TEST_DB);

        TEST_DATA.add("dog");
        TEST_DATA.add("cat");
        TEST_DATA.add("koibito");

    }

    @After
    public void tearDown(){

        File testfile = new File(TEST_DB);
        testfile.delete();
    }

    @Test
    public void testHasKey() throws Exception {
        cache.setData(TEST_KEY, TEST_DATA);
        assertTrue(cache.hasKey(TEST_KEY));
        assertFalse(cache.hasKey(TEST_KEY+TEST_KEY));
    }

    @Test
    public void testGetData() throws Exception {
        cache.setData(TEST_KEY, TEST_DATA);
        assertArrayEquals(cache.getData(TEST_KEY).toArray(), TEST_DATA.toArray());
    }
    
}