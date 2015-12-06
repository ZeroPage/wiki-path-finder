package org.zeropage;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MemCacheStorage implements CacheStorage {
    private ConcurrentHashMap<String,Set<String>> cacheTable = new ConcurrentHashMap<>(100,1);

    @Override
    public boolean hasKey(String key) { return cacheTable.containsKey(key); }

    @Override
    public Set<String> getData(String key) {
        return cacheTable.get(key);
    }

    @Override
    public void setData(String key, Set<String> data) {
        cacheTable.put(key,data);
    }
}
