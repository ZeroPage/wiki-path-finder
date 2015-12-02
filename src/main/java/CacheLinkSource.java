import java.util.Set;

public class CacheLinkSource implements LinkSourceDecorator {
    CacheStorage cache;
    LinkSource innerSource;

    public CacheLinkSource(LinkSource innerObject, CacheStorage cacheStorage) {
        this.innerSource = innerObject;
        this.cache = cacheStorage;
    }

    @Override
    public Set<String> getLinks(String from) throws Exception {
        if(this.cache.getData(from)==null) {
            if(innerSource==null) {
                return null;
            } else {
                return innerSource.getLinks(from);
            }
        }
        return this.cache.getData(from);
    }
}
