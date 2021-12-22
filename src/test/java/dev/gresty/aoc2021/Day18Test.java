package dev.gresty.aoc2021;

import dev.gresty.aoc2021.Day18.Node;
import dev.gresty.aoc2021.Day18.SnailFishMaths;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day18.part1;
import static dev.gresty.aoc2021.Day18.part2;
import static org.assertj.core.api.Assertions.assertThat;

class Day18Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(4140);
    }

    @Test
    public void testPart2() {
        assertThat(part2(input())).isEqualTo(3993);
    }

    @Test
    public void testParseAndToString() {
        assertThat(SnailFishMaths.parse("[1,2]").toString()).isEqualTo("[1,2]");
        assertThat(SnailFishMaths.parse("[[1,2],3]").toString()).isEqualTo("[[1,2],3]");
        assertThat(SnailFishMaths.parse("[9,[8,7]]").toString()).isEqualTo("[9,[8,7]]");
        assertThat(SnailFishMaths.parse("[[1,9],[8,5]]").toString()).isEqualTo("[[1,9],[8,5]]");
        assertThat(SnailFishMaths.parse("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]").toString()).isEqualTo("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]");
    }

    @Test
    public void testExplode() {
        performExplode("[[[[[9,8],1],2],3],4]","[[[[0,9],2],3],4]");
        performExplode("[7,[6,[5,[4,[3,2]]]]]", "[7,[6,[5,[7,0]]]]");
        performExplode("[[6,[5,[4,[3,2]]]],1]", "[[6,[5,[7,0]]],3]");
        performExplode("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]");
        performExplode("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[7,0]]]]");
    }

    private void performExplode(String before, String after) {
        SnailFishMaths maths = new SnailFishMaths();
        maths.add(before);
        assertThat(maths.explode()).isTrue();
        assertThat(maths.toString()).isEqualTo(after);
    }

    @Test
    public void testSplit() {
        performSplit(10,"[5,5]");
        performSplit(11,"[5,6]");
        performSplit(12,"[6,6]");
    }

    private void performSplit(int before, String after) {
        Node node = new Node(null, before);
        node.split();
        assertThat(node.toString()).isEqualTo(after);
    }

    @Test
    public void testReduce() {
        SnailFishMaths maths = new SnailFishMaths();
        maths.add("[[[[4,3],4],4],[7,[[8,4],9]]]");
        maths.add("[1,1]");
        assertThat(maths.toString()).isEqualTo("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]");
    }

    @Test
    public void testMagnitude() {
        performMagnitude("[[9,1],[1,9]]", 129);
        performMagnitude("[[1,2],[[3,4],5]]", 143);
        performMagnitude("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", 1384);
        performMagnitude("[[[[1,1],[2,2]],[3,3]],[4,4]]", 445);
        performMagnitude("[[[[3,0],[5,3]],[4,4]],[5,5]]", 791);
        performMagnitude("[[[[5,0],[7,4]],[5,5]],[6,6]]", 1137);
        performMagnitude("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]", 3488);
    }

    private void performMagnitude(String before, long after) {
        SnailFishMaths maths = new SnailFishMaths();
        maths.add(before);
        assertThat(maths.root.magnitude()).isEqualTo(after);
    }

    private Stream<String> input() {
        return """
                [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
                [[[5,[2,8]],4],[5,[[9,9],0]]]
                [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
                [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
                [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
                [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
                [[[[5,4],[7,7]],8],[[8,3],8]]
                [[9,3],[[9,9],[6,[4,9]]]]
                [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
                [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
                """.lines();
    }}