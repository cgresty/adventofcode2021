package dev.gresty.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day16.part1;
import static dev.gresty.aoc2021.Day16.part2;
import static org.assertj.core.api.Assertions.assertThat;

class Day16Test {

    @Test
    public void testPart1() {
        assertThat(part1("8A004A801A8002F478")).isEqualTo(16);
        assertThat(part1("620080001611562C8802118E34")).isEqualTo(12);
        assertThat(part1("C0015000016115A2E0802F182340")).isEqualTo(23);
        assertThat(part1("A0016C880162017C3686B18A3D4780")).isEqualTo(31);
    }

    @Test
    public void testPart2() {
        assertThat(part2("C200B40A82")).isEqualTo(3);
        assertThat(part2("04005AC33890")).isEqualTo(54);
        assertThat(part2("880086C3E88112")).isEqualTo(7);
        assertThat(part2("CE00C43D881120")).isEqualTo(9);
        assertThat(part2("D8005AC2A8F0")).isEqualTo(1);
        assertThat(part2("F600BC2D8F")).isEqualTo(0);
        assertThat(part2("9C005AC2F8F0")).isEqualTo(0);
        assertThat(part2("9C0141080250320F1802104A08")).isEqualTo(1);
    }
}