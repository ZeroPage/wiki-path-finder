import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class CacheLinkSourceTest {
    LinkSource linkSource;
    CacheStorage testCache;
    Set<String> testValue,testInnerValue;
    MockLinkSource testInnerObject;

    @Before
    public void setUp() throws Exception {
        MemCacheStorage memCache = new MemCacheStorage();

        testValue = new HashSet<>();
        testValue.add("dog");
        testValue.add("cat");
        testValue.add("koibito");
        memCache.setData("white", testValue);

        testInnerObject = new MockLinkSource();
        testInnerValue = testInnerObject.getLinks("0");

        testCache = memCache;

        linkSource = new CacheLinkSource(testInnerObject,testCache);
    }

    @Test
    public void testGetLinks() throws Exception {
        assertThat(linkSource.getLinks("white"), CoreMatchers.is(testValue));
        assertThat(linkSource.getLinks("0"), CoreMatchers.is(testInnerValue));
    }
}