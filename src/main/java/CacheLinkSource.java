import java.util.Set;

public class CacheLinkSource implements LinkSourceDecorator {
    CacheStorage cache;

    public CacheLinkSource(CacheStorage cacheStorage) {
        cache = cacheStorage;
    }

    @Override
    public Set<String> getLinks(String from) {
        return cache.getData(from);
    }
}
