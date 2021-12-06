package dev.gresty.aoc2021;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @RequiredArgsConstructor
    @EqualsAndHashCode
    @ToString
    static class IntPair {
        final int x;
        final int y;

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
