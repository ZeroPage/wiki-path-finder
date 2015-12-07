package org.zeropage;

import java.util.Set;

public class CacheLinkSource extends LinkSourceDecorator {
    CacheStorage cache;

    public CacheLinkSource(LinkSource innerObject, CacheStorage cacheStorage) {
        this.innerSource = innerObject;
        this.cache = cacheStorage;
    }

    @Override
    public Set<String> getLinks(String from) throws Exception {
        if (this.cache.hasKey(from)) {
            Set<String> result = this.cache.getData(from);
            if (result != null) {
                return result;
            }
        }

        if (innerSource == null) {
            return null;
        } else {
            Set<String> result = innerSource.getLinks(from);
            this.cache.setData(from, result);
            return result;
        }

    }
}
