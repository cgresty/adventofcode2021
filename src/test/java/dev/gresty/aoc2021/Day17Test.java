package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import static dev.gresty.aoc2021.Day17.part1;
import static dev.gresty.aoc2021.Day17.part2;
import static org.assertj.core.api.Assertions.assertThat;

class Day17Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(45);
    }

    @Test
    public void testPart2() {
        assertThat(part2(input())).isEqualTo(112);
    }

    private String input() {
        return "target area: x=20..30, y=-10..-5";
    }}