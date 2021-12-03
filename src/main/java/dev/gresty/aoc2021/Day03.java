package dev.gresty.aoc2021;

import java.util.function.Function;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;
import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.range;

public class Day03 {

    public static void main(String[] args) {
        withStrings(Day03::part1, "day03");
        withStrings(Day03::part2, "day03");
    }

    public static long part1(Stream<String> input) {
        PowerConsumption pc = new PowerConsumption();
        input.forEach(pc::submit);
        return pc.power();
    }

    public static long part2(Stream<String> input) {
        Ratings r = new Ratings();
        input.forEach(r::submit);
        return r.lifeSupportRating();
    }

    static class PowerConsumption {
        int[] counts;
        int numValues;

        void submit(String value) {
            if (counts == null) counts = new int[value.length()];
            numValues++;
            range(0, value.length()).forEach(i -> counts[i] += value.charAt(i) - '0');
        }

        long gamma() {
            long gamma = 0;
            for (int count : counts) {
                gamma = gamma << 1;
                if (count > numValues / 2) gamma += 1;
            }
            return gamma;
        }

        long epsilon(long gamma) {
            long ones = (1L << counts.length) - 1;
            return gamma ^ ones;
        }

        long power() {
            long gamma = gamma();
            return gamma * epsilon(gamma);
        }

    }

    static class Ratings {
        Node values = new Node();

        void submit(String value) {
            values.store(value);
        }

        long oxygenRating() {
            String rating = values.find(n -> n.count(0) > n.count(1) ? 0 : 1);
            return parseInt(rating, 2);
        }

        long co2Rating() {
            String rating = values.find(n -> n.count(0) > n.count(1) ? 1 : 0);
            return parseInt(rating, 2);
        }

        long lifeSupportRating() {
            return co2Rating() * oxygenRating();
        }
    }

    static class Node {
        Node[] children = new Node[2];
        int count;

        void store(String value) {
            count++;
            if (value.length() > 0) {
                int index = value.charAt(0) - '0';
                if (children[index] == null)
                    children[index] = new Node();
                children[index].store(value.substring(1));
            }
        }

        int count(int index) {
            return children[index] == null ? 0 : children[index].count;
        }

        String find(Function<Node, Integer> criteria) {
            if (children[0] == null && children[1] == null) { // Leaf
                return "";
            }

            int next = children[0] == null ? 1 :
                    children[1] == null ? 0 :
                            criteria.apply(this);

            return next + children[next].find(criteria);
        }
    }
}
