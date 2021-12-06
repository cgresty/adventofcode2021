package dev.gresty.aoc2021;

import dev.gresty.aoc2021.Utils.IntPair;
import dev.gresty.aoc2021.Utils.Node;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;

public class Day05 {

    public static void main(String[] args) {
        withStrings(Day05::part1, "day05");
        withStrings(Day05::part2, "day05");
    }

    public static int part1(Stream<String> input) {
        VentMapper mapper = new VentMapper(false);
        input.forEach(mapper::submit);
        return mapper.dangerous;
    }

    public static int part2(Stream<String> input) {
        VentMapper mapper = new VentMapper(true);
        input.forEach(mapper::submit);
        return mapper.dangerous;
    }

    static class VentMapper {

        Node ventMap = new Node();
        int dangerous;
        boolean includeDiagonals;

        VentMapper(boolean includeDiagonals) {
            this.includeDiagonals = includeDiagonals;
        }

        void submit(String input) {
            String[] ends = input.split(" ");
            IntPair start = IntPair.of(ends[0]);
            IntPair end = IntPair.of(ends[2]);
            IntPair d = end.subtract(start).signum();
            if (d.x != 0 && d.y != 0 && !includeDiagonals) return;
            IntPair i = start.subtract(d);
            while (!i.equals(end)) {
                i = i.add(d);
                if (ventMap.store(i.toArray()) == 2) {
                    dangerous++;
                }
            }
        }
    }
}
