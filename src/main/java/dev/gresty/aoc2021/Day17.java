package dev.gresty.aoc2021;

import dev.gresty.aoc2021.Utils.IntPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static dev.gresty.aoc2021.Utils.withString;
import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

public class Day17 {

    public static void main(String[] args) {
        withString(Day17::part1, "day17");
        withString(Day17::part2, "day17");
    }

    public static int part1(String input) {
        return new Targeter(input).maxHeight();
    }

    public static int part2(String input) {
        return new Targeter(input).validInitialVelocityCount();
    }

    static class Targeter {
        final int xmin;
        final int xmax;
        final int ymin;
        final int ymax;

        Targeter (String input) {
            int i1 = input.indexOf('=');
            int i2 = input.indexOf('.');
            int i3 = input.indexOf(',');
            int i4 = input.lastIndexOf('=');
            int i5 = input.lastIndexOf('.');
            xmin = Integer.parseInt(input.substring(i1 + 1, i2));
            xmax = Integer.parseInt(input.substring(i2 + 2, i3));
            ymin = Integer.parseInt(input.substring(i4 + 1, i5 - 1));
            ymax = Integer.parseInt(input.substring(i5 + 1));
        }

        int maxHeight() {
            return (maxYvel() * (maxYvel() + 1)) / 2;
        }

        int validInitialVelocityCount() {
            Map<Integer, List<Integer>> onTargetX = onTargetXiToStep();
            Map<Integer, List<Integer>> onTargetY = onTargetYiToStep();
            Set<IntPair> onTarget = onTargetInitialVelocities(onTargetX, onTargetY);
            return onTarget.size();
        }

        Map<Integer, List<Integer>> onTargetXiToStep() {
            Map<Integer, List<Integer>> onTargetXvels = new HashMap<>();
            for (int xi = minXvel(); xi <= maxXvel(); xi++) {
                List<Integer> onTargetSteps = onTargetXSteps(xi);
                if (onTargetSteps.size() > 0) {
                    onTargetXvels.put(xi, onTargetSteps);
                }
            }
            return onTargetXvels;
        }

        Map<Integer, List<Integer>> onTargetYiToStep() {
            Map<Integer, List<Integer>> onTargetYvels = new HashMap<>();
            for (int yi = minYvel(); yi <= maxYvel(); yi++) {
                List<Integer> onTargetSteps = onTargetYSteps(yi);
                if (onTargetSteps.size() > 0) {
                    onTargetYvels.put(yi, onTargetSteps);
                }
            }
            return onTargetYvels;
        }

        List<Integer> onTargetXSteps(int xi) {
            List<Integer> onTarget = new ArrayList<>();
            int x = 0;
            int dx = xi;
            int step = 0;
            while (dx > 0) {
                if (x >= xmin && x <= xmax) {
                    onTarget.add(step);
                }
                x += dx;
                dx -= 1;
                step++;
            }
            if (x >= xmin && x <= xmax) {
                onTarget.add(-step); // All steps from this one are valid X steps. Because X doesn't change.
            }
            return onTarget;
        }

        List<Integer> onTargetYSteps(int yi) {
            List<Integer> onTarget = new ArrayList<>();
            int y = 0;
            int dy = yi;
            int step = 0;
            while (y >= ymin) {
                if (y <= ymax) {
                    onTarget.add(step);
                }
                y += dy;
                dy -= 1;
                step++;
            }
            return onTarget;
        }

        Set<IntPair> onTargetInitialVelocities(Map<Integer, List<Integer>> onTargetX, Map<Integer, List<Integer>> onTargetY) {
            Set<IntPair> onTarget = new HashSet<>();
            for (Entry<Integer, List<Integer>> yEntry : onTargetY.entrySet()) {
                int yi = yEntry.getKey();
                List<Integer> ySteps = yEntry.getValue();

                for (Entry<Integer, List<Integer>> xEntry : onTargetX.entrySet()) {
                    int xi = xEntry.getKey();
                    List<Integer> xSteps = xEntry.getValue();
                    int finalXstep = xSteps.get(xSteps.size() - 1);

                    for (int yStep : ySteps) {
                        if (xSteps.contains(yStep) || (finalXstep < 0 && yStep >= -finalXstep )) {
                            onTarget.add(IntPair.xy(xi, yi));
                        }
                    }
                }
            }
            return onTarget;
        }

        int maxYvel() {
            return -ymin - 1;
        }

        int minYvel() {
            return ymin;
        }

        int maxXvel() {
            return xmax;
        }

        int minXvel() {
            // x(x+1) / 2 = xmin  =>  x^2 + x - 2 * xmin = 0  =>  x = (-1 +/- sqrt(1 + 8xmin)) / 2
            return (int) ceil((sqrt(1 + 8 * xmin) - 1) / 2);
        }
    }
}
