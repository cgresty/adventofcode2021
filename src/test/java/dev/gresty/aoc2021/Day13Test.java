package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day13.part1;
import static org.assertj.core.api.Assertions.assertThat;

class Day13Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(17);
    }

    private Stream<String> input() {
        return """
                6,10
                0,14
                9,10
                0,3
                10,4
                4,11
                6,0
                6,12
                4,1
                0,13
                10,12
                3,4
                3,0
                8,4
                1,10
                2,14
                8,10
                9,0
                                
                fold along y=7
                fold along x=5
                """.lines();
    }}