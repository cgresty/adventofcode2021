package dev.gresty.aoc2021;

public class Heap {

    final int capacity;
    final int[] store;
    final int rule;
    int size;

    private Heap(int capacity, int rule) {
        store = new int[capacity];
        this.capacity = capacity;
        this.rule = rule;
    }

    public static Heap minHeap(int capacity) {
        return new Heap(capacity, -1);
    }

    public static Heap maxHeap(int capacity) {
        return new Heap(capacity, 1);
    }

    public int size() {
        return size;
    }

    public void insert(int value) {
        if (size >= capacity) {
            throw new IndexOutOfBoundsException("Heap size exceeded");
        }
        int index = size;
        store[size++] = value;
        while(applyRule(store[index], store[parent(index)])) {
            swap(index, parent(index));
            index = parent(index);
        }
    }

    public int root() {
        return store[0];
    }

    public int remove() {
        int value = root();
        store[0] = store[--size];
        heapify(0);
        return value;
    }

    private int rightChild(int index) {
        return 2 * index + 2;
    }

    private int leftChild(int index) {
        return 2 * index + 1;
    }

    private int parent(int index) {
        return (index - 1) / 2;
    }

    private boolean leaf(int index) {
        return index >= size / 2 && index < size;
    }

    private void swap(int index1, int index2) {
        int value1 = store[index1];
        store[index1] = store[index2];
        store[index2] = value1;
    }

    private boolean applyRule(int value1, int value2) {
        return Integer.compare(value1, value2) == rule;
    }

    private void heapify(int index) {
        if (leaf(index)) return;
        int left = leftChild(index);
        int right = rightChild(index);
        if (applyRule(store[left], store[index]) || applyRule(store[right], store[index])) {
            if (applyRule(store[left], store[right])) {
                swap(index, left);
                heapify(left);
            } else {
                swap(index, right);
                heapify(right);
            }
        }
    }


}
