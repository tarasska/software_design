import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> implements Cache<K, V> {

    private final SequentialList<Map.Entry<K, V>> orderedValues = new SequentialList<>();
    private final HashMap<K, LinkedNode<Map.Entry<K, V>>> cache = new HashMap<>();
    private final int capacity;

    public LRUCache(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must be non-negative.");
        }
        this.capacity = capacity;
    }

    private void assertSizeEqual() {
        assert cache.size() == orderedValues.size()
            : "Map and list must contain the same number of items";
    }

    @Override
    public V get(K key) {
        LinkedNode<Map.Entry<K, V>> holder = cache.get(key);
        if (holder != null) {
            V value = holder.getValue().getValue();
            orderedValues.remove(holder);
            cache.put(key, orderedValues.addFirst(new AbstractMap.SimpleEntry<>(key, value)));

            assertSizeEqual();

            return value;
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        if (capacity == 0) {
            return;
        }

        if (cache.containsKey(key)) {
            orderedValues.remove(cache.get(key));
        } else if (cache.size() == capacity) {
            cache.remove(orderedValues.removeLast().getValue().getKey());
        }
        cache.put(key, orderedValues.addFirst(new AbstractMap.SimpleEntry<>(key, value)));

        assertSizeEqual();
    }

    @Override
    public int size() {
        assertSizeEqual();
        return cache.size();
    }
}
