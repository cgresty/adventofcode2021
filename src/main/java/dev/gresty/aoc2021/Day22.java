package dev.gresty.aoc2021;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;
import static java.lang.Integer.parseInt;
import static java.lang.Long.max;
import static java.lang.Long.min;

public class Day22 {

    public static void main(String[] args) {
        withStrings(Day22::part1, "day22");
        withStrings(Day22::part2, "day22");
    }

    public static long part1(Stream<String> input) {
        return processCuboids(parseCuboids(input, Cuboid.of(-50, 50)));
    }

    public static long part2(Stream<String> input) {
        return processCuboids(parseCuboids(input, Cuboid.ofInfinity()));
    }

    static List<Cuboid> parseCuboids(Stream<String> input, Cuboid limit) {
        return input.map(Cuboid::from)
                .filter(Objects::nonNull)
                .filter(c -> c.within(limit))
                .toList();
    }

    static long processCuboids(List<Cuboid> cuboids) {
        List<Cuboid> processed = new ArrayList<>();
        List<Cuboid> extra = new ArrayList<>();
        long on = 0;
        for (Cuboid next : cuboids) {
            extra.clear();
            if (next.value == 1) {
                extra.add(next);
                on += next.size();
            }
            for (Cuboid existing : processed) {
                Cuboid intersect = next.intersect(existing);
                if (intersect.isEmpty()) continue;
                on += intersect.size() * intersect.value;
                extra.add(intersect);
            }
            processed.addAll(extra);
        }
        return on;
    }

    record Range(long from, long to) {
        boolean within(Range other) {
            return from >= other.from && to <= other.to;
        }

        boolean isEmpty() {
            return from >= to;
        }

        long size() {
            return to - from + 1;
        }

        Range intersect(Range other) {
            return Range.of(max(from, other.from), min(to, other.to));
        }

        static Range of(String from, String to) {
            return Range.of(parseInt(from), parseInt(to));
        }

        static Range of(long from, long to) {
            return new Range(from, to);
        }
    }

    record Cuboid(Range x, Range y, Range z, int value) {

        boolean within(Cuboid other) {
            return x.within(other.x) && y.within(other.y) && z.within(other.z);
        }

        Cuboid intersect(Cuboid other) {
            return new Cuboid(x.intersect(other.x), y.intersect(other.y), z.intersect(other.z), -other.value);
        }

        boolean isEmpty() {
            return x.isEmpty() || y.isEmpty() || z.isEmpty();
        }

        long size() {
            return x.size() * y.size() * z.size();
        }

        static final Pattern PATTERN = Pattern.compile("(on|off) x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)");
        static Cuboid from(String line) {
            Matcher matcher = PATTERN.matcher(line);
            if (matcher.find()) {
                String onOff = matcher.group(1);
                String xmin = matcher.group(2);
                String xmax = matcher.group(3);
                String ymin = matcher.group(4);
                String ymax = matcher.group(5);
                String zmin = matcher.group(6);
                String zmax = matcher.group(7);
                return new Cuboid(Range.of(xmin, xmax),
                        Range.of(ymin, ymax),
                        Range.of(zmin, zmax),
                        onOff.equals("on") ? 1 : -1);
            }
            return null;
        }

        static Cuboid of(int min, int max) {
            return new Cuboid(Range.of(min, max), Range.of(min, max), Range.of(min, max), -1);
        }

        static Cuboid ofInfinity() {
            return of(Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }
}
