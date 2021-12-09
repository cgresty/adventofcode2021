package dev.gresty.aoc2021;

import dev.gresty.aoc2021.Utils.Pair;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;

public class Day08 {

    public static void main(String[] args) {
        withStrings(Day08::part1, "day08");
        withStrings(Day08::part2, "day08");
    }

    public static long part1(Stream<String> input) {
        return input.map(Entry::of).mapToLong(Entry::easyDigits).sum();
    }

    public static int part2(Stream<String> input) {
        return input.map(Entry::of).mapToInt(entry -> new Decoder(entry).decode()).sum();
    }

    @RequiredArgsConstructor
    static class Entry {
        final String[] signals;
        final String[] outputs;

        static Entry of(String line) {
            String[] tokens = line.split("\\|");
            String[] signals = tokens[0].trim().split(" ");
            String[] outputs = tokens[1].trim().split(" ");
            return new Entry(signals, outputs);
        }

        long easyDigits() {
            return stream(outputs).mapToInt(this::decode)
                    .filter(i -> i == 1 || i == 4 || i == 7 || i == 8)
                    .count();
        }

        Set<String> signalsOfLength(int len) {
            return stream(signals).filter(s -> s.length() == len).collect(toSet());
        }

        int decode(String digit) {
            if (digit.length() == 2) return 1;
            if (digit.length() == 4) return 4;
            if (digit.length() == 3) return 7;
            if (digit.length() == 7) return 8;
            return 0;
        }
    }

    static final Set<Integer>[] DIGIT_SEGMENTS = new Set[] {
            Set.of(0, 1, 2, 4, 5, 6),
            Set.of(2, 5),
            Set.of(0, 2, 3, 4, 6),
            Set.of(0, 2, 3, 5, 6),
            Set.of(1, 2, 3, 5),
            Set.of(0, 1, 3, 5, 6),
            Set.of(0, 1, 3, 4, 5, 6),
            Set.of(0, 2, 5),
            Set.of(0, 1, 2, 3, 4, 5, 6),
            Set.of(0, 1, 2, 3, 5, 6)
    };

    static class Decoder {
        final Entry entry;
        final Choice<Character, Integer> signalToSegmentChoices;

        Decoder(Entry entry) {
            this.entry = entry;
            signalToSegmentChoices = new Choice<>(range(0, 7).mapToObj(i -> (char) ('a' + i)).toArray(Character[]::new),
                    range(0, 7).boxed().toArray(Integer[]::new));
        }

        int decode() {
            String one = getOnly(entry.signalsOfLength(2));
            signalToSegmentChoices.set(toSet(one), DIGIT_SEGMENTS[1]);

            String seven = getOnly(entry.signalsOfLength(3));
            signalToSegmentChoices.set(toSet(seven), DIGIT_SEGMENTS[7]);

            String four = getOnly(entry.signalsOfLength(4));
            signalToSegmentChoices.set(toSet(four), DIGIT_SEGMENTS[4]);

            Set<String> l5 = entry.signalsOfLength(5);
            String three = l5.stream().filter(s -> containsAllOf(s, one)).findFirst().orElse("X");
            signalToSegmentChoices.set(toSet(three), DIGIT_SEGMENTS[3]);

            Set<String> l6 = entry.signalsOfLength(6);
            String nine = l6.stream().filter(s -> containsAllOf(s, four)).findFirst().orElse("X");
            signalToSegmentChoices.set(toSet(nine), DIGIT_SEGMENTS[9]);

            l6.remove(nine);
            String zero = l6.stream().filter(s -> containsAllOf(s, one)).findFirst().orElse("X");
            signalToSegmentChoices.set(toSet(zero), DIGIT_SEGMENTS[0]);

            l6.remove(zero);
            String six = getOnly(l6);
            signalToSegmentChoices.set(toSet(six), DIGIT_SEGMENTS[6]);

            return Integer.parseInt(stream(entry.outputs).map(this::displayNumber).collect(joining()));
        }

        Set<Character> toSet(String signal) {
            return signal.chars().mapToObj(i -> (char) i).collect(Collectors.toSet());
        }

        <T> T getOnly(Set<T> set) {
            return set.iterator().next();
        }

        boolean containsAllOf(String a, String b) {
            for (char c : b.toCharArray()) {
                if (a.indexOf(c) == -1) return false;
            }
            return true;
        }

        String displayNumber(String segments) {
            Set<Integer> decodedSegments = new HashSet<>();
            for (char s : segments.toCharArray()) {
                decodedSegments.add(signalToSegmentChoices.get(s));
            }
            for (int i = 0; i < 10; i++) {
                if (DIGIT_SEGMENTS[i].size() == decodedSegments.size() &&
                        DIGIT_SEGMENTS[i].containsAll(decodedSegments)) {
                    return String.valueOf(i);
                }
            }
            return "X";
        }
    }

    static class Choice<A, B> {

        final int size;
        final Set<Pair<A, B>> choices;
        final A[] allA;
        final B[] allB;

        Choice(A[] as, B[] bs) {
            size = as.length;
            choices = stream(as)
                    .flatMap(a -> stream(bs).map(b -> Pair.of(a, b)))
                    .collect(toSet());
            allA = as;
            allB = bs;
        }

        B get(A a) {
            return allBs(a).iterator().next();
        }

        void set(Set<A> aSet, Set<B> bSet) {
            Set<Pair<A, B>> toRemove = choices.stream()
                    .filter(ab -> bothOrNeitherMatch(ab, aSet, bSet))
                    .collect(toSet());
            toRemove.forEach(choices::remove);
        }

        boolean bothOrNeitherMatch(Pair<A, B> ab, Set<A> a, Set<B> b) {
            return (a.contains(ab.a) && !b.contains(ab.b)) ||
                    (!a.contains(ab.a) && b.contains(ab.b));
        }

        Set<B> allBs(A a) {
            return choices.stream().filter(ab -> ab.a.equals(a))
                    .map(ab -> ab.b)
                    .collect(toSet());
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            stream(allA).forEach(a -> {
                builder.append(a).append(": ");
                allBs(a).stream().sorted().forEach(b -> builder.append(b).append(" "));
                builder.append("\n");
            });
            return builder.toString();
        }
    }

}
