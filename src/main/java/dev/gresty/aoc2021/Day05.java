package dev.gresty.aoc2021;

import dev.gresty.aoc2021.Utils.Point2;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;
import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class Day05 {

    public static void main(String[] args) {
        withStrings(Day05::part1, "day05");
        withStrings(Day05::part2, "day05");
    }

    public static int part1(Stream<String> input) {
        Set<Point2> intersections = new HashSet<>();
        VentLine[] lines = input.map(i -> Day05.ventLine(i, false))
                .filter(Objects::nonNull)
                .toArray(VentLine[]::new);
        for (int i = 0; i < lines.length - 1; i++) {
            for (int j = i + 1; j < lines.length; j++) {
                intersections.addAll(lines[i].intersection(lines[j]));
            }
        }
        return intersections.size();
    }

    public static int part2(Stream<String> input) {
        Set<Point2> intersections = new HashSet<>();
        VentLine[] lines = input.map(i -> Day05.ventLine(i, true))
                .filter(Objects::nonNull)
                .toArray(VentLine[]::new);
        for (int i = 0; i < lines.length - 1; i++) {
            for (int j = i + 1; j < lines.length; j++) {
                intersections.addAll(lines[i].intersection(lines[j]));
            }
        }
        return intersections.size();
    }

    static VentLine ventLine(String input, boolean includeDiagonals) {
        String[] ends = input.split(" ");
        String[] startStr = ends[0].split(",");
        String[] endStr = ends[2].split(",");
        Point2 start = new Point2(Integer.parseInt(startStr[0]), Integer.parseInt(startStr[1]));
        Point2 end = new Point2(Integer.parseInt(endStr[0]), Integer.parseInt(endStr[1]));
        if (start.x == end.x) {
            if (start.y < end.y)
                return new VerticalVentLine(start, end);
            else
                return new VerticalVentLine(end, start);
        } else if (start.y == end.y) {
            if (start.x < end.x)
                return new HorizontalVentLine(start, end);
            else
                return new HorizontalVentLine(end, start);
        } else if (includeDiagonals) {
            if (start.y < end.y)
                return new DiagonalVentLine(start, end);
            else
                return new DiagonalVentLine(end, start);
        }
        return null;
    }

    interface VentLine {
        List<Point2> intersection(VentLine other);
        List<Point2> satisfies(BiPredicate<Integer, Integer> predicate);
    }

    @RequiredArgsConstructor
    @ToString
    static class HorizontalVentLine implements VentLine {
        final Point2 from;
        final Point2 to;

        @Override
        public List<Point2> intersection(VentLine other) {
            return other.satisfies((x,y) -> y == from.y && x >= from.x && x <= to.x);
        }

        @Override
        public List<Point2> satisfies(BiPredicate<Integer, Integer> predicate) {
            return IntStream.rangeClosed(from.x, to.x)
                    .filter(x -> predicate.test(x, from.y))
                    .mapToObj(x -> new Point2(x, from.y))
                    .collect(Collectors.toList());
        }
    }

    @RequiredArgsConstructor
    @ToString
    static class VerticalVentLine implements VentLine {
        final Point2 from;
        final Point2 to;

        @Override
        public List<Point2> intersection(VentLine other) {
            return other.satisfies((x,y) -> x == from.x && y >= from.y && y <= to.y);
        }

        @Override
        public List<Point2> satisfies(BiPredicate<Integer, Integer> predicate) {
            return IntStream.rangeClosed(from.y, to.y)
                    .filter(y -> predicate.test(from.x, y))
                    .mapToObj(y -> new Point2(from.x, y))
                    .collect(Collectors.toList());
        }
    }

    @RequiredArgsConstructor
    @ToString
    static class DiagonalVentLine implements VentLine {
        final Point2 from;
        final Point2 to;

        @Override
        public List<Point2> intersection(VentLine other) {
            return other.satisfies((x,y) -> signum(x - from.x) != signum(x - to.x) && y >= from.y && y <= to.y && abs(x - from.x) == abs(y - from.y));
        }

        @Override
        public List<Point2> satisfies(BiPredicate<Integer, Integer> predicate) {
            int dx = (int) Math.signum(to.x - from.x);
            return IntStream.rangeClosed(0, to.y - from.y)
                    .mapToObj(d -> new Point2(from.x + d * dx, from.y + d))
                    .filter(p -> predicate.test(p.x, p.y))
                    .collect(Collectors.toList());
        }
    }

}
