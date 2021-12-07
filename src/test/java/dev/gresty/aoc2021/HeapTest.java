package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HeapTest {

    @Test
    public void minHeap() {
        Heap heap = Heap.minHeap(10);
        heap.insert(5);
        heap.insert(3);
        heap.insert(17);
        heap.insert(10);
        heap.insert(84);
        heap.insert(19);
        heap.insert(6);
        heap.insert(22);
        heap.insert(9);

        assertThat(heap.root()).isEqualTo(3);
    }

    @Test
    public void maxHeap() {
        Heap heap = Heap.maxHeap(10);
        heap.insert(5);
        heap.insert(3);
        heap.insert(17);
        heap.insert(10);
        heap.insert(84);
        heap.insert(19);
        heap.insert(6);
        heap.insert(22);
        heap.insert(9);

        assertThat(heap.root()).isEqualTo(84);
    }
}