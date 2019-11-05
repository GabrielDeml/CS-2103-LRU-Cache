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

    // todo test remove()
}
