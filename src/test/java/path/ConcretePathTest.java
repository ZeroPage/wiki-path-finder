package path;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

/**
 * Created by miri1 on 2015-11-25.
 */
public class ConcretePathTest {
    private ConcretePath concretePath;
    private final ArrayList<String> TEST_ARRAYLIST = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        TEST_ARRAYLIST.add("Apple");
        TEST_ARRAYLIST.add("Fruit tree");
        concretePath = new ConcretePath(TEST_ARRAYLIST);
    }

    @Test
    public void testGetPathIterator() {
        Iterator<String> iterator = concretePath.getPathIterator();
        assertEquals(iterator.next(), "Apple");
        assertEquals(iterator.next(),"Fruit tree");
    }

    @Test
    public void testLength() {
        assertEquals(concretePath.length(), 2);
    }

    @Test
    public void testRemove() {
        Iterator<String> iterator = concretePath.getPathIterator();
        iterator.remove();
        assertEquals(TEST_ARRAYLIST.size(), 2);
        assertEquals(iterator.next(), "Apple");
    }
}