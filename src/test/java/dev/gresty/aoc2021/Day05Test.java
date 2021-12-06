package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day05.part1;
import static dev.gresty.aoc2021.Day05.part2;
import static org.assertj.core.api.Assertions.assertThat;

class Day05Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(5);
    }

    @Test
    public void testPart2() {
        assertThat(part2(input())).isEqualTo(12);
    }

    private Stream<String> input() {
        return """
                0,9 -> 5,9
                8,0 -> 0,8
                9,4 -> 3,4
                2,2 -> 2,1
                7,0 -> 7,4
                6,4 -> 2,0
                0,9 -> 2,9
                3,4 -> 1,4
                0,0 -> 8,8
                5,5 -> 8,2
                """.lines();
    }

}