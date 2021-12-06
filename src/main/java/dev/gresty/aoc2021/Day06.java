package dev.gresty.aoc2021;

import java.util.Arrays;
import java.util.stream.IntStream;

import static dev.gresty.aoc2021.Utils.withFirstLineInts;
import static java.util.stream.IntStream.range;

public class Day06 {

    static final int GESTATION = 7;
    static final int MATURATION = 2;
    static final int LIFECYCLE = MATURATION + GESTATION;

    public static void main(String[] args) {
        withFirstLineInts(Day06::part1, "day06");
        withFirstLineInts(Day06::part2, "day06");
    }

    public static long part1(IntStream input) {
        return simulate(input, 80);
    }

    public static long part2(IntStream input) {
        return simulate(input, 256);
    }

    public static long simulate(IntStream input, int days) {
        long[] fishByTimer = new long[LIFECYCLE];
        input.forEach(i -> fishByTimer[i]++);
        range(0, days).forEach(day -> { // Use day to index a circular array.
            // The spawning fish count at (day +) 0 automatically becomes the new fish count at (day +) 9
            // The spawning fish get added back in at (day +) 7 to start their new gestation period.
            fishByTimer[(day + GESTATION) % LIFECYCLE] += fishByTimer[day % LIFECYCLE];
        });
        return Arrays.stream(fishByTimer).sum();
    }

}
