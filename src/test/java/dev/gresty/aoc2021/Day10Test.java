package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day10.part1;
import static dev.gresty.aoc2021.Day10.part2;
import static org.assertj.core.api.Assertions.assertThat;

class Day10Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(26397);
    }

    @Test
    public void testPart2() {
        assertThat(part2(input())).isEqualTo(288957L);
    }

    private Stream<String> input() {
        return """
                [({(<(())[]>[[{[]{<()<>>
                [(()[<>])]({[<{<<[]>>(
                {([(<{}[<>[]}>{[]{[(<()>
                (((({<>}<{<{<>}{[]{[]{}
                [[<[([]))<([[{}[[()]]]
                [{[{({}]{}}([{[{{{}}([]
                {<[[]]>}<{[{[{[]{()[[[]
                [<(<(<(<{}))><([]([]()
                <{([([[(<>()){}]>(<<{{
                <{([{{}}[<[[[<>{}]]]>[]]
                """.lines();
    }}