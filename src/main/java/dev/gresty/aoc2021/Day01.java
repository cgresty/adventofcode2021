package dev.gresty.aoc2021;

import java.util.stream.IntStream;

import static dev.gresty.aoc2021.Utils.withInts;
import static java.lang.Math.floorMod;

public class Day01 {

    public static void main(String[] args) {
        withInts(Day01::day01a, "day01.txt");
        withInts(Day01::day01b, "day01.txt");
    }

    static int day01a(IntStream input) {
        Counter counter = new Counter(1);
        return input.map(counter::submit).sum();
    }

    static int day01b(IntStream input) {
        Counter counter = new Counter(3);
        return input.map(counter::submit).sum();
    }

    private static class Counter {
        final int[] w;
        int idx;

        Counter(int windowSize) {
            w = new int[windowSize + 1];
            idx = -windowSize;
        }

        // To compare A+B+C to B+C+D, only need to compare A to D.
        int submit(int value) {
            w[floorMod(idx, w.length)] = value;
            return (++idx > 0 && value > w[floorMod(idx, w.length)]) ? 1 : 0;
        }
    }
}
