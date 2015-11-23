import java.util.Set;

public interface CacheStorage {
    Set<String> getData(String key);
    void setData(String key, Set<String> data);
}
