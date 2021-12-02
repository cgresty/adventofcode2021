package dev.gresty.aoc2021;

import lombok.SneakyThrows;

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

    public static void withInts(Function<IntStream, Integer> function, String filename) {
        int result = function.apply(streamInput(filename).mapToInt(Integer::parseInt));
        System.out.println("Result: " + result);
    }

    public static void withStrings(Function<Stream<String>, Integer> function, String filename) {
        int result = function.apply(streamInput(filename));
        System.out.println("Result: " + result);
    }

}
