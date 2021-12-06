package dev.gresty.aoc2021;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
    static class Point2 {
        final int x;
        final int y;
    }
}
