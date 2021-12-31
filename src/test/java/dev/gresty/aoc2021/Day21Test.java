package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day21.part1;
import static dev.gresty.aoc2021.Day21.part2;
import static org.assertj.core.api.Assertions.assertThat;

class Day21Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(739785);
    }

    @Test
    public void testPart2() {
        assertThat(part2(input())).isEqualTo(444356092776315L);
    }

    private Stream<String> input() {
        return """
                Player 1 starting position: 4
                Player 2 starting position: 8
                """.lines();
    }}