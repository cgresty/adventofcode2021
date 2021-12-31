package dev.gresty.aoc2021;

import java.util.Arrays;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;

public class Day21 {

    static int[] UNIVERSES_PER_ROLL = new int[] {0, 0, 0, 1, 3, 6, 7, 6, 3, 1};

    public static void main(String[] args) {
        withStrings(Day21::part1, "day21");
        withStrings(Day21::part2, "day21");
    }

    public static int part1(Stream<String> input) {
        return play(loadPlayers(input, 1000));
    }

    public static long part2(Stream<String> input) {
        return playAllUniverses(loadPlayers(input, 21));
    }

    static Player[] loadPlayers(Stream<String> input, int target) {
        return input
                .mapToInt(line -> Integer.parseInt(line.substring(line.lastIndexOf(' ') + 1)))
                .mapToObj(pos -> new Player(pos, target))
                .toArray(Player[]::new);
    }

    static int play(Player[] players) {
        DeterministicDie die = new DeterministicDie();
        while(true) {
            for (int i = 0; i < players.length; i++) {
                if (players[i].play(die.next3sum())) {
                    return players[1 - i].score * die.rolls;
                }
            }
        }
    }

    static long playAllUniverses(Player[] players) {
        return playNextRound(players, 0, 1);

    }

    static long playNextRound(Player[] players, int playerToRoll, long universes) {
        for (int roll = 3; roll <= 9; roll++) {
            Player player = players[playerToRoll];
            long universesWithRoll = universes * UNIVERSES_PER_ROLL[roll];
            if (players[playerToRoll].play(roll)) {
                players[playerToRoll].wins += universesWithRoll;
            } else {
                playNextRound(players, 1 - playerToRoll, universesWithRoll);
            }
            player.unplay(roll);
        }
        return Arrays.stream(players).mapToLong(p -> p.wins).max().getAsLong();
    }

    static class Player {
        int position;
        int score;
        int target;
        long wins;

        Player(int position, int target) {
            this.position = position - 1;
            this.target = target;
        }

        boolean play(int roll) {
            position += roll;
            position = position % 10;
            score += position + 1;
            return score >= target;
        }

        void unplay(int roll) {
            score -= position + 1;
            position -= roll;
            position = (position + 10) % 10;
        }
    }

    static class DeterministicDie {
        int rolls = 0;
        int value = 0;

        public int next3sum() {
            return next() + next() + next();
        }

        public int next() {
            rolls++;
            return value++%100 + 1;
        }
    }
}
