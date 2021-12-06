package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day06.part1;
import static dev.gresty.aoc2021.Day06.part2;
import static org.assertj.core.api.Assertions.assertThat;

class Day06Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(5934L);
    }

    @Test
    public void testPart2() {
        assertThat(part2(input())).isEqualTo(26984457539L);
    }

    private Stream<String> input() {
        return """
                3,4,3,1,2
                """.lines();
    }

}