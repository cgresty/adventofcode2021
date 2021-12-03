package dev.gresty.aoc2021;

import java.util.function.Function;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.stringsToLong;
import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.range;

public class Day03 {

    public static void main(String[] args) {
        stringsToLong(Day03::day03a, "day03.txt");
        stringsToLong(Day03::day03b, "day03.txt");
    }

    public static long day03a(Stream<String> input) {
        PowerConsumption pc = new PowerConsumption();
        input.forEach(pc::submit);
        return pc.power();
    }

    public static long day03b(Stream<String> input) {
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
            range(0, value.length())
                    .forEach(i -> counts[i] += value.charAt(i) - '0');
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
            values.add(value);
        }

        long oxygenRating() {
            String rating = values.find(n ->
                    n.childCount(0) > n.childCount(1) ? 0 : 1);
            return parseInt(rating, 2);
        }

        long co2Rating() {
            String rating = values.find(n ->
                    n.childCount(0) > n.childCount(1) ? 1 : 0);
            return parseInt(rating, 2);
        }

        long lifeSupportRating() {
            return co2Rating() * oxygenRating();
        }
    }

    static class Node {
        Node[] child = new Node[2];
        int count;

        void add(String value) {
            int index = value.charAt(0) - '0';
            String newValue = value.substring(1);
            if (child[index] == null) {
                child[index] = newValue.length() == 0 ?
                        new Leaf() :
                        new Node();
            }
            child[index].add(value.substring(1));
            count++;
        }

        int childCount(int index) {
            return child[index] == null ? 0 : child[index].count;
        }

        String find(Function<Node, Integer> criteria) {
            int next = child[0] == null ? 1 :
                    child[1] == null ? 0 :
                            criteria.apply(this);

            return next + child[next].find(criteria);
        }
    }

    static class Leaf extends Node {

        void add(String value) {
            if (value.length() != 0)
                throw new RuntimeException("Unexpected leaf value [" + value + "]");
            count++;
        }

        String find(Function<Node, Integer> criteria) {
            return "";
        }
    }
}
