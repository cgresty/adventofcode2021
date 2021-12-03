package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day02.part1;
import static dev.gresty.aoc2021.Day02.part2;
import static org.assertj.core.api.Assertions.assertThat;

class Day02Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(150);
    }

    @Test
    public void testPart2() {
        assertThat(part2(input())).isEqualTo(900);
    }

    private Stream<String> input() {
        return """
                forward 5
                down 5
                forward 8
                up 3
                down 8
                forward 2
                """.lines();
    }

}