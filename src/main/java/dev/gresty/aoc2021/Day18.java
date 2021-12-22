package dev.gresty.aoc2021;

import java.util.function.Function;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;

public class Day18 {

    public static void main(String[] args) {
        withStrings(Day18::part1, "day18");
        withStrings(Day18::part2, "day18");
    }

    public static long part1(Stream<String> input) {
        SnailFishMaths maths = new SnailFishMaths();
        input.forEach(maths::add);
        return maths.root.magnitude();
    }

    public static long part2(Stream<String> input) {
        String[] inputs = input.toArray(String[]::new);
        SnailFishMaths maths = new SnailFishMaths();
        long max = 0;
        for (int i = 0; i < inputs.length; i++) {
            for (int j = 0; j < inputs.length; j++) {
                if (j == i) continue;
                maths.root = SnailFishMaths.parse(inputs[i]);
                maths.add(SnailFishMaths.parse(inputs[j]));
                long magnitude = maths.root.magnitude();
                max = Math.max(max, magnitude);
            }
        }
        return max;
    }

    static class SnailFishMaths {

        Node root;

        void add(String input) {
            Node node = parse(input);
            if (root == null) {
                root = node;
            } else {
                add(node);
            }
        }

        void add(Node node) {
            Node newRoot = new Node(null);
            newRoot.left = root;
            root.up = newRoot;
            newRoot.right = node;
            node.up = newRoot;
            root = newRoot;

            reduce();
        }

        void reduce() {
            boolean done = false;
            do {
                if (!explode()) {
                    if (!split()) {
                        done = true;
                    }
                }
            } while (!done);
        }

        boolean explode() {
            boolean[] exploded = new boolean[1];
            root.visitLR(new Function<>() {
                Node lastNumericNode;
                int rightVal = -1;

                @Override
                public Boolean apply(Node node) {
                    if (rightVal != -1) {
                        if (node.isLeaf) {
                            node.value += rightVal;
                            return false;
                        }
                    } else if (!node.isLeaf && node.depth() == 4) {
                        exploded[0] = true;
                        if (lastNumericNode != null) {
                            lastNumericNode.value += node.left.value;
                            lastNumericNode = null;
                        }
                        rightVal = node.right.value;
                        node.convertToZeroLeaf();
                    } else if (node.isLeaf) {
                        lastNumericNode = node;
                    }
                    return true;
                }
            });
            return exploded[0];
        }

        boolean split() {
            boolean[] split = new boolean[1];
            root.visitLR(node -> {
                if (node.isLeaf && node.value > 9) {
                    split[0] = true;
                    node.split();
                    return false;
                }
                return true;
            });
            return split[0];
        }

        static Node parse(String input) {
            Node root = null;
            Node current = null;
            int i = 0;
            while (i < input.length()) {
                char next = input.charAt(i);
                if (next == '[') {
                    Node node = new Node(current);
                    if (root == null) root = node;
                    if (current != null) {
                        if (current.left == null) {
                            current.left = node;
                        } else {
                            current.right = node;
                        }
                    }
                    current = node;
                } else if (next == ']') {
                    current = current.up;
                } else if (next >= '0' && next <= '9') {
                    Node node = new Node(current, next - '0');
                    if (current.left == null) {
                        current.left = node;
                    } else {
                        current.right = node;
                    }
                }
                i++;
            }
            return root;
        }

        @Override
        public String toString() {
            return root.toString();
        }
    }

    static class Node {
        boolean isLeaf;
        Node left;
        Node right;
        Node up;
        int value;

        Node(Node up) {
            this.up = up;
        }

        Node(Node up, int value) {
            this.isLeaf = true;
            this.up = up;
            this.value = value;
        }

        boolean visitLR(Function<Node, Boolean> visitor) {
            if (visitor.apply(this)) {
                if (!isLeaf) {
                    if (left.visitLR(visitor)) {
                        return right.visitLR(visitor);
                    }
                    return false;
                }
                return true;
            }
            return false;
        }

        int depth() {
            if (up == null) return 0;
            return up.depth() + 1;
        }

        void convertToZeroLeaf() {
            right = null;
            left = null;
            isLeaf = true;
            value = 0;
        }

        void split() {
            isLeaf = false;
            left = new Node(this, value / 2);
            right = new Node(this, value - left.value);
            value = 0;
        }

        long magnitude() {
            if (isLeaf) return value;
            return 3 * left.magnitude() + 2 * right.magnitude();
        }

        @Override
        public String toString() {
            if (isLeaf) return "" + value;
            return "[" + left + "," + right + "]";
        }
    }
}
