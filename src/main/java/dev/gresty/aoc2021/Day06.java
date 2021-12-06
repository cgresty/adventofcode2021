package dev.gresty.aoc2021;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;
import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;

public class Day06 {

    public static void main(String[] args) {
        withStrings(Day06::part1, "day06");
        withStrings(Day06::part2, "day06");
    }

    public static long part1(Stream<String> input) {
        return simulate(firstLineIntStream(input), 80);
    }

    public static long part2(Stream<String> input) {
        return simulate(firstLineIntStream(input), 256);
    }

    private static IntStream firstLineIntStream(Stream<String> input) {
        return Arrays.stream(input.findFirst().get().split(",")).mapToInt(Integer::parseInt);
    }

    public static long simulate(IntStream input, int days) {
        long[] fishByTimer = new long[9];
        input.forEach(i -> fishByTimer[i]++);
        rangeClosed(1, days)
                .forEach(day -> {
                    long spawning = fishByTimer[0];
                    range(0, 8).forEach(i -> fishByTimer[i] = fishByTimer[i+1]);
                    fishByTimer[6] += spawning;
                    fishByTimer[8] = spawning;
                });
        return Arrays.stream(fishByTimer).sum();
    }

}
