package dev.gresty.aoc2021;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;
import static java.util.stream.IntStream.range;

public class Day11 {

    public static void main(String[] args) {
        withStrings(Day11::part1, "day11");
        withStrings(Day11::part2, "day11");
    }

    public static int part1(Stream<String> input) {
        OctopusSim1 sim = new OctopusSim1(10, input.collect(Collectors.joining()));
        return sim.simulate100();
    }

    public static long part2(Stream<String> input) {
        OctopusSim1 sim = new OctopusSim1(10, input.collect(Collectors.joining()));
        return sim.simulateUntilSynced();
    }

    static class OctopusSim1 {
        final int size;
        final long[] octopi;

        OctopusSim1(int size, String octopi) {
            this.size = size;
            this.octopi = octopi.chars().mapToLong(c -> c - '0').toArray();
        }

        int simulate100() {
            return range(0, 100).map(this::step).sum();
        }

        int simulateUntilSynced() {
            int i = 0;
            do {
                i++;
            } while (step(i) < 100);
            return i;
        }

        int step(int iteration) {
            int[] count = new int[1];
            Queue<Integer> flashed = new ArrayDeque<>();
            locations().forEach(l -> {
                if (++octopi[l] == 10) {
                    flashed.add(l);
                    count[0]++;
                }
            });

            Integer loc;
            while((loc = flashed.poll()) != null) {
                neighbours(loc).forEach(n -> {
                    if (++octopi[n] == 10) {
                        flashed.add(n);
                        count[0]++;
                    }
                });
            }

            locations().forEach(l -> {
                if (octopi[l] >= 10) {
                    octopi[l] = 0;
                }
            });

            return count[0];
        }

        IntStream neighbours(int loc) {
            int[] n = new int[8];
            int i = 0;
            for (int y = loc / size -1; y <= loc / size + 1; y++)
                for (int x = loc % size - 1; x <= loc % size + 1; x++)
                    if (loc(x, y) != loc && valid(x, y))
                        n[i++] = loc(x, y);

            return Arrays.stream(n, 0, i);
        }

        IntStream locations() {
            return range(0, size * size);
        }

        boolean valid(int x, int y) {
            return x >= 0 && y >= 0 && x < size && y < size;
        }

        int loc(int x, int y) {
            return y * size + x;
        }

    }

}
