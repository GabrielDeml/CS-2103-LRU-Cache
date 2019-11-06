import static org.junit.Assert.*;

import org.junit.Test;

public class QueueTest {
    @Test
    public void testEnqueue() {
        ConstantTimeQueue<Character> queue = new ConstantTimeQueue<>();
        for (char c = 'a'; c <= 'e'; ++c) queue.enqueue(c);
        assertEquals("a, b, c, d, e", queue.toString());
    }

    // todo test dequeue
    @Test
    public void testDequeue() {
        // Check when head is empty
        ConstantTimeQueue<Integer> keys = new ConstantTimeQueue<>();
        assertNull(keys.dequeue());

        // Check when there is only one object
        keys = new ConstantTimeQueue<>();
        keys.enqueue(0);
        keys.dequeue();
        assertNull(keys.getFirst());

        // Check when there is two objects
        keys = new ConstantTimeQueue<>();
//        keys.enqueue(0);
//        keys.enqueue(1);
        putObjectsInQueue(2, keys);
        int dequeued = keys.dequeue();
        assertEquals((int) keys.getFirst(), 1);
        assertEquals(dequeued, 0);
    }

    @Test
    public void testRemove() {
        ConstantTimeQueue<Integer> keys = new ConstantTimeQueue<>();

        // Make sure we can get rid of everything
        keys = new ConstantTimeQueue<>();
        putObjectsInQueue(10, keys);
        for (int i = 0; i < 10; i++) {
            keys.remove(i);
        }
        assertNull(keys.getFirst());

        // Get rid of the first half
        keys = new ConstantTimeQueue<>();
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

    private void putObjectsInQueue(int n, ConstantTimeQueue<Integer> q) {
        for (int i = 0; i < n; i++) {
            q.enqueue(i);
        }
    }
}
