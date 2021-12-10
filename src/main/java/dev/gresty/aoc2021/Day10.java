package dev.gresty.aoc2021;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;
import static java.util.stream.Collectors.toList;

public class Day10 {

    public static void main(String[] args) {
        withStrings(Day10::part1, "day10");
        withStrings(Day10::part2, "day10");
    }

    public static int part1(Stream<String> input) {
        SyntaxChecker checker = new SyntaxChecker();
        return input.mapToInt(checker::submit).sum();
    }

    public static long part2(Stream<String> input) {
        SyntaxChecker checker = new SyntaxChecker();
        input.forEach(checker::submit);
        return checker.autocomplete();
    }

    static class SyntaxChecker {
        private static final String OPEN = "([{<";
        private static final String CLOSE = ")]}>";
        private static final int[] SYNTAX = new int[]{3, 57, 1197, 25137};

        Set<Deque<Integer>> autocomplete = new HashSet<>();

        int submit(String line) {
            Deque<Integer> brackets = new ArrayDeque<>();
            for (char c : line.toCharArray()) {
                int open = OPEN.indexOf(c);
                if (open > -1) brackets.push(open);
                else {
                    int close = CLOSE.indexOf(c);
                    if (close != brackets.pop()) {
                        return SYNTAX[close];
                    }
                }
            }
            if (!brackets.isEmpty()) {
                autocomplete.add(brackets);
            }
            return 0;
        }

        long autocomplete() {
            List<Long> inOrder = autocomplete.stream()
                    .map(q -> q.stream()
                            .mapToLong(i -> i + 1)
                            .reduce(0, (a, i) -> a * 5 + i))
                    .sorted()
                    .collect(toList());
            return inOrder.get(inOrder.size() / 2);
        }
    }
}
