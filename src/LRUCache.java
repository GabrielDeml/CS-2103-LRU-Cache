import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U> {
    /**
     * The DataProvider supplying this cache with data
     */
    private final DataProvider<T, U> provider;
    /**
     * The capacity (number of items) of this cache
     */
    private final int capacity;
    /**
     * Acts as a queue of the keys. Oldest keys are at the head and should be removed first.
     */
    private final Queue<T> keys;
    /**
     * The actual cache of the data from the DataProvider
     */
    private final Map<T, U> cache;
    /**
     * The number of misses so far
     */
    private int numMisses = 0;

    /**
     * @param provider the data provider to consult for a cache miss
     * @param capacity the exact number of (key,value) pairs to store in the cache
     */
    public LRUCache(DataProvider<T, U> provider, int capacity) {
        keys = new LinkedList<>();
        // "If the initial capacity is greater than the maximum number of entries divided by the load factor,
        //   no rehash operations will ever occur."
        // From Java 8 Documentation: https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
        // Thus, to prevent rehashing (and increase performance of the cache), we use ceil(capacity/defaultLoadFactor)
        final double defaultLoadFactor = 0.75; // From the java 8 documentation for HashMap)
        // "Constructs an empty HashMap with the specified initial capacity and the default load factor (0.75)."
        cache = new HashMap<>((int) Math.ceil(capacity / defaultLoadFactor));
        this.provider = provider;
        this.capacity = capacity;
    }

    /**
     * Returns the value associated with the specified key.
     *
     * @param key the key
     * @return the value associated with the key
     */
    public U get(T key) {
        if (cache.containsKey(key)) {
            makeMostRecentTransaction(key);
            return cache.get(key);
        } else {
            numMisses++;
            if (capacity <= 0) return provider.get(key);
            if (keys.size() >= capacity) removeOldest();
            final U value = provider.get(key);
            addToCache(key, value);
            return value;
        }
    }

    /**
     * Removes the oldest item in the cache
     */
    private void removeOldest() {
        cache.remove(keys.poll());
    }

    /**
     * Adds an item to the cache
     *
     * @param key   the key of the value to add to the cache
     * @param value the value to add to the cache
     */
    private void addToCache(T key, U value) {
        keys.add(key);
        cache.put(key, value);
    }

    /**
     * Makes the key the most recent transaction (used in the LRU eviction strategy)
     *
     * @param key the key to move to the end of the cache's queue
     */
    private void makeMostRecentTransaction(T key) {
        keys.remove(key);
        keys.add(key);
    }

    /**
     * Returns the number of cache misses since the object's instantiation.
     *
     * @return the number of cache misses since the object's instantiation.
     */
    public int getNumMisses() {
        return numMisses;
    }
}
