import java.util.Iterator;

public class RedirectablePath implements Path {

    @Override
    public Iterator<String> getPathIterator() {
        return null;
    }

    @Override
    public int length() {
        return 0;
    }

    public Iterator<RedirectableNode> getRedirectablePathIterator() {
        return null;
    }
}
