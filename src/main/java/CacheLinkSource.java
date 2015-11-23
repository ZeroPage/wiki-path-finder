import java.util.Set;

public class CacheLinkSource implements LinkSourceDecorator {

    public CacheLinkSource(CacheStorage cacheStorage) {

    }

    @Override
    public Set<String> getLinks(String from) {
        return null;
    }
}
