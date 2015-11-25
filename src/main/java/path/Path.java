package path;

import java.util.Iterator;

public interface Path {
    Iterator<String> getPathIterator();
    int length();
}
