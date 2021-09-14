import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> implements Cache<K, V> {

    private final SequentialList<Map.Entry<K, V>> orderedValues = new SequentialList<>();
    private final HashMap<K, LinkedNode<Map.Entry<K, V>>> cache = new HashMap<>();
    private final int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public V get(K key) {
        LinkedNode<Map.Entry<K, V>> holder = cache.get(key);
        if (holder != null) {
            V value = holder.getValue().getValue();
            orderedValues.remove(holder);
            cache.put(key, orderedValues.addFirst(new AbstractMap.SimpleEntry<>(key, value)));

            assert cache.size() == orderedValues.size()
                : "Map and list must contain the same number of items";
            
            return value;
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        if (cache.containsKey(key)) {
            orderedValues.remove(cache.get(key));
        } else if (cache.size() == capacity) {
            cache.remove(orderedValues.removeLast().getValue().getKey());
        }
        cache.put(key, orderedValues.addFirst(new AbstractMap.SimpleEntry<>(key, value)));

        assert cache.size() == orderedValues.size()
            : "Map and list must contain the same number of items";
    }
}
