package dev.gresty.aoc2021;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
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
        private static final String BRACKETS = "()[]{}<>";
        private static final int[] SYNTAX_SCORE = new int[]{3, 57, 1197, 25137};

        private final List<Long> autoCompleteScores = new ArrayList<>();

        int submit(String line) {
            Deque<Integer> brackets = new ArrayDeque<>();
            for (char c : line.toCharArray()) {
                int code = BRACKETS.indexOf(c);
                if (code % 2 == 0) brackets.push(code + 1);
                else {
                    if (code != brackets.pop()) {
                        return SYNTAX_SCORE[code / 2];
                    }
                }
            }
            if (!brackets.isEmpty()) {
                autoCompleteScores.add(brackets.stream()
                        .mapToLong(i -> i / 2 + 1)
                        .reduce(0, (acc, l) -> acc * 5 + l));
            }
            return 0;
        }

        long autocomplete() {
            List<Long> inOrder = autoCompleteScores.stream()
                    .sorted()
                    .collect(toList());
            return inOrder.get(inOrder.size() / 2);
        }
    }
}
