package dev.gresty.aoc2021;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class Utils {

    @SneakyThrows
    public static Stream<String> streamInput(String filename) {
        URL resource = Utils.class.getResource(filename);
        if (resource == null)
            throw new RuntimeException("No such input file " + filename);
        return Files.lines(Path.of(resource.toURI()));
    }

    public static <T> void withInts(Function<IntStream, T> function, String filename) {
        T result = function.apply(streamInput(filename).mapToInt(Integer::parseInt));
        System.out.println("Result: " + result);
    }

    public static <T> void withStrings(Function<Stream<String>, T> function, String filename) {
        T result = function.apply(streamInput(filename));
        System.out.println("Result: " + result);
    }

    public static<T> void withFirstLineInts(Function<IntStream, T> function, String filename) {
        IntStream intStream = intStreamOf(streamInput(filename).findFirst().orElse("Failed to read first line"));
        T result = function.apply(intStream);
        System.out.println("Result: " + result);
    }

    public static<T> void withIntArray(Function<int[], T> function, String filename) {
        int[] intArray = intStreamOf(streamInput(filename).findFirst().orElse("Failed to read first line")).toArray();
        T result = function.apply(intArray);
        System.out.println("Result: " + result);
    }

    public static IntStream intStreamOf(String lineOfCommaSeparatedInts) {
        return Arrays.stream(lineOfCommaSeparatedInts.split(","))
                .mapToInt(Integer::parseInt);
    }

    @EqualsAndHashCode
    @ToString
    static class IntPair {
        final int x;
        final int y;

        private IntPair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int row() {
            return y;
        }

        int col() {
            return x;
        }

        IntPair add(IntPair other) {
            return new IntPair(x + other.x, y + other.y);
        }

        IntPair subtract(IntPair other) {
            return new IntPair(x - other.x, y - other.y);
        }

        IntPair signum() {
            return new IntPair(Integer.signum(x), Integer.signum(y));
        }

        int[] toArray() {
            return new int[] {x, y};
        }

        static IntPair of(String asString) {
            String[] values = asString.split(",");
            return new IntPair(parseInt(values[0]), parseInt(values[1]));
        }

        static IntPair rowCol(int row, int col) {
            return new IntPair(col, row);
        }

        static IntPair xy(int x, int y) {
            return new IntPair(x, y);
        }
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode
    @ToString
    static class Pair<A, B> {
        final A a;
        final B b;

        static <A, B> Pair<A, B> of(A a, B b) {
            return new Pair<>(a, b);
        }
    }

    static class Node {
        Map<Integer, Node> children = new HashMap<>();
        int count;

        int store(int[] value) {
            return store(value, 0);
        }

        private int store(int[] value, int offset) {
            count++;
            if (value.length > offset) {
                int index = value[offset];
                Node child = children.computeIfAbsent(index, i -> new Node());
                return child.store(value, offset + 1);
            }
            return count;
        }
    }

}
