package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day15.part1;
import static dev.gresty.aoc2021.Day15.part2;
import static org.assertj.core.api.Assertions.assertThat;

class Day15Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(40);
    }

    @Test
    public void testPart2() {
        assertThat(part2(input())).isEqualTo(315);
    }

    private Stream<String> input() {
        return """
                1163751742
                1381373672
                2136511328
                3694931569
                7463417111
                1319128137
                1359912421
                3125421639
                1293138521
                2311944581
                """.lines();
    }}