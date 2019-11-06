import java.util.HashMap;
import java.util.Map;

/**
 * A queue implementation that has constant time operations for enqueue, dequeue, and remove
 * This is achieved by keeping a HashMap of the values that are stored in the queue mapped to their corresponding Node
 * (Having access to the Node associated with the key allows you to do node.previous.next = node.previous.next.next)
 *
 * @param <T> The type of data this ConstantTimeQueue should store
 */
public class ConstantTimeQueue<T> {
    /**
     * A node of the queue
     */
    private class Node {
        /**
         * The data the node stores
         */
        private T data;
        /**
         * The next and previous nodes in the queue
         */
        private Node next, previous;

        /**
         * Constructs a Node with the given data
         *
         * @param data the data to construct a Node with
         */
        private Node(T data) {
            this.data = data;
            next = previous = null;
        }

        /**
         * Constructs a Node with the given data that points to the next and previous nodes
         *
         * @param data     the data to construct a Node with
         * @param previous the previous Node in the queue (can be null)
         */
        private Node(T data, Node previous) {
            this.data = data;
            this.next = null;
            this.previous = previous;
        }
    }

    /**
     * The two ends of the queue, the head and tail
     * Head is needed as a starting point and also to keep dequeue in constant time
     * Tail is needed to keep enqueue in constant time
     */
    private Node head, tail;
    /**
     * The number of nodes in the queue at any given time
     */
    private int size;
    /**
     * A map of each key to its associated Node
     * This is solely used to keep the remove method in constant time
     * For an explanation of why this was chosen, see the class' javadoc
     */
    private Map<T, Node> nodeCache;

    /**
     * Constructs an empty ConstantTimeQueue
     */
    @SuppressWarnings("WeakerAccess")
    public ConstantTimeQueue() {
        head = tail = null;
        size = 0;
        nodeCache = new HashMap<>();
    }

    /**
     * Enqueues an object onto the queue
     *
     * @param data the data to enqueue
     */
    @SuppressWarnings("WeakerAccess")
    public void enqueue(T data) {
        if (head == null) head = tail = new Node(data); // first element enqueue (queue is currently empty)
        else if (head == tail) { // second element enqueue (queue currently contains just 1 item)
            head.next = tail = new Node(data, head); // add the Node to the queue
        } else { // past second element enqueue (queue currently contains more than 1 item)
            tail.next = new Node(data, tail); // add an element to the end
            tail = tail.next; // move tail to the end
        }
        nodeCache.put(data, tail); // add the most recently added node to the nodeCache
        size++;
    }

    /**
     * Removes an object from any position in the queue in constant time
     *
     * @param data the data to remove from the queue
     */
    @SuppressWarnings("WeakerAccess")
    public void remove(T data) {
        if (head == null) return;
        if (head.data.equals(data)) dequeue(); // simply remove the head because that is what we want to remove
        else {
            if (!nodeCache.containsKey(data)) return; // data not found in queue, so abort
            size--;
            final Node previous = nodeCache.remove(data).previous; // the parent to the node we want to remove
            previous.next = previous.next.next; // skip over the node we want to remove, effectively removing it
            if (previous.next == null) tail = previous; // reset the tail if it was what we just removed
            else previous.next.previous = previous; // fix the dependency
        }
    }

    /**
     * Dequeues the head from the queue
     *
     * @return the data that was dequeued
     */
    @SuppressWarnings("WeakerAccess")
    public T dequeue() {
        if (head == null) return null;
        size--;
        final T returnVal = head.data;
        nodeCache.remove(returnVal); // remove the head's key to head mapping from the nodeCache
        head = head.next; // skip over the head, effectively removing it
        if (head == null) tail = null; // queue is empty so reset the tail
        else head.previous = null;
        return returnVal;
    }

    /**
     * Returns the number of elements in the queue at any given time
     *
     * @return the size of the queue
     */
    @SuppressWarnings("WeakerAccess")
    public int size() {
        return size;
    }

    /**
     * Returns the data at the head of the queue
     *
     * @return the data at the head of the queue
     */
    @SuppressWarnings("WeakerAccess")
    public T getFirst() {
        if (head == null) return null;
        return head.data;
    }

    /**
     * Returns the data at the tail of the queue
     *
     * @return the data at the tail of the queue
     */
    @SuppressWarnings("WeakerAccess")
    public T getLast() {
        if (tail == null) return null;
        return tail.data;
    }

    /**
     * Provides a string representation of the queue. Useful for debugging
     *
     * @return the queue represented as a String
     */
    @Override
    public String toString() {
        StringBuilder returnVal = new StringBuilder();
        for (Node pointer = head; pointer != null; pointer = pointer.next) {
            returnVal.append(pointer.data);
            if (pointer.next != null) returnVal.append(", ");
        }
        return returnVal.toString();
    }
}
