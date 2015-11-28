import java.util.Hashtable;
import java.util.Set;

public class MemCacheStorage implements CacheStorage {
    private static Hashtable<String,Set<String>> cacheTable = new Hashtable<>(100,1);

    @Override
    public Set<String> getData(String key) {
        return cacheTable.get(key);
    }

    @Override
    public void setData(String key, Set<String> data) {
        cacheTable.put(key,data);
    }
}
