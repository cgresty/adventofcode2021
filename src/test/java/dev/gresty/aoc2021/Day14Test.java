package dev.gresty.aoc2021;

import dev.gresty.aoc2021.Day14.Polymerization;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day14.part1;
import static dev.gresty.aoc2021.Day14.part2;
import static org.assertj.core.api.Assertions.assertThat;

class Day14Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(1588L);
    }

    @Test
    public void testPart2() {
        assertThat(part2(input())).isEqualTo(2188189693529L);
    }

    private Stream<String> input() {
        return """
                NNCB
                                
                CH -> B
                HH -> N
                CB -> H
                NH -> C
                HB -> C
                HC -> B
                HN -> C
                NN -> C
                BH -> H
                NC -> B
                NB -> B
                BN -> B
                BB -> N
                BC -> B
                CC -> N
                CN -> C
                """.lines();
    }}