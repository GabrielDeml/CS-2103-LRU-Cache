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
		@Override
		public String get(Integer key) {
			return key.toString();
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
}
