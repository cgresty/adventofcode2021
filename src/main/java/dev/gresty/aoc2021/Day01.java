package dev.gresty.aoc2021;

import java.util.stream.IntStream;

import static dev.gresty.aoc2021.Utils.runInt;
import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;

public class Day01 {

    public static void main(String[] args) {
        runInt(Day01::day01a, "day01.txt");
        runInt(Day01::day01b, "day01.txt");
    }

    static int day01a(IntStream input) {
        WindowedCounter counter = new WindowedCounter(1);
        input.forEach(counter::submit);
        return counter.count;
    }

    static int day01b(IntStream input) {
        WindowedCounter counter = new WindowedCounter(3);
        input.forEach(counter::submit);
        return counter.count;
    }

    private static class WindowedCounter {
        final int size;
        final int[] w;
        int ok;
        int count = 0;

        WindowedCounter(int windowSize) {
            size = windowSize;
            w = new int[size + 1];
            ok = -size;
        }

        void submit(int value) {
            rangeClosed(1, size).forEach(i -> w[i] += value);
            if (++ok > 0 && w[1] > w[0]) count++;
            range(0, size).forEach(i -> w[i] = w[i + 1]);
            w[size] = 0;
        }
    }
}
