package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day03.part1;
import static dev.gresty.aoc2021.Day03.part2;
import static org.assertj.core.api.Assertions.assertThat;

class Day03Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(198);
    }

    @Test
    public void testPart2() {
        assertThat(part2(input())).isEqualTo(230);
    }

    private Stream<String> input() {
        return """
                00100
                11110
                10110
                10111
                10101
                01111
                00111
                11100
                10000
                11001
                00010
                01010
                """.lines();
    }

}