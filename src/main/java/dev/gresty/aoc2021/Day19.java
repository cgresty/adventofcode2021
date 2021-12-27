package dev.gresty.aoc2021;

import dev.gresty.aoc2021.Utils.IntTriple;
import dev.gresty.aoc2021.Utils.Pair;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day19.Transforms.IDENTITY;
import static dev.gresty.aoc2021.Day19.Transforms.ROTATIONS;
import static dev.gresty.aoc2021.Day19.Transforms.difference;
import static dev.gresty.aoc2021.Day19.Transforms.distance;
import static dev.gresty.aoc2021.Day19.Transforms.inverse;
import static dev.gresty.aoc2021.Day19.Transforms.isEqual;
import static dev.gresty.aoc2021.Day19.Transforms.multiply;
import static dev.gresty.aoc2021.Day19.Transforms.nC2;
import static dev.gresty.aoc2021.Day19.Transforms.translation;
import static dev.gresty.aoc2021.Utils.withStrings;
import static java.lang.Math.abs;
import static java.util.Arrays.fill;

public class Day19 {

    public static void main(String[] args) {
        withStrings(Day19::part1, "day19");
        withStrings(Day19::part2, "day19");
    }

    public static long part1(Stream<String> input) {
        BeaconScannerPuzzle puzzle = loadPuzzle(input);
        return puzzle.solve();
    }

    public static long part2(Stream<String> input) {
        BeaconScannerPuzzle puzzle = loadPuzzle(input);
        puzzle.solve();
        return puzzle.maxScannerDistance();
    }

    static BeaconScannerPuzzle loadPuzzle(Stream<String> input) {
        String[] lines = input.toArray(String[]::new);
        BeaconScannerPuzzle puzzle = new BeaconScannerPuzzle();
        ScannerView scanner = null;
        int scannerNum = 0;
        for (String line : lines) {
            if (line.startsWith("---")) {
                scanner = new ScannerView(scannerNum++);
                puzzle.add(scanner);
            } else if (line.length() > 0 && scanner != null) {
                scanner.addBeacon(IntTriple.of(line));
            }
        }
        return puzzle;
    }

    static class BeaconScannerPuzzle {
        List<ScannerView> scanners = new ArrayList<>();

        void add(ScannerView scanner) {
            scanners.add(scanner);
        }

        int solve() {
            scanners.forEach(ScannerView::calculateDistances);
            Set<Pair<ScannerView, ScannerView>> relatedPairs = matchDistances(nC2(12));
            updateTransforms(relatedPairs);
            return applyTransforms().size();
        }

        private Set<Pair<ScannerView, ScannerView>> matchDistances(int threshold) {
            Set<Pair<ScannerView, ScannerView>> relatedPairs = new HashSet<>();
            for (int i = 0; i < scanners.size() - 1; i++) {
                ScannerView scannerA = scanners.get(i);
                for (int j = i + 1; j < scanners.size(); j++) {
                    ScannerView scannerB = scanners.get(j);
                    if (scannerA.matchDistances(scannerB, threshold)) {
                        relatedPairs.add(Pair.of(scannerA, scannerB));
                    }
                }
            }
            return relatedPairs;
        }

        private void updateTransforms(Set<Pair<ScannerView, ScannerView>> relatedPairs) {
            updateTransforms(scanners.get(0), IDENTITY, relatedPairs, new HashSet<>());
        }

        private void updateTransforms(ScannerView scanner, int[][] transform, Set<Pair<ScannerView, ScannerView>> relatedPairs, Set<ScannerView> handled) {
            handled.add(scanner);
            relatedPairs.stream()
                    .filter(pair -> pair.a.equals(scanner) || pair.b.equals(scanner))
                    .map(pair -> pair.a.equals(scanner) ?
                            Pair.of(pair.b, pair.a.scannerTransforms.get(pair.b)) :
                            Pair.of(pair.a, inverse(pair.a.scannerTransforms.get(pair.b))))
                    .forEach(s -> {
                        ScannerView to = s.a;
                        int[][] toTransform = s.b;
                        if (!handled.contains(to)) {
                            int[][] newTransform = multiply(toTransform, transform);
                            to.transformFrom0 = newTransform;
                            updateTransforms(to, newTransform, relatedPairs, handled);
                        }
                    });
        }

        private Set<IntTriple> applyTransforms() {
            Set<IntTriple> beacons = new HashSet<>();
            for (ScannerView scanner : scanners) {
                scanner.transform();
                beacons.addAll(scanner.beaconsFrom0);
            }
            return beacons;
        }

        int maxScannerDistance() {
            int max = 0;
            for (int i = 0; i < scanners.size() - 1; i++) {
                for (int j = i; j < scanners.size(); j++) {
                    max = Math.max(max, distance(scanners.get(i).originFrom0.toArray(), scanners.get(j).originFrom0.toArray()));
                }
            }
            return max;
        }
    }

    @EqualsAndHashCode(of = "number")
    static class ScannerView {
        int number;
        List<int[]> beacons = new ArrayList<>();
        int[] distances;
        Map<Integer, Pair<int[], int[]>> beaconsAtDistance = new HashMap<>();
        int[] matchFlyweight;
        Map<ScannerView, List<Integer>> matchingDistancesByScanner = new HashMap<>();
        Map<ScannerView, int[][]> scannerTransforms = new HashMap<>();
        int[][] transformFrom0 = IDENTITY;
        IntTriple originFrom0 = IntTriple.origin();
        Set<IntTriple> beaconsFrom0;

        ScannerView(int num) {
            this.number = num;
        }

        void addBeacon(IntTriple beacon) {
            beacons.add(beacon.toArray());
        }

        void calculateDistances() {
            distances = new int[nC2(beacons.size())];
            int di = 0;
            for (int i = 0; i < beacons.size() - 1; i++) {
                for (int j = i + 1; j < beacons.size(); j++) {
                    distances[di] = distance(beacons.get(i), beacons.get(j));
                    beaconsAtDistance.put(distances[di], Pair.of(beacons.get(i), beacons.get(j)));
                    di++;
                }
            }
            matchFlyweight = new int[distances.length];
        }

        boolean matchDistances(ScannerView other, int threshold) {
            List<Integer> matchingDistances = new ArrayList<>();
            fill(matchFlyweight, 0);
            for (int i = 0; i < other.distances.length; i++) {
                for (int j = 0; j < distances.length; j++) {
                    if (other.distances[i] == distances[j] && matchFlyweight[j] == 0) {
                        matchFlyweight[j] = 1;
                        matchingDistances.add(distances[j]);
                        break;
                    }
                }
            }

            if (matchingDistances.size() >= threshold) {
                matchingDistancesByScanner.put(other, matchingDistances);
                relateTo(other);
                return true;
            }

            return false;
        }

        void relateTo(ScannerView other) {
            for(int distance : matchingDistancesByScanner.get(other)) {
                if (distance == 0) return;
                Pair<int[], int[]> myBeacons = beaconsAtDistance.get(distance);
                Pair<int[], int[]> otherBeacons = other.beaconsAtDistance.get(distance);

                int[] myDiff = difference(myBeacons.b, myBeacons.a);

                for (int r = 0; r < 24; r++) {
                    int[] otherARotated = multiply(otherBeacons.a, ROTATIONS[r]);
                    int[] otherBRotated = multiply(otherBeacons.b, ROTATIONS[r]);
                    int[] otherDiff = difference(otherBRotated, otherARotated);
                    if (isEqual(myDiff, otherDiff)) {
                        int[] t = difference(myBeacons.a, otherARotated);
                        int[][] transform = multiply(ROTATIONS[r], translation(t[0], t[1], t[2]));
                        if (verifyAtLeast12PointsMatch(other, transform)) {
                            scannerTransforms.put(other, transform);
                            return;
                        }
                    }
                }
            }
        }

        boolean verifyAtLeast12PointsMatch(ScannerView other, int[][] transform) {
            long matches = other.beacons.stream()
                    .map(b -> multiply(b, transform))
                    .filter(b -> beacons.stream()
                            .filter(a -> isEqual(a, b))
                            .count() == 1)
                    .count();
            return matches >= 12;
        }

        void transform() {
            beaconsFrom0 = new HashSet<>();
            for (int[] beacon : beacons) {
                beaconsFrom0.add(IntTriple.of(multiply(beacon, transformFrom0)));
            }

            originFrom0 = IntTriple.of(multiply(new int[] {0, 0, 0, 1}, transformFrom0));
        }
    }


    static class Transforms {

        static final int[][] IDENTITY = new int[][] {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
        static final int[][] ROTATE_X = new int[][] {{1, 0, 0, 0}, {0, 0, -1, 0}, {0, 1, 0, 0}, {0, 0, 0, 1}};
        static final int[][] ROTATE_Y = new int[][] {{0, 0, -1, 0}, {0, 1, 0, 0}, {1, 0, 0, 0}, {0, 0, 0, 1}};
        static final int[][] ROTATE_Z = new int[][] {{0, -1, 0, 0}, {1, 0, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};

        static final int[][][] ROTATIONS = new int[24][][];
        static {
            ROTATIONS[0] = IDENTITY;
            ROTATIONS[1] = multiply(ROTATIONS[0], ROTATE_Z);
            ROTATIONS[2] = multiply(ROTATIONS[1], ROTATE_Z);
            ROTATIONS[3] = multiply(ROTATIONS[2], ROTATE_Z);

            ROTATIONS[4] = ROTATE_X;
            ROTATIONS[5] = multiply(ROTATIONS[4], ROTATE_Z);
            ROTATIONS[6] = multiply(ROTATIONS[5], ROTATE_Z);
            ROTATIONS[7] = multiply(ROTATIONS[6], ROTATE_Z);

            ROTATIONS[8] = multiply(ROTATIONS[4], ROTATE_X);
            ROTATIONS[9] = multiply(ROTATIONS[8], ROTATE_Z);
            ROTATIONS[10] = multiply(ROTATIONS[9], ROTATE_Z);
            ROTATIONS[11] = multiply(ROTATIONS[10], ROTATE_Z);

            ROTATIONS[12] = multiply(ROTATIONS[8], ROTATE_X);
            ROTATIONS[13] = multiply(ROTATIONS[12], ROTATE_Z);
            ROTATIONS[14] = multiply(ROTATIONS[13], ROTATE_Z);
            ROTATIONS[15] = multiply(ROTATIONS[14], ROTATE_Z);

            ROTATIONS[16] = ROTATE_Y;
            ROTATIONS[17] = multiply(ROTATIONS[16], ROTATE_Z);
            ROTATIONS[18] = multiply(ROTATIONS[17], ROTATE_Z);
            ROTATIONS[19] = multiply(ROTATIONS[18], ROTATE_Z);

            ROTATIONS[20] = multiply(ROTATE_Y, multiply(ROTATE_Y, ROTATE_Y));
            ROTATIONS[21] = multiply(ROTATIONS[20], ROTATE_Z);
            ROTATIONS[22] = multiply(ROTATIONS[21], ROTATE_Z);
            ROTATIONS[23] = multiply(ROTATIONS[22], ROTATE_Z);
        }

        static int[][] translation(int dx, int dy, int dz) {
            return new int[][] {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {dx, dy, dz, 1}};
        }

        static int[][] multiply(int[][] a, int[][] b) {
            int[][] x = new int[a.length][a.length];
            for (int r = 0; r < a.length; r++) {
                for (int c = 0; c < b.length; c++) {
                    x[r][c] = multiply(a[r], col(b, c));
                }
            }
            return x;
        }

        static int[] multiply(int[][] a, int[] b) {
            int[] x = new int[a.length];
            for (int i = 0; i < a.length; i++) {
                x[i] = multiply(a[i], b);
            }
            return x;
        }

        static int[] multiply(int[] a, int[][] b) {
            int[] x = new int[a.length];
            for (int i = 0; i < a.length; i++) {
                x[i] = multiply(a, col(b, i));
            }
            return x;
        }

        static int multiply(int[] a, int[] b) {
            int result = 0;
            for (int i = 0; i < a.length; i++) {
                result += a[i] * b[i];
            }
            return result;
        }

        static int[] col(int[][] a, int c) {
            int[] col = new int[a.length];
            for (int i = 0; i < a.length; i++) {
                col[i] = a[i][c];
            }
            return col;
        }

        static int distance(int[] a, int[] b) {
            int d = 0;
            for (int i = 0; i < a.length; i++) {
                d += abs(a[i] - b[i]);
            }
            return d;
        }

        static int[] difference(int[] a, int[] b) {
            int[] diff = new int[a.length];
            for (int i = 0; i < a.length; i++) {
                diff[i] = a[i] - b[i];
            }
            return diff;
        }

        static boolean isEqual(int[] a, int[] b) {
            for (int i = 0; i < a.length; i++) {
                if (a[i] != b[i]) return false;
            }
            return true;
        }

        static int[][] inverse(int[][] a) {
            int[][] rT = new int[][]{
                    {a[0][0], a[1][0], a[2][0]},
                    {a[0][1], a[1][1], a[2][1]},
                    {a[0][2], a[1][2], a[2][2]}
            };
            int[] t = new int[]{a[3][0], a[3][1], a[3][2]};
            int[] tNew = multiply(t, rT);
            return new int[][]{
                    {rT[0][0], rT[0][1], rT[0][2], 0},
                    {rT[1][0], rT[1][1], rT[1][2], 0},
                    {rT[2][0], rT[2][1], rT[2][2], 0},
                    {-tNew[0], -tNew[1], -tNew[2], 1},
            };
        }

        static int nC2(int n)  {
            return n * (n - 1) / 2;
        }
    }
}
