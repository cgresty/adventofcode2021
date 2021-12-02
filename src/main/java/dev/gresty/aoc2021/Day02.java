package dev.gresty.aoc2021;

import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;

public class Day02 {

    public static void main(String[] args) {
        withStrings(Day02::day02a, "day02.txt");
        withStrings(Day02::day02b, "day02.txt");
    }

    public static int day02a(Stream<String> input) {
        CourseReader reader = new CourseReader(CommandA::valueOf);
        input.forEach(reader::submit);
        return reader.sub.depth * reader.sub.horizontal;
    }

    public static int day02b(Stream<String> input) {
        CourseReader reader = new CourseReader(CommandB::valueOf);
        input.forEach(reader::submit);
        return reader.sub.depth * reader.sub.horizontal;
    }

    static class Submarine {
        int horizontal;
        int depth;
        int aim;
    }

    private static class CourseReader {
        Submarine sub = new Submarine(); // across,down
        Function<String, Movement> movementFactory;

        CourseReader(Function<String, Movement> movementFactory) {
            this.movementFactory = movementFactory;
        }

        void submit(String command) {
            String[] tokens = command.split(" ");
            movementFactory
                    .apply(tokens[0].toUpperCase(Locale.ROOT))
                    .move(sub, Integer.parseInt(tokens[1]));
        }
    }

    public interface Movement {
        void move(Submarine sub, int quantity);
    }

    enum CommandA implements Movement {
        UP((sub, q) -> { sub.depth -= q; }),
        DOWN((sub, q) -> { sub.depth += q; }),
        FORWARD((sub, q) -> { sub.horizontal += q; });

        final BiConsumer<Submarine, Integer> operation;

        CommandA(BiConsumer<Submarine, Integer> operation) {
            this.operation = operation;
        }

        @Override
        public void move(Submarine sub, int quantity) {
            operation.accept(sub, quantity);
        }
    }

    enum CommandB implements Movement {
        UP((sub, q) -> { sub.aim -= q; }),
        DOWN((sub, q) -> { sub.aim += q; }),
        FORWARD((sub, q) -> {
            sub.horizontal += q;
            sub.depth += sub.aim * q;
        });

        final BiConsumer<Submarine, Integer> operation;

        CommandB(BiConsumer<Submarine, Integer> operation) {
            this.operation = operation;
        }

        @Override
        public void move(Submarine sub, int quantity) {
            operation.accept(sub, quantity);
        }
    }
}
