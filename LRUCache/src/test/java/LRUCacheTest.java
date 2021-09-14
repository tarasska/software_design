import org.junit.Assert;
import org.junit.Test;

public class LRUCacheTest {
    @Test
    public void cacheAsMapTest() {
        final int capacity = 4;
        Cache<String, String> cache = new LRUCache<>(capacity);
        for (int i = 0; i < capacity; i++) {
            cache.put("First Name " + i, "Last Name " + i);
        }
        Assert.assertEquals(capacity, cache.size());
        for (int i = 0; i < capacity; i++) {
            Assert.assertEquals("Last Name " + i, cache.get("First Name " + i));
        }
    }
    @Test
    public void emptyCacheTest() {
        final int capacity = 0;
        Cache<Integer, String> cache = new LRUCache<>(capacity);

        cache.put(1, "Taras");
        Assert.assertEquals(0, cache.size());
        Assert.assertNull(cache.get(1));

        cache.put(2, "Kate");
        Assert.assertEquals(0, cache.size());
        Assert.assertNull(cache.get(2));
    }
}
