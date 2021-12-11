package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day11.part1;
import static dev.gresty.aoc2021.Day11.part2;
import static org.assertj.core.api.Assertions.assertThat;

class Day11Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(1656);
    }

    @Test
    public void testPart2() {
        assertThat(part2(input())).isEqualTo(195);
    }

    private Stream<String> input() {
        return """
                5483143223
                2745854711
                5264556173
                6141336146
                6357385478
                4167524645
                2176841721
                6882881134
                4846848554
                5283751526
                """.lines();
    }}