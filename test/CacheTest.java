import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTest {
    /**
     * A simple test DataProvider that returns whatever is passed in as input as a string (via toString())
     */
    private static class EchoProvider implements DataProvider<Integer, String> {
        private Integer lastCall;

        @Override
        public String get(Integer key) {
            this.lastCall = key;
            return key.toString();
        }

        Integer getLastCall() {
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
     * Checks to make sure that we don't go over the cache size
     */
    @Test
    public void overFillCache() {
        EchoProvider provider = new EchoProvider();
        final int capacity = 10;
        Cache<Integer, String> cache = new LRUCache<>(provider, capacity);
        fillCache(capacity + 1, cache);
        cache.get(0);
        assertEquals(0, (int) provider.getLastCall());
        assertEquals(capacity + 2, cache.getNumMisses());
    }

    /**
     * Check to make sure the cache actually stores stuff
     */
    @Test
    public void checkThatTheCacheCachesThings() {
        // Just picked a reasonable number it really can be any int bigger than zero
        final int numOfElements = 4;
        EchoProvider provider = new EchoProvider();
        Cache<Integer, String> cache = new LRUCache<>(provider, numOfElements);
        fillCache(numOfElements, cache);
        for (int i = 0; i < numOfElements - 1; i++) {
            cache.get(i);
            assertEquals(numOfElements - 1, (int) provider.getLastCall());
        }
    }

    /**
     * Checks when cache size is 0 we always get things from the provider
     */
    @Test
    public void checkWhenCacheSizeIsZero() {
        final int numOfElements = 10;
        DataProvider<Integer, String> provider = new EchoProvider();
        Cache<Integer, String> cache = new LRUCache<>(provider, 0);
        for (int i = 0; i < numOfElements; i++) cache.get(i);
        assertEquals(10, cache.getNumMisses());
    }

    /**
     * Check if we run in linear time just throw a warning if not
     */
    @Test
    public void checkLinearTime() {
        int smallCacheSize = 100;
        int mediumCacheSize = 10000;
        int largeCacheSize =  10000000;

        long smallTime = checkHowLongItTakes(smallCacheSize);
        long mediumTime = checkHowLongItTakes(mediumCacheSize);
        long longTime = checkHowLongItTakes(largeCacheSize);
        System.out.println("small time : " + smallTime + " medium time : " + mediumTime + " long time : " + longTime);
        System.out.println("small time : " + smallTime / smallCacheSize + " medium time : " + longTime / mediumCacheSize + " long time : " + longTime / largeCacheSize);
        // Find the diff in times
        long diffOfAverageTimeTaken = Math.abs((smallTime / smallCacheSize) - (longTime / largeCacheSize));


        if (diffOfAverageTimeTaken < 2) {
            System.out.println("We think this is running in linear time");
        } else System.out.println("This does not seem to be linear time");
    }

    private long checkHowLongItTakes(int numOfElements) {
        // Set up the large cache and fill it up
        DataProvider<Integer, String> provider = new EchoProvider();
        Cache<Integer, String> cache = new LRUCache<>(provider, numOfElements);
        fillCache(numOfElements, cache);

        // Test how long small cache size takes to run
        long StartTime = System.currentTimeMillis();
//        System.out.println(smallStartTime);
        for (int i = 0; i < numOfElements; i++) {
            if (System.currentTimeMillis() - StartTime > 3000) {
                System.out.println("Took took too long to run. We don't know if it runs int linear time or not. Quiting!!!");
                System.exit(0);
            }
            cache.get(i);
        }
        long StopTime = System.currentTimeMillis();
        return StopTime - StartTime;
    }

    /**
     * Fills the supplied cache with n elements, from 0 to n-1 (assuming its capacity is high enough)
     * Example: if n is 3, the cache's .get() will be called with 0, 1, & 2
     *
     * @param n     the number of integers to fill the cache with
     * @param cache the cache to fill
     */
    private void fillCache(int n, Cache<Integer, String> cache) {
        for (int i = 0; i < n; i++) cache.get(i);
    }
}


