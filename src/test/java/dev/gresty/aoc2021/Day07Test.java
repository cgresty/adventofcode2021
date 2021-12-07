package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import static dev.gresty.aoc2021.Day07.part1;
import static dev.gresty.aoc2021.Day07.part2;
import static dev.gresty.aoc2021.Utils.intStreamOf;
import static org.assertj.core.api.Assertions.assertThat;

class Day07Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(37);
    }

    @Test
    public void testPart2() {
        assertThat(part2(input())).isEqualTo(168);
    }

    private int[] input() {
        return intStreamOf("16,1,2,0,4,2,7,1,2,14").toArray();
    }
}