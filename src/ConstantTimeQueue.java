import java.util.HashMap;
import java.util.Map;

/**
 * A queue implementation that has constant time operations for enqueue, dequeue, and remove
 * This is achieved by keeping a HashMap of the values that are stored in the queue mapped to their Node's parent Node
 * (Having access to the parent's Node allows you to remove the node in question by doing node.next = node.next.next)
 * The HashMap is to a Node's parent versus the Node itself because I did not want to implement a doubly linked list
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
        T data;
        /**
         * The next node in the queue
         */
        Node next = null;

        /**
         * Constructs a Node with the given data
         *
         * @param data the data to construct a Node with
         */
        Node(T data) {
            this.data = data;
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
     * A map of each key to its associated Node's parent's Node
     * This is solely used to keep the remove method in constant time
     * For an explanation of why this was chosen, see the class javadoc
     */
    private Map<T, Node> parentsToKey;

    /**
     * Constructs an empty ConstantTimeQueue
     */
    public ConstantTimeQueue() {
        head = tail = null;
        size = 0;
        parentsToKey = new HashMap<>();
    }

    /**
     * Enqueues an object onto the queue
     *
     * @param data the data to enqueue
     */
    public void enqueue(T data) {
        if (head == null) head = tail = new Node(data); // first element enqueue (queue is currently empty)
        else if (head == tail) { // second element enqueue (queue currently contains just 1 item)
            parentsToKey.put(data, head); // add the parent to the data's Node to the map
            head.next = tail = new Node(data); // add the Node to the queue
        } else { // past second element enqueue (queue currently contains more than 1 item)
            parentsToKey.put(data, tail); // add the parent to the data's Node to the map
            tail.next = new Node(data); // add an element to the end
            tail = tail.next; // move tail to the end
        }
        size++;
    }

    /**
     * Removes an object from any position in the queue in constant time
     *
     * @param data the data to remove from the queue
     */
    public void remove(T data) {
        if (head == null) return;
        if (head.data.equals(data)) dequeue(); // simply remove the head because that is what we want to remove
        else {
            if (!parentsToKey.containsKey(data)) return; // data not found in queue, so abort
            size--;
            final Node pointer = parentsToKey.remove(data); // a reference the parent of the Node we want to remove
            pointer.next = pointer.next.next; // skip over the node we want to remove, effectively removing it
            if (pointer.next == null) tail = pointer; // reset the tail if it was what we just removed
        }
    }

    /**
     * Dequeues the head from the queue
     *
     * @return the data that was dequeued
     */
    public T dequeue() {
        if (head == null) return null;
        size--;
        final T returnVal = head.data;
        parentsToKey.remove(returnVal); // remove the head's key from the map (if it is in there, which it may not)
        head = head.next; // skip over the head, effectively removing it
        if (head == null) tail = null; // queue is empty so reset the tail
        return returnVal;
    }

    /**
     * Returns the number of elements in the queue at any given time
     *
     * @return the size of the queue
     */
    public int size() {
        return size;
    }

    /**
     * Returns the data at the head of the queue
     *
     * @return the data at the head of the queue
     */
    public T getFirst() {
        if (head == null) return null;
        return head.data;
    }

    /**
     * Returns the data at the tail of the queue
     *
     * @return the data at the tail of the queue
     */
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
