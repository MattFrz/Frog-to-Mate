/**
 * Implementation of a priority queue using arrays where elements with equal priority are unique.
 * @author Matt Farzaneh
 * @param <T> The type of elements stored in the priority queue.
 */
public class ArrayUniquePriorityQueue<T> implements UniquePriorityQueueADT<T> {
    
    private T[] queue;
    private double[] priority;
    private int count;
    
    /**
     * Constructor to initialize the queue with a default capacity.
     */
    @SuppressWarnings("unchecked")
    public ArrayUniquePriorityQueue() {
        queue = (T[]) new Object[10];
        priority = new double[10];
        count = 0;
    }
    
    /**
     * Add an element with a given priority to the queue.
     * @param data The element to add.
     * @param prio The priority of the element.
     */
    public void add(T data, double prio) {
        if (contains(data)) {
            return;
        }
        
        if (count == queue.length) {
            expandCapacity();
        }
        
        int index = count;
        
        // Find the correct position to insert based on priority
        while (index > 0 && priority[index - 1] > prio) {
            queue[index] = queue[index - 1];
            priority[index] = priority[index - 1];
            index--;
        }
        
        // Insert new item
        queue[index] = data;
        priority[index] = prio;
        
        count++;
    }
    
    /**
     * Expand the capacity of the queue when needed.
     */
    private void expandCapacity() {
        T[] newQueue = (T[]) new Object[queue.length + 5];
        double[] newPriority = new double[priority.length + 5];
        for (int i = 0; i < queue.length; i++) {
            newQueue[i] = queue[i];
            newPriority[i] = priority[i];
        }
        queue = newQueue;
        priority = newPriority;
    }
    
    /**
     * Check if the queue contains a given element.
     * @param data The element to check for.
     * @return True if the element exists in the queue, otherwise false.
     */
    public boolean contains(T data) {
        for (int i = 0; i < count; i++) {
            if (queue[i].equals(data)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get the element with the minimum priority without removing it from the queue.
     * @return The element with the minimum priority.
     * @throws CollectionException If the queue is empty.
     */
    public T peek() throws CollectionException {
        if (isEmpty()) {
            throw new CollectionException("PQ is empty");
        }
        return queue[0];
    }
    
    /**
     * Remove and return the element with the minimum priority from the queue.
     * @return The element with the minimum priority.
     * @throws CollectionException If the queue is empty.
     */
    public T removeMin() throws CollectionException {
        if (isEmpty()) {
            throw new CollectionException("PQ is empty");
        }
        
        T minData = queue[0];
        // Shift elements to remove the minimum element
        for (int i = 0; i < count - 1; i++) {
            queue[i] = queue[i + 1];
            priority[i] = priority[i + 1];
        }
        count--;
        return minData;
    }
    
    /**
     * Update the priority of a given element in the queue.
     * @param data The element whose priority needs to be updated.
     * @param newPrio The new priority of the element.
     * @throws CollectionException If the element is not found in the queue.
     */
    public void updatePriority(T data, double newPrio) throws CollectionException {
        if (!contains(data)) {
            throw new CollectionException("Item not found in PQ");
        }
        // Remove the element and add it again with the new priority
        T removedItem = null;
        double removedPriority = 0.0;
        for (int i = 0; i < count; i++) {
            if (queue[i].equals(data)) {
                removedItem = queue[i];
                removedPriority = priority[i];
                // Remove the item by shifting elements
                for (int j = i; j < count - 1; j++) {
                    queue[j] = queue[j + 1];
                    priority[j] = priority[j + 1];
                }
                count--;
                break;
            }
        }
        add(removedItem, newPrio);
    }
    
    /**
     * Check if the queue is empty.
     * @return True if the queue is empty, otherwise false.
     */
    public boolean isEmpty() {
        return count == 0;
    }
    
    /**
     * Get the number of elements in the queue.
     * @return The number of elements in the queue.
     */
    public int size() {
        return count;
    }
    
    /**
     * Get the current capacity of the queue.
     * @return The current capacity of the queue.
     */
    public int getLength() {
        return queue.length;
    }
    
    /**
     * Generate a string representation of the queue.
     * @return A string representation of the queue.
     */
    public String toString() {
        if (isEmpty()) {
            return "The PQ is empty";
        }
        String result = "";
        for (int i = 0; i < count; i++) {
            result += queue[i] + " [" + priority[i] + "]";
            if (i < count - 1) {
                result += ", ";
            }
        }
        return result;
    }
}