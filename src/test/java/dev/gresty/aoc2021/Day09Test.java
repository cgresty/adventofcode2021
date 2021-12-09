package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day09.part1;
import static dev.gresty.aoc2021.Day09.part2;
import static org.assertj.core.api.Assertions.assertThat;

class Day09Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(15);
    }

    @Test
    public void testPart2() {
        assertThat(part2(input())).isEqualTo(1134);
    }

    private Stream<String> input() {
        return """
                2199943210
                3987894921
                9856789892
                8767896789
                9899965678
                """.lines();
    }}