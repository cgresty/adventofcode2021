package dev.gresty.aoc2021;

import dev.gresty.aoc2021.Utils.IntPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.IntPair.rowCol;
import static dev.gresty.aoc2021.Utils.withStrings;

public class Day09 {

    public static void main(String[] args) {
        withStrings(Day09::part1, "day09");
        withStrings(Day09::part2, "day09");
    }

    public static int part1(Stream<String> input) {
        RiskAssessor assessor = new RiskAssessor();
        input.forEach(assessor::submit);
        assessor.finalise();
        return assessor.risk();
    }

    public static int part2(Stream<String> input) {
        RiskAssessor assessor = new RiskAssessor();
        input.forEach(assessor::submit);
        assessor.finalise();
        return assessor.basinSizes()
                .sorted(Collections.reverseOrder())
                .limit(3)
                .reduce(1, (a, b) -> a * b);
    }

    static class RiskAssessor {

        int len;
        int row;
        List<char[]> heights = new ArrayList<>();
        List<Low> lows = new ArrayList<>();

        void submit(String line) {
            int current = row;
            int compare = row - 1;
            int before = row - 2;

            heights.add(line.toCharArray());
            if (row == 0) {
                len = line.length();
            } else if (row == 1) { //
                compare(0, heights.get(compare), heights.get(current));
            } else {
                compare(compare, heights.get(compare), heights.get(before), heights.get(current));
            }

            row++;
        }

        void finalise() {
            int compare = row - 1;
            int before = row - 2;

            compare(compare, heights.get(compare), heights.get(before));
        }

        int risk() {
            return lows.stream().mapToInt(Low::risk).sum();
        }

        Stream<Integer> basinSizes() {
            int[][] basins = new int[heights.size()][heights.get(0).length];
            return lows.stream().map(low -> findBasin(low.value, low.location, basins));
        }

        int findBasin(int height, IntPair position, int[][] basins) {
            basins[position.row()][position.col()] = 1;
            return 1 + neighbours(position).mapToInt(n -> {
                char nHeight = heights.get(n.row())[n.col()];
                if (basins[n.row()][n.col()] == 0 && nHeight > height && nHeight < '9') {
                    return findBasin(nHeight, n, basins);
                }
                return 0;
            }).sum();
        }

        static final IntPair UP = rowCol(-1, 0);
        static final IntPair DOWN = rowCol(1, 0);
        static final IntPair LEFT = rowCol(0, -1);
        static final IntPair RIGHT = rowCol(0, 1);

        Stream<IntPair> neighbours(IntPair position) {
            List<IntPair> neighbours = new ArrayList<>();
            if (position.row() > 0) neighbours.add(position.add(UP));
            if (position.row() < heights.size() - 1) neighbours.add(position.add(DOWN));
            if (position.col() > 0) neighbours.add(position.add(LEFT));
            if (position.col() < len - 1) neighbours.add(position.add(RIGHT));
            return neighbours.stream();
        }

        private void compare(int row, char[] line, char[] other) {
            compareCorner(row, 0, 1, line, other);
            for (int i = 1; i < len - 1; i++) {
                if (line[i] < line[i - 1] && line[i] < line[i + 1] && line[i] < other[i])
                    lows.add(new Low(row, i, line[i]));
            }
            compareCorner(row, len - 1, -1, line, other);
        }

        private void compare(int row, char[] line, char[] prev, char[] next) {
            compareEdge(row, 0, 1, line, prev, next);
            for (int i = 1; i < len - 1; i++) {
                if (line[i] < line[i - 1] && line[i] < line[i + 1] && line[i] < prev[i] && line[i] < next[i])
                    lows.add(new Low(row, i, line[i]));
            }
            compareEdge(row, len - 1, -1, line, prev, next);
        }

        private void compareCorner(int row, int col, int right, char[] line, char[] other) {
            if (line[col] < line[col + right] && line[col] < other[col])
                lows.add(new Low(row, col, line[col]));
        }

        private void compareEdge(int row, int col, int right, char[] line, char[] prev, char[] next) {
            if (line[col] < line[col + right] && line[col] < prev[col] && line[col] < next[col])
                lows.add(new Low(row, col, line[col]));
        }
    }

    static class Low {
        IntPair location;
        int value;

        Low(int row, int col, char value) {
            location = rowCol(row, col);
            this.value = value - '0';
        }

        int risk() {
            return value + 1;
        }
    }

}
