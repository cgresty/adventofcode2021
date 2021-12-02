package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day02.day02a;
import static dev.gresty.aoc2021.Day02.day02b;
import static org.assertj.core.api.Assertions.assertThat;

class Day02Test {

    @Test
    public void testDay02a() {
        assertThat(day02a(testInput())).isEqualTo(150);
    }

    @Test
    public void testDay02b() {
        assertThat(day02b(testInput())).isEqualTo(900);
    }

    private Stream<String> testInput() {
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