package dev.gresty.aoc2021;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;
import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;

public class Day11 {

    public static void main(String[] args) {
        withStrings(Day11::part1, "day11");
        withStrings(Day11::part2, "day11");
    }

    public static int part1(Stream<String> input) {
        OctopusSim sim = new OctopusSim(10, input.collect(Collectors.joining()));
        return sim.simulate100();
    }

    public static long part2(Stream<String> input) {
        OctopusSim sim = new OctopusSim(10, input.collect(Collectors.joining()));
        return sim.simulateUntilSynced();
    }

    static class OctopusSim {
        final int size;
        final int[] octopusEnergy;

        // Handy lookup tables
        final int[][][] neighbourOffsets; // avoids recalculating neighbours every.single.time.
        final int[] mapSizeTo3; // So we can map our full size octopus group to the 3x3 neighbour cases

        OctopusSim(int size, String octopi) {
            this.size = size;
            this.octopusEnergy = octopi.chars().map(c -> c - '0').toArray();
            this.neighbourOffsets = range(0, 3).mapToObj(x ->
                            range(0, 3).mapToObj(y -> calcNeighbours(x, y))
                                    .toArray(int[][]::new))
                    .toArray(int[][][]::new);
            this.mapSizeTo3 = range(0, size)
                    .map(i -> i == 0 ? 0 : i == size - 1 ? 2 : 1)
                    .toArray();
        }

        private int[] calcNeighbours(int x, int y) {
            return rangeClosed(-1, 1)
                    .flatMap(dx ->
                            rangeClosed(-1, 1)
                                    .filter(dy -> dx != 0 || dy != 0)
                                    .filter(dy -> valid3(x + dx, y + dy))
                                    .map(dy -> loc(dx, dy)))
                    .toArray();
        }

        private boolean valid3(int x, int y) {
            return x >= 0 && y >= 0 && x < 3 && y < 3;
        }

        private int loc(int x, int y) {
            return y * size + x;
        }

        int simulate100() {
            return range(0, 100).map(this::step).sum();
        }

        int simulateUntilSynced() {
            int i = 0;
            do { i++; } while (step(i) < 100);
            return i;
        }

        private int step(int iteration) {
            int[] count = new int[1];
            Queue<Integer> flashed = new ArrayDeque<>();
            locations().forEach(l -> {
                if (++octopusEnergy[l] == 10) {
                    flashed.add(l);
                    count[0]++;
                }
            });

            Integer loc;
            while((loc = flashed.poll()) != null) {
                neighbours(loc).forEach(n -> {
                    if (++octopusEnergy[n] == 10) {
                        flashed.add(n);
                        count[0]++;
                    }
                });
            }

            locations().forEach(l -> {
                if (octopusEnergy[l] >= 10) {
                    octopusEnergy[l] = 0;
                }
            });

            return count[0];
        }

        private IntStream neighbours(int loc) {
            return stream(neighbourOffsets[mapSizeTo3[loc % size]][mapSizeTo3[loc / size]])
                    .map(dl -> loc + dl);
        }

        private IntStream locations() {
            return range(0, size * size);
        }
    }
}
