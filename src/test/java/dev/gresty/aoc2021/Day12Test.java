package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day12.part1;
import static dev.gresty.aoc2021.Day12.part2;
import static org.assertj.core.api.Assertions.assertThat;

class Day12Test {

    @Test
    public void testPart1a() {
        assertThat(part1(inputA())).isEqualTo(10);
    }

    @Test
    public void testPart1b() {
        assertThat(part1(inputB())).isEqualTo(19);
    }

    @Test
    public void testPart1c() {
        assertThat(part1(inputC())).isEqualTo(226);
    }

    @Test
    public void testPart2a() {
        assertThat(part2(inputA())).isEqualTo(36);
    }

    @Test
    public void testPart2b() {
        assertThat(part2(inputB())).isEqualTo(103);
    }

    @Test
    public void testPart2c() {
        assertThat(part2(inputC())).isEqualTo(3509);
    }

    private Stream<String> inputA() {
        return """
                start-A
                start-b
                A-c
                A-b
                b-d
                A-end
                b-end
                """.lines();
    }

    private Stream<String> inputB() {
        return """
                dc-end
                HN-start
                start-kj
                dc-start
                dc-HN
                LN-dc
                HN-end
                kj-sa
                kj-HN
                kj-dc
                """.lines();
    }

    private Stream<String> inputC() {
        return """
                fs-end
                he-DX
                fs-he
                start-DX
                pj-DX
                end-zg
                zg-sl
                zg-pj
                pj-he
                RW-he
                fs-DX
                pj-RW
                zg-RW
                start-pj
                he-WI
                zg-he
                pj-fs
                start-RW
                """.lines();
    }
}