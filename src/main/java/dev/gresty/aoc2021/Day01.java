package dev.gresty.aoc2021;

import java.util.stream.IntStream;

import static dev.gresty.aoc2021.Utils.runInt;
import static java.util.stream.IntStream.range;

public class Day01 {

    public static void main(String[] args) {
        runInt(Day01::day01a, "day01.txt");
        runInt(Day01::day01b, "day01.txt");
    }

    static int day01a(IntStream input) {
        Counter counter = new Counter(1);
        input.forEach(counter::submit);
        return counter.count;
    }

    static int day01b(IntStream input) {
        Counter counter = new Counter(3);
        input.forEach(counter::submit);
        return counter.count;
    }

    private static class Counter {
        final int size;
        final int[] w;
        int ok;
        int count = 0;

        Counter(int windowSize) {
            size = windowSize;
            w = new int[size + 1];
            ok = -size;
        }

        // To compare A+B+C to B+C+D, only need to compare A to D.
        void submit(int value) {
            w[size] = value;
            if (++ok > 0 && w[size] > w[0]) count++;
            range(0, size).forEach(i -> w[i] = w[i + 1]);
        }
    }
}
