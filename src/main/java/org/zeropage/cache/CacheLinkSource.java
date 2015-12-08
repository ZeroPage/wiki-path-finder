package org.zeropage.cache;

import org.zeropage.LinkSource;
import org.zeropage.LinkSourceDecorator;

import java.util.Set;

/**
 *Implementation of LinkSource Decorator, which retrieves data from cacheStorage.
 */
public class CacheLinkSource extends LinkSourceDecorator {
    CacheStorage cache;

    /**
     *
     * @param innerObject Object should be wrapped by decorator
     * @param cacheStorage data source of the class
     */
    public CacheLinkSource(LinkSource innerObject, CacheStorage cacheStorage) {
        this.innerSource = innerObject;
        this.cache = cacheStorage;
    }

    /**
     *
     * retrieving cached data mapped with given keyword parameter
     * @param from Keyword to be searched from cacheStorage
     * @return result of search from cacheStorage. If there's no result, pass the task to innerSource.
     * @throws Exception when problem occurs so that the data can't retrieved(ex.network problem)
     */
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
