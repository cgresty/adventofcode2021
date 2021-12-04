package dev.gresty.aoc2021;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;

public class Day04 {

    public static void main(String[] args) {
        withStrings(Day04::part1, "day04");
        withStrings(Day04::part2, "day04");
    }

    public static int part1(Stream<String> input) {
        Generator g = new Generator();
        input.forEach(g::submit);
        return g.game.play(true);
    }

    public static int part2(Stream<String> input) {
        Generator g = new Generator();
        input.forEach(g::submit);
        return g.game.play(false);
    }

    static class Generator {
        Game game;
        int[][] currentBoard;
        int row;

        void submit(String line) {
            if (game == null) {
                int[] numbers = toInts(line,",");
                game = new Game(numbers);
            } else if (currentBoard == null) {
                currentBoard = new int[5][];
            } else {
                currentBoard[row++] = toInts(line, " ");
                if (row == 5) {
                    game.add(new Board(currentBoard));
                    currentBoard = null;
                    row = 0;
                }
            }
        }

        int[] toInts(String line, String separator) {
            return Arrays.stream(line.split(separator))
                    .filter(s -> s.length() > 0)
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }
    }

    @RequiredArgsConstructor
    static class NumberInfo {
        final int row;
        final int column;
        boolean matched;
    }

    static class Board {
        Map<Integer, NumberInfo> numbers = new HashMap<>();
        int[] rowCounts = new int[5];
        int[] columnCounts = new int[5];
        boolean completed;

        public Board(int[][] numbers) {
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    this.numbers.put(numbers[row][col], new NumberInfo(row, col));
                }
            }
        }

        boolean call(int number) {
            NumberInfo info = numbers.get(number);
            if (info == null) return false;
            info.matched = true;
            completed = ++rowCounts[info.row] == 5 || ++columnCounts[info.column] == 5;
            return completed;
        }

        int score(int winningNumber) {
            return numbers.entrySet().stream()
                    .filter(e -> !e.getValue().matched)
                    .mapToInt(Entry::getKey)
                    .sum() * winningNumber;
        }
    }

    static class Game {
        int[] numbers;
        List<Board> boards = new ArrayList<>();

        Game(int[] numbers) {
            this.numbers = numbers;
        }

        void add(Board board) {
            boards.add(board);
        }

        int play(boolean playToWin) {
            int lastScore = -1;
            for (int number : numbers) {
                for (Board board : boards) {
                    if (!board.completed && board.call(number)) {
                        lastScore = board.score(number);
                        if (playToWin) {
                            return lastScore;
                        }
                    }
                }
            }
            return lastScore;
        }
    }
}
