package dev.gresty.aoc2021;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static dev.gresty.aoc2021.Utils.withStringIterator;

public class Day20 {

    public static void main(String[] args) {
        withStringIterator(Day20::part1, "day20");
        withStringIterator(Day20::part2, "day20");
    }

    public static int part1(Iterator<String> input) {
        return execute(input, 2);
    }

    public static long part2(Iterator<String> input) {
        return execute(input, 50);
    }

    private static int execute(Iterator<String> input, int enhancements) {
        ImageEnhancer enh = new ImageEnhancer(input.next());
        input.next();
        Image img = new Image(0);
        while (input.hasNext()) {
            img.add(input.next());
        }
        for (int i = 0; i < enhancements; i++) {
            img = enh.enhance(img);
        }
        return img.count1s();
    }




    static class ImageEnhancer {
        int[] algorithm;

        ImageEnhancer (String input) {
            algorithm = convertTo01(input);
        }

        Image enhance(Image image) {
            Image enhanced = new Image(image.defaultValue == 0 ? algo(0) : algo(511));
            for (int y = -1; y <= image.height(); y++) {
                int[] row = new int[image.width() + 2];
                for (int x = -1; x <= image.width(); x++) {
                    row[x + 1] = algo(image.group(x, y));
                }
                enhanced.add(row);
            }
            return enhanced;
        }

        int algo(int i) {
            return algorithm[i];
        }
    }

    static class Image {
        int defaultValue;
        List<int[]> lines = new ArrayList<>();

        Image(int defaultValue) {
            this.defaultValue = defaultValue;
        }

        void add(String line) {
            add(convertTo01(line));
        }

        void add(int[] line) {
            lines.add(line);
        }

        int height() {
            return lines.size();
        }

        int width() {
            return lines.get(0).length;
        }

        int group(int x, int y) {
            int group = 0;
            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    group = (group << 1) + val(x + dx, y + dy);
                }
            }
            return group;
        }

        int val(int x, int y) {
            if (x < 0 || y < 0 || x >= width() || y >= height()) return defaultValue;
            return lines.get(y)[x];
        }

        int count1s() {
            int sum = 0;
            for(int[] line : lines) {
                for (int val : line) {
                    sum += val;
                }
            }
            return sum;
        }

    }

    static int[] convertTo01(String input) {
        int[] output = new int[input.length()];
        for (int i = 0; i < input.length(); i++) {
            output[i] = input.charAt(i) == '#' ? 1 : 0;
        }
        return output;
    }
}
