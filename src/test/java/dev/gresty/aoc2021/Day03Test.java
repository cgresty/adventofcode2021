package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day03.day03a;
import static dev.gresty.aoc2021.Day03.day03b;
import static org.assertj.core.api.Assertions.assertThat;

class Day03Test {

    @Test
    public void testDay03a() {
        assertThat(day03a(testInput())).isEqualTo(198);
    }

    @Test
    public void testDay03b() {
        assertThat(day03b(testInput())).isEqualTo(230);
    }

    private Stream<String> testInput() {
        return """
                00100
                11110
                10110
                10111
                10101
                01111
                00111
                11100
                10000
                11001
                00010
                01010
                """.lines();
    }

}