import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

public class LRUCacheTest {
    private static class TestKey {
        Integer key;

        TestKey(Integer key) {
            Objects.requireNonNull(key);
            this.key = key;
        }

        @Override
        public int hashCode() {
            return key;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TestKey) {
                return key.equals(((TestKey) obj).key);
            }
            return false;
        }
    }

    private String testStrById(int id) {
        return "Value" + id;
    }

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

    @Test(expected = IllegalArgumentException.class)
    public void negativeCapacityTest() {
        Cache<Integer, String> cache = new LRUCache<>(-13);
    }

    public void displacingElementTest(final int capacity, final int overflowCnt) {
        Cache<TestKey, String> cache = new LRUCache<>(capacity);
        for (int i = 0; i < capacity + overflowCnt; i++) {
            cache.put(new TestKey(i), testStrById(i));
        }
        Assert.assertEquals(capacity, cache.size());
        for (int i = 0; i < overflowCnt; i++) {
            Assert.assertNull(cache.get(new TestKey(i)));
        }
        for (int i = overflowCnt; i < capacity + overflowCnt; i++) {
            Assert.assertEquals(testStrById(i), cache.get(new TestKey(i)));
        }
    }

    @Test
    public void displacingLastElementTest() {
        displacingElementTest(10, 1);
    }

    @Test
    public void displacingAllElementsSmallTest() {
        displacingElementTest(13, 13);
    }

    @Test
    public void displacingAllElementsLargeTest() {
        displacingElementTest(10000, 20000);
    }

    @Test
    public void refreshElementTest() {
        final int capacity = 100;
        Cache<Integer, String> cache = new LRUCache<>(capacity);
        for (int i = 0; i < capacity; i++) {
            cache.put(i, testStrById(i));
        }
        Assert.assertEquals(capacity, cache.size());
        for (int i = 0; i < capacity; i += 2) {
            Assert.assertEquals(testStrById(i), cache.get(i));
        }
        for (int i = capacity; i < capacity + capacity / 2; i++) {
            cache.put(i, testStrById(i));
        }
        for (int i = 0; i < capacity; i++) {
            if (i % 2 == 0) {
                Assert.assertEquals(testStrById(i), cache.get(i));
            } else {
                Assert.assertNull(cache.get(i));
            }
        }
        for (int i = capacity; i < capacity + capacity / 2; i++) {
            Assert.assertEquals(testStrById(i), cache.get(i));
        }
        Assert.assertEquals(capacity, cache.size());
    }

    @Test
    public void refreshExistingElementsTest() {
        final int capacity = 100;
        Cache<Integer, String> cache = new LRUCache<>(capacity);
        for (int i = 0; i < capacity; i++) {
            cache.put(i, testStrById(i));
        }
        for (int i = 0; i < capacity / 2; i++) {
            cache.put(i, testStrById(i));
        }
        Assert.assertEquals(capacity, cache.size());

        for (int i = capacity; i < capacity + capacity / 2; i++) {
            cache.put(i, testStrById(i));
        }
        for (int i = 0; i < capacity + capacity / 2; i++) {
            if (i < capacity / 2 || capacity <= i) {
                Assert.assertEquals(testStrById(i), cache.get(i));
            } else {
                Assert.assertNull(cache.get(i));
            }
        }
    }

    @Test
    public void getNonExistentElements() {
        final int capacity = 100;
        Cache<Integer, String> cache = new LRUCache<>(capacity);
        for (int i = 0; i < capacity; i++) {
            cache.put(i, testStrById(i));
        }

        for (int i = capacity; i < 2 * capacity; i++) {
            Assert.assertNull(cache.get(i));
        }
        Assert.assertEquals(capacity, cache.size());

        for (int i = 0; i < capacity; i++) {
            Assert.assertEquals(testStrById(i), cache.get(i));
        }

        // Checking correct order
        for (int i = 0; i < capacity; i++) {
            cache.put(i + capacity, testStrById(i + capacity));
            Assert.assertEquals(capacity, cache.size());
            Assert.assertNull(cache.get(i));
        }
    }
}
