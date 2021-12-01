package dev.gresty.aoc2021;

import org.testng.annotations.Test;

import java.util.stream.IntStream;

import static dev.gresty.aoc2021.Day01.day01a;
import static dev.gresty.aoc2021.Day01.day01b;
import static org.assertj.core.api.Assertions.assertThat;

public class Day01Test {

    @Test
    public void testDay01a() {
        assertThat(day01a(day01TestInput())).isEqualTo(7);
    }

    @Test
    public void testDay01b() {
        assertThat(day01b(day01TestInput())).isEqualTo(5);
    }

    private IntStream day01TestInput() {
        return """
                199
                200
                208
                210
                200
                207
                240
                269
                260
                263
                """.lines().mapToInt(Integer::parseInt);
    }
}