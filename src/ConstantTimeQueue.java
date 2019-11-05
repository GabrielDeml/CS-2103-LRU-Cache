
public class ConstantTimeQueue<T> {
    private class Node {
        T data;
        Node next = null;

        Node(T data) {
            this.data = data;
        }
    }

    private Node head, tail;
    private int size;

    public ConstantTimeQueue() {
        head = tail = null;
        size = 0;
    }

    public void enqueue(T data) {
        if (head == null) head = tail = new Node(data); // first element enqueue
        else if (head == tail) head.next = tail = new Node(data); // second element enqueue
        else { // past second element enqueue
            tail.next = new Node(data);
            tail = tail.next;
        }
    }

    public boolean remove(T data) {
        if (head == null) return false;
        if (head.data.equals(data)) {
            head = head.next;
            if (head == null) tail = null;
            return true;
        }
        for (Node pointer = head; pointer.next != null; pointer = pointer.next) {
            if (pointer.next.data.equals(data)) {
                if (pointer.next == tail) tail = pointer;
                pointer.next = pointer.next.next;
                return true;
            }
        }
        return false;
        // this is the O(n) method but is required for makeMostRecentTransaction
    }

    public T dequeue() {
        if (head == null) return null;
        final T returnVal = head.data;
        head = head.next;
        if (head == null) tail = null;
        return returnVal;
    }

    public int size() {
        return size;
    }

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
