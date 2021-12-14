package dev.gresty.aoc2021;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;

public class Day14 {

    public static void main(String[] args) {
        withStrings(Day14::part1, "day14");
        withStrings(Day14::part2, "day14");
    }

    public static long part1(Stream<String> input) {
        return new Polymerization(input).execute(10);
    }

    public static long part2(Stream<String> input) {
        return new Polymerization(input).execute(40);
    }

    static class Polymerization {
        Map<String, Pair> insertionRules;
        String startingPolymer;

        Polymerization(Stream<String> input) {
            insertionRules = input
                    .peek(line -> {
                        if (line.length() != 7 && line.length() > 0) startingPolymer = line;
                    })
                    .filter(line -> line.length() == 7)
                    .map(Pair::from)
                    .collect(Collectors.toMap(Pair::name, pair -> pair));
            insertionRules.forEach((k, v) -> v.setup(insertionRules));
        }

        void init() {
            insertionRules.forEach((k, v) -> v.reset());
            for (int i = 0; i < startingPolymer.length() - 1; i++) {
                insertionRules.get(startingPolymer.substring(i, i + 2)).increment(1);
            }
            insertionRules.forEach((k, v) -> v.update());
        }

        long execute(int steps) {
            init();
            for (int s = 1; s <= steps; s++) {
                insertionRules.forEach((k, v) -> v.insert());
                insertionRules.forEach((k, v) -> v.update());
            }
            return minMaxDifference();
        }

        long minMaxDifference() {
            long[] counts = new long[26];
            insertionRules.forEach((k, v) -> {
                counts[k.charAt(0) - 'A'] += v.count;
                counts[k.charAt(1) - 'A'] += v.count;
            });
            counts[firstElement() - 'A'] += 1;
            counts[lastElement() - 'A'] += 1;
            for (int i = 0; i < counts.length; i++) {
                counts[i] = counts[i] / 2;
            }
            long min = Long.MAX_VALUE;
            long max = 0;
            for(long count : counts) {
                if (count != 0 && count < min) min = count;
                if (count != 0 && count > max) max = count;
            }
            return max - min;
        }

        char firstElement() {
            return startingPolymer.charAt(0);
        }

        char lastElement() {
            return startingPolymer.charAt(startingPolymer.length() - 1);
        }
    }

    static class Pair {
        String name;
        String[] insertIntoNames = new String[2];
        Pair[] insertInto = new Pair[2];
        long count;
        long newCount;

        Pair(String name, char insertElement) {
            this.name = name;
            insertIntoNames[0] = "" + name.charAt(0) + insertElement;
            insertIntoNames[1] = "" + insertElement + name.charAt(1);
        }

        static Pair from(String line) {
            return new Pair(line.substring(0, 2), line.charAt(6));
        }

        String name() {
            return name;
        }

        void setup(Map<String, Pair> pairs) {
            insertInto[0] = pairs.get(insertIntoNames[0]);
            insertInto[1] = pairs.get(insertIntoNames[1]);
        }

        void insert() {
            insertInto[0].increment(count);
            insertInto[1].increment(count);
        }

        void increment(long extra) {
            newCount += extra;
        }

        void update() {
            count = newCount;
            newCount = 0;
        }

        void reset() {
            count = 0;
            newCount = 0;
        }
    }
}
