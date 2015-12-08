package org.zeropage.cache;

import java.util.Set;

/**
 * Interface for general cacheStorage
 */
public interface CacheStorage {
    boolean hasKey(String key);

    Set<String> getData(String key);

    void setData(String key, Set<String> data);
}
