package dev.gresty.aoc2021;

import java.util.function.BiFunction;

import static dev.gresty.aoc2021.Utils.withIntArray;
import static java.lang.Math.abs;
import static java.util.Arrays.stream;

public class Day07 {

    public static void main(String[] args) {
        withIntArray(Day07::part1, "day07");
        withIntArray(Day07::part2, "day07");
    }

    public static int part1(int[] crabs) {
        MedianFinder finder = new MedianFinder();
        stream(crabs).forEach(finder::submit);
        int median = finder.median();

        Optimizer optimizer = new Optimizer(median, crabs, Day07::fuelPart1);
        return optimizer.optimize();
    }

    public static int part2(int[] crabs) {
        int mean = stream(crabs).sum() / crabs.length;

        Optimizer optimizer = new Optimizer(mean, crabs, Day07::fuelPart2);
        return optimizer.optimize();
    }

    static class Optimizer {
        final int[] crabs;
        final BiFunction<Integer, int[], Integer> fuelCalc;
        int minFuel;
        int bestLoc;

        Optimizer(int startLoc, int[] crabs, BiFunction<Integer, int[], Integer> fuelCalc) {
            this.crabs = crabs;
            this.fuelCalc = fuelCalc;
            this.bestLoc = startLoc;
            this.minFuel = fuelCalc.apply(startLoc, crabs);
            report();
        }

        void report() {
            System.out.println("Centroid: " + bestLoc + ", Fuel: " + minFuel);
        }

        int optimize() {
            if (!search(1)) search(-1);
            return minFuel;
        }

        boolean search(int direction) {
            boolean improved = false;
            int newLoc = bestLoc + direction;

            while(true) {
                int newFuel = fuelCalc.apply(newLoc, crabs);
                if (newFuel < minFuel) {
                    minFuel = newFuel;
                    bestLoc = newLoc;
                    newLoc += direction;
                    improved = true;
                    report();
                } else {
                    return improved;
                }
            }
        }
    }

    static int fuelPart1(int centroid, int[] crabs) {
        return stream(crabs).map(c -> abs(c - centroid)).sum();
    }

    static int fuelPart2(int centroid, int[] crabs) {
        return stream(crabs).map(c -> triangle(abs(c - centroid))).sum();
    }

    static int triangle(int n) {
        return n * (n + 1) / 2;
    }

    static class MedianFinder {
        Heap minHeap = Heap.minHeap(1000);
        Heap maxHeap = Heap.maxHeap(1000);

        void submit(int value) {
            if (minHeap.size() == maxHeap.size()) {
                maxHeap.insert(value);
                minHeap.insert(maxHeap.remove());
            } else {
                minHeap.insert(value);
                maxHeap.insert(minHeap.remove());
            }
        }

        int median() {
            if (minHeap.size() > maxHeap.size()) {
                return minHeap.root();
            } else {
                return (minHeap.root() + maxHeap.root()) / 2;
            }
        }
    }
}
