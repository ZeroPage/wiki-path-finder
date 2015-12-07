package org.zeropage;

import java.util.Set;

public interface CacheStorage {
    boolean hasKey(String key);

    Set<String> getData(String key);

    void setData(String key, Set<String> data);
}
