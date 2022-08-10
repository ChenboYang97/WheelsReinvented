package practices.Design;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class MyHeap<E> {

    // private member variables
    private int size;
    private Object[] array;
    private final Comparator<E> comparator;
    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    // Constructors
    public MyHeap() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }
    public MyHeap(int capacity) {
        this(capacity, null);
    }
    public MyHeap(Comparator<E> comparator) {
        this(DEFAULT_INITIAL_CAPACITY, comparator);
    }
    public MyHeap(int capacity, Comparator<E> comparator) {
        if (capacity <= 0) throw new IllegalArgumentException("The capacity cannot less than or equal to zero");
        this.size = 0;
        array = new Object[capacity];
        this.comparator = comparator;
    }
    public MyHeap(E[] array) {
        this(array, null);
    }
    public MyHeap(E[] array, Comparator<E> comparator) {
        if (array == null || array.length == 0) throw new IllegalArgumentException("The input array cannot be null or empty");
        this.size = array.length;
        this.comparator = comparator;
        this.array = Arrays.copyOf(array, (int)(array.length * 1.5));
        heapify();
    }

    // public methods
    public void offer(E value) {
        if (isFull()) expand();
        array[size] = value;
        size++;
        if (comparator == null) {
            percolateUpComparable(size - 1);
        } else {
            percolateUpByComparator(size - 1);
        }
    }
    public E poll() {
        if (isEmpty()) throw new NoSuchElementException("The current heap is empty");
        Object result = array[0];
        array[0] = array[size - 1];
        size--;
        if (comparator == null) {
            percolateDownComparable(0);
        } else {
            percolateDownByComparator(0);
        }
        return (E) result;
    }
    public void update(E oldValue, E newValue) {
        Integer index = getIndex(oldValue);
        if (index == null) throw new NoSuchElementException("The old value doesn't exist in the Heap");
        updateByIndex(index, oldValue, newValue);
    }
    public E peek() {
        if (isEmpty()) throw new NoSuchElementException("The current heap is empty");
        return (E) array[0];
    }
    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return size == 0;
    }


    // private methods
    private void heapify() {
        // start percolateDown from the middle of the array
        int index = (size - 2) / 2;
        while (index >= 0) {
            if (comparator == null) {
                percolateDownComparable(index--);
            } else {
                percolateDownByComparator(index--);
            }
        }
    }
    private boolean isFull() {
        return array.length == size;
    }
    private void expand() {
        array = Arrays.copyOf(array, (int)(array.length * 1.5));
    }
    private void percolateUpByComparator(int index) {
        while (index > 0) {
            int parent = (index - 1) >>> 1;
            if (comparator.compare((E) array[parent], (E) array[index]) <= 0) return;
            else {
                swap(parent, index);
                index = parent;
            }
        }
    }
    private void percolateUpComparable(int index) {
        Comparable<E> key = (Comparable<E>) array[index];
        while (index > 0) {
            int parent = (index - 1) >>> 1;
            if (key.compareTo((E) array[parent]) >= 0) return;
            else {
                swap(parent, index);
                index = parent;
            }
        }
    }
    private void percolateDownByComparator(int index) {
        int half = (size >>> 1) - 1;
        while (index <= half) {
            int left = (index << 2) + 1;
            int right = (index << 2) + 2;

            // pick the smallest candidate
            int candidate;
            if (right >= size || comparator.compare((E) array[left], (E) array[right]) <= 0) {
                candidate = left;
            } else {
                candidate = right;
            }

            // compare candidate with the current index
            if (comparator.compare((E) array[candidate], (E) array[index]) >= 0) return;
            else {
                swap(candidate, index);
                index = candidate;
            }
        }
    }
    private void percolateDownComparable(int index) {
        Comparable<E> key = (Comparable<E>) array[index];
        int half = (size >>> 1) - 1;
        while (index <= half) {
            int left = (index << 1) + 1;
            int right = (index << 1) + 2;

            // pick the smallest candidate
            int candidate;
            if (right >= size || ((Comparable<E>) array[left]).compareTo((E) array[right]) <= 0) {
                candidate = left;
            } else {
                candidate = right;
            }

            // compare candidate with the current index
            if (key.compareTo((E) array[candidate]) <= 0) return;
            else {
                swap(candidate, index);
                index = candidate;
            }
        }
    }
    private Integer getIndex(E value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) return i;
        }
        return null;
    }
    private void swap(int left, int right) {
        Object temp = array[left];
        array[left] = array[right];
        array[right] = temp;
    }
    private void updateByIndex(int index, E oldValue, E newValue) {
        array[index] = newValue;
        if (comparator == null) {
            Comparable<E> key = (Comparable<E>) array[index];
            if (key.compareTo(oldValue) > 0) {
                percolateDownComparable(index);
            } else {
                percolateUpComparable(index);
            }
        } else {
            if (comparator.compare(newValue, oldValue) > 0) {
                percolateDownByComparator(index);
            } else {
                percolateUpByComparator(index);
            }
        }
    }
}


