import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTest {
    /**
     * A simple test DataProvider that returns whatever is passed in as input as a string (via toString())
     */
    private class EchoProvider implements DataProvider<Integer, String> {
        private Integer lastCall;

        @Override
        public String get(Integer key) {
            this.lastCall = key;
            return key.toString();
        }

        public Integer getLastCall() {
            return this.lastCall;
        }

    }


    /**
     * Does a thorough test of the LRU algorithm used by the LRUCache
     */

    @Test
    public void leastRecentlyUsedIsCorrect() {
        DataProvider<Integer, String> provider = new EchoProvider(); // Need to instantiate an actual DataProvider
        for (int capacity = 0; capacity < 100; ++capacity) {
            Cache<Integer, String> cache = new LRUCache<>(provider, capacity);
            for (int i = 0; i < capacity * 2; ++i) {
                assertEquals(Integer.toString(i), cache.get(i));
                assertEquals(i + 1, cache.getNumMisses());
            }
            // At this point, the cache should contain capacity to (capacity * 2 - 1)
            for (int i = capacity; i < capacity * 2; ++i) {
                assertEquals(Integer.toString(i), cache.get(i));
                assertEquals(2 * capacity, cache.getNumMisses());
            }
            for (int i = 0; i < capacity; ++i) {
                assertEquals(Integer.toString(i), cache.get(i));
                assertEquals(2 * capacity + i + 1, cache.getNumMisses());
            }
            for (int i = 0; i < capacity; ++i) {
                assertEquals(Integer.toString(i), cache.get(i));
                assertEquals(3 * capacity, cache.getNumMisses());
            }
        }
    }

    /**
     * Checks to make sure that we don't go over the cache size.
     */
    @Test
    public void overFillCache() {
        DataProvider<Integer, String> provider = new EchoProvider();
        Cache<Integer, String> cache = new LRUCache<>(provider, 10);
        fillCache(11, cache);
        cache.get(0);
        assertTrue(0 == ((EchoProvider) provider).getLastCall());
    }

    /**
     * Check to make sure the cache actually stores stuff.
     */
    @Test
    public void checkThatTheCacheCachesThings() {
        // Just picked a reasonable number it really can be any int bigger than zero
        int numOfElements = 4;
        DataProvider<Integer, String> provider = new EchoProvider();
        Cache<Integer, String> cache = new LRUCache<>(provider, numOfElements);
        fillCache(numOfElements, cache);
        for (int i = 0; i < numOfElements - 1; i++) {
//            System.out.println(i);
            cache.get(i);
//            System.out.println(((EchoProvider) provider).getLastCall());
            assertTrue(((EchoProvider) provider).getLastCall() != i);
        }
    }

    /**
     * Checks when cache size is 0 we always get things from the provider
     */
    @Test
    public void checkWhenCacheSizeIsZero() {
        int numOfElements = 10;
        DataProvider<Integer, String> provider = new EchoProvider();
        Cache<Integer, String> cache = new LRUCache<>(provider, 0);
        for (int i = 0; i < numOfElements; i++) {
            cache.get(i);
        }
        assertEquals(cache.getNumMisses(), 10);
    }


    /**
     * fills cache with n. If we get 3 as n we will fill the cache with 0, 1, 2
     *
     * @param n     the number of things to fill the cache with
     * @param cache cache that to add to
     */
    private void fillCache(int n, Cache<Integer, String> cache) {
        for (int i = 0; i < n; i++) {
            cache.get(i);
        }
    }
}


