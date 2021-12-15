package dev.gresty.aoc2021;

import dev.gresty.aoc2021.Utils.IntPair;

import java.util.PriorityQueue;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;
import static java.util.Comparator.comparingInt;

public class Day15 {

    public static void main(String[] args) {
        withStrings(Day15::part1, "day15");
        withStrings(Day15::part2, "day15");
    }

    public static int part1(Stream<String> input) {
        return new PathFinder(input).findLeastRiskPath();
    }

    public static int part2(Stream<String> input) {
        return new PathFinder(input).expand().findLeastRiskPath();
    }

    static class PathFinder {

        int size;
        int[][] risk;
        int[][] paths;
        IntPair[] neighbours = new IntPair[] {
                IntPair.xy(-1, 0),
                IntPair.xy(0, 1),
                IntPair.xy(1, 0),
                IntPair.xy(0, -1),
        };

        PathFinder(Stream<String> input) {
            // assume square
            risk = input.map(String::chars)
                    .map(istr -> istr.map(i -> i - '0').toArray())
                    .toArray(int[][]::new);
            size = risk.length;
            paths = new int[size][size];
        }

        PathFinder expand() {
            int newSize = 5 * size;
            int[][] newRisk = new int[newSize][newSize];
            for (int ymult = 0; ymult < 5; ymult++)
                for (int xmult = 0; xmult < 5; xmult++)
                    for (int y = 0; y < size; y++)
                        for (int x = 0; x < size; x++)
                            newRisk[ymult * size + y][xmult * size + x]
                                    = (risk[y][x] + ymult + xmult - 1) % 9 + 1;
            size = newSize;
            risk = newRisk;
            paths = new int[size][size];
            return this;
        }

        int findLeastRiskPath() {

            PriorityQueue<IntPair> pathsToCheck = new PriorityQueue<>(comparingInt(a -> paths[a.y][a.x]));
            pathsToCheck.add(IntPair.xy(0, 0));
            while(!pathsToCheck.isEmpty()) {
                IntPair node = pathsToCheck.poll();
                for (int i = 0; i < 4; i++) {
                    IntPair next = node.add(neighbours[i]);
                    int x = next.x;
                    int y = next.y;
                    if (x >= 0 && x < size && y >= 0 && y < size) {
                        int p = paths[y][x];
                        if (p == 0 || pathsToCheck.remove(next)) {
                            int d = paths[node.y][node.x] + risk[y][x];
                            if (p == 0 || p > d) {
                                paths[y][x] = d;
                            }
                            pathsToCheck.add(next);
                        }
                    }
                }
            }
            return paths[risk.length - 1][risk[0].length - 1];
        }
    }
}
