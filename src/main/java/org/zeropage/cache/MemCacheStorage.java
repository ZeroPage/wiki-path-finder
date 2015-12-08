package org.zeropage.cache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of CacheStorage handling cache saved on memory
 */
public class MemCacheStorage implements CacheStorage {
    private ConcurrentHashMap<String, Set<String>> cacheTable;


    public MemCacheStorage() {
        this.cacheTable = new ConcurrentHashMap<>(100, 1);
    }

    /**
     *
     * @param key target keyword
     * @return If cache about keyword exists, return true. Otherwise, return false.
     */
    @Override
    public boolean hasKey(String key) {
        return cacheTable.containsKey(key);
    }

    /**
     *
     * @param key target keyword
     * @return cached data about keyword. If the data not exists, return null.
     */
    @Override
    public Set<String> getData(String key) {
        return cacheTable.get(key);
    }

    /**
     *
     * @param key target keyword to be saved is key for the map.
     * @param data Data related with keyword. Saved as value in the map.
     */
    @Override
    public void setData(String key, Set<String> data) {
        if (key != null && data != null) {
            cacheTable.put(key, data);
        }
    }
}
