package dev.gresty.aoc2021;

import dev.gresty.aoc2021.Utils.IntPair;
import dev.gresty.aoc2021.Utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;
import static java.lang.Integer.max;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toSet;

public class Day13 {

    public static void main(String[] args) {
        withStrings(Day13::part1, "day13");
        withStrings(Day13::part2, "day13");
    }

    public static int part1(Stream<String> input) {
        return new Folder(input).foldFirst().size();
    }

    public static long part2(Stream<String> input) {
        display(new Folder(input).foldAll());
        return 0;
    }

    static void display(Set<IntPair> dots) {
        int xmax = 0;
        int ymax = 0;
        for (IntPair dot : dots) {
            xmax = max(dot.x, xmax);
            ymax = max(dot.y, ymax);
        }
        char[][] display = new char[ymax + 1][xmax + 1];
        for (int i = 0; i <= ymax; i++) {
            Arrays.fill(display[i], ' ');
        }
        dots.forEach(dot -> display[dot.y][dot.x] = '#');

        for (int i = 0; i <= ymax; i++) {
            System.out.println(String.valueOf(display[i]));
        }
    }

    static class Folder {
        Set<IntPair> dots = new HashSet<>();
        List<Instruction> instructions = new ArrayList<>();

        Folder(Stream<String> input) {
            input.filter(line -> line.length() > 0)
                    .forEach(line -> {
                        if (line.startsWith("fold")) {
                            instructions.add(Instruction.from(line));
                        } else {
                            dots.add(IntPair.of(line));
                        }
                    });
        }

        Set<IntPair> foldAll() {
            Set<IntPair> folded = dots;
            for (Instruction instr : instructions) {
                folded = fold(folded, instr);
            }
            return folded;
        }

        Set<IntPair> foldFirst() {
            return fold(dots, instructions.get(0));
        }

        Set<IntPair> fold(Set<IntPair> dots, Pair<Character, Integer> instr) {
            return instr.a == 'x' ? foldOnX(dots, instr.b) : foldOnY(dots, instr.b);
        }

        Set<IntPair> foldOnX(Set<IntPair> dots, int fold) {
            return dots.stream()
                    .map(d -> d.x < fold ? d : IntPair.xy(2 * fold - d.x, d.y))
                    .collect(toSet());
        }

        Set<IntPair> foldOnY(Set<IntPair> dots, int fold) {
            return dots.stream()
                    .map(d -> d.y < fold ? d : IntPair.xy(d.x, 2 * fold - d.y))
                    .collect(toSet());
        }
    }

    static class Instruction extends Pair<Character, Integer> {

        public Instruction(Character character, Integer integer) {
            super(character, integer);
        }

        static Instruction from(String line) {
            int lastSpace = line.lastIndexOf(' ');
            int equals = line.lastIndexOf('=');
            return new Instruction(line.charAt(lastSpace + 1), parseInt(line.substring(equals + 1)));
        }
    }


}
