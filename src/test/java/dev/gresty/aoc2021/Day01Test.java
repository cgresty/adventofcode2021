package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static dev.gresty.aoc2021.Day01.part1;
import static dev.gresty.aoc2021.Day01.part2;
import static org.assertj.core.api.Assertions.assertThat;

class Day01Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(7);
    }

    @Test
    public void testPart2() {
        assertThat(part2(input())).isEqualTo(5);
    }

    private IntStream input() {
        return """
                199
                200
                208
                210
                200
                207
                240
                269
                260
                263
                """.lines().mapToInt(Integer::parseInt);
    }

}