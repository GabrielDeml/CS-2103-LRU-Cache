import static org.junit.Assert.*;

import org.junit.Test;

/**
 * A class to test the ConstantTimeQueue
 */
public class QueueTest {
    /**
     * Tests the enqueue functionality
     */
    @Test
    public void testEnqueue() {
        ConstantTimeQueue<Character> queue = new ConstantTimeQueue<>();
        for (char c = 'a'; c <= 'e'; ++c) queue.enqueue(c);
        assertEquals("a, b, c, d, e", queue.toString());
    }

    /**
     * Tests the dequeue functionality
     */
    @Test
    public void testDequeue() {
        // Check when head is empty
        ConstantTimeQueue<Integer> keys = new ConstantTimeQueue<>();
        assertNull(keys.dequeue());

        // Check for a variable amount of objects (from 1 to 10)
        for (int i = 1; i < 10; ++i) {
            putObjectsInQueue(i, keys);
            for (int j = 0; j < i; ++j) {
                assertEquals(j, (int) keys.getFirst());
                assertEquals(j, (int) keys.dequeue());
            }
            assertNull(keys.getFirst());
        }
    }

    /**
     * Tests the remove functionality
     */
    @Test
    public void testRemove() {
        ConstantTimeQueue<Integer> keys = new ConstantTimeQueue<>();

        // Make sure we can get rid of everything
        putObjectsInQueue(10, keys);
        for (int i = 0; i < 10; i++) {
            keys.remove(i);
        }
        assertNull(keys.getFirst());

        // Get rid of the first half
        putObjectsInQueue(10, keys);
        for (int i = 0; i < 5; i++) {
            keys.remove(i);
        }
        assertEquals((int) keys.getFirst(), 5);

        // Get rid of the second half
        keys = new ConstantTimeQueue<>();
        putObjectsInQueue(10, keys);
        for (int i = 10; i > 5; i--) {
            keys.remove(i);
        }
        assertEquals((int) keys.getLast(), 5);

        // Try to remove stuff out of bounds
        keys = new ConstantTimeQueue<>();
        for (int i = 10; i > 5; i--) {
            keys.remove(i);
        }
        assertNull(keys.getLast());
    }

    /**
     * A helper method that fills up a queue with varying integers
     *
     * @param n the upper bound of integers to add to the queue
     * @param q the queue to fill with integers
     */
    private void putObjectsInQueue(int n, ConstantTimeQueue<Integer> q) {
        for (int i = 0; i < n; i++) q.enqueue(i);
    }
}
