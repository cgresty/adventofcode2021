package dev.gresty.aoc2021;

import dev.gresty.aoc2021.Day19.BeaconScannerPuzzle;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static dev.gresty.aoc2021.Day19.Transforms.IDENTITY;
import static dev.gresty.aoc2021.Day19.Transforms.ROTATE_X;
import static dev.gresty.aoc2021.Day19.Transforms.ROTATE_Y;
import static dev.gresty.aoc2021.Day19.Transforms.ROTATE_Z;
import static dev.gresty.aoc2021.Day19.Transforms.inverse;
import static dev.gresty.aoc2021.Day19.Transforms.multiply;
import static dev.gresty.aoc2021.Day19.Transforms.translation;
import static dev.gresty.aoc2021.Day19.loadPuzzle;
import static dev.gresty.aoc2021.Day19.part1;
import static dev.gresty.aoc2021.Day19.part2;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

class Day19Test {

    @Test
    public void testPart1() {
        assertThat(part1(input())).isEqualTo(79);
    }

    @Test
    public void testPart1_01only() {
        assertThat(part1(input01())).isEqualTo(25 + 25 - 12);
    }

    @Test
    public void testPart1_14only() {
        assertThat(part1(input14())).isEqualTo(25 + 26 - 12);
    }


    @Test
    public void testPart2() {
        assertThat(part2(input())).isEqualTo(3621);
    }

    @Test
    public void testDistances() {
        BeaconScannerPuzzle puzzle = loadPuzzle(input());
        puzzle.solve();
    }

    @Test
    public void testTransform() {
        int[] translated = multiply(new int[] {2, 4, 6, 1}, translation(10, 20, 30));
        assertThat(translated).containsExactly(12, 24, 36, 1);

        int[] rotated = multiply(new int[] {2, 4, 6, 1}, ROTATE_X);
        assertThat(rotated).containsExactly(2, 6, -4, 1);

        int[] rAndT = multiply(new int[] {2, 4, 6, 1}, multiply(ROTATE_X, translation(10, 20, 30)));
        assertThat(rAndT).containsExactly(12, 26, 26, 1);

        int[] rAndT2 = multiply(multiply(new int[] {2, 4, 6, 1}, ROTATE_X), translation(10, 20, 30));
        assertThat(rAndT2).containsExactly(12, 26, 26, 1);
    }

    @Test
    public void testInverse() {
        int[][] translate = translation(2, -4, 6);
        testInverse(translate);
        testInverse(ROTATE_X);
        testInverse(ROTATE_Y);
        testInverse(ROTATE_Z);
        testInverse(multiply(ROTATE_X, translate));
        testInverse(multiply(multiply(ROTATE_X, ROTATE_Y), translate));
    }

    void testInverse(int[][] a) {
        int[][] inverse = inverse(a);
        isIdentity(multiply(a, inverse));
        isIdentity(multiply(inverse, a));
    }

    void isIdentity(int[][] a) {
        range(0, a.length).forEach(i -> assertThat(a[i]).isEqualTo(IDENTITY[i]));
    }

    private Stream<String> input() {
        return """
                --- scanner 0 ---
                404,-588,-901
                528,-643,409
                -838,591,734
                390,-675,-793
                -537,-823,-458
                -485,-357,347
                -345,-311,381
                -661,-816,-575
                -876,649,763
                -618,-824,-621
                553,345,-567
                474,580,667
                -447,-329,318
                -584,868,-557
                544,-627,-890
                564,392,-477
                455,729,728
                -892,524,684
                -689,845,-530
                423,-701,434
                7,-33,-71
                630,319,-379
                443,580,662
                -789,900,-551
                459,-707,401
                                
                --- scanner 1 ---
                686,422,578
                605,423,415
                515,917,-361
                -336,658,858
                95,138,22
                -476,619,847
                -340,-569,-846
                567,-361,727
                -460,603,-452
                669,-402,600
                729,430,532
                -500,-761,534
                -322,571,750
                -466,-666,-811
                -429,-592,574
                -355,545,-477
                703,-491,-529
                -328,-685,520
                413,935,-424
                -391,539,-444
                586,-435,557
                -364,-763,-893
                807,-499,-711
                755,-354,-619
                553,889,-390
                                
                --- scanner 2 ---
                649,640,665
                682,-795,504
                -784,533,-524
                -644,584,-595
                -588,-843,648
                -30,6,44
                -674,560,763
                500,723,-460
                609,671,-379
                -555,-800,653
                -675,-892,-343
                697,-426,-610
                578,704,681
                493,664,-388
                -671,-858,530
                -667,343,800
                571,-461,-707
                -138,-166,112
                -889,563,-600
                646,-828,498
                640,759,510
                -630,509,768
                -681,-892,-333
                673,-379,-804
                -742,-814,-386
                577,-820,562
                                
                --- scanner 3 ---
                -589,542,597
                605,-692,669
                -500,565,-823
                -660,373,557
                -458,-679,-417
                -488,449,543
                -626,468,-788
                338,-750,-386
                528,-832,-391
                562,-778,733
                -938,-730,414
                543,643,-506
                -524,371,-870
                407,773,750
                -104,29,83
                378,-903,-323
                -778,-728,485
                426,699,580
                -438,-605,-362
                -469,-447,-387
                509,732,623
                647,635,-688
                -868,-804,481
                614,-800,639
                595,780,-596
                                
                --- scanner 4 ---
                727,592,562
                -293,-554,779
                441,611,-461
                -714,465,-776
                -743,427,-804
                -660,-479,-426
                832,-632,460
                927,-485,-438
                408,393,-506
                466,436,-512
                110,16,151
                -258,-428,682
                -393,719,612
                -211,-452,876
                808,-476,-593
                -575,615,604
                -485,667,467
                -680,325,-822
                -627,-443,-432
                872,-547,-609
                833,512,582
                807,604,487
                839,-516,451
                891,-625,532
                -652,-548,-490
                30,-46,-14
                """.lines();
    }

    private Stream<String> input01() {
        return """
                --- scanner 0 ---
                404,-588,-901
                528,-643,409
                -838,591,734
                390,-675,-793
                -537,-823,-458
                -485,-357,347
                -345,-311,381
                -661,-816,-575
                -876,649,763
                -618,-824,-621
                553,345,-567
                474,580,667
                -447,-329,318
                -584,868,-557
                544,-627,-890
                564,392,-477
                455,729,728
                -892,524,684
                -689,845,-530
                423,-701,434
                7,-33,-71
                630,319,-379
                443,580,662
                -789,900,-551
                459,-707,401
                                
                --- scanner 1 ---
                686,422,578
                605,423,415
                515,917,-361
                -336,658,858
                95,138,22
                -476,619,847
                -340,-569,-846
                567,-361,727
                -460,603,-452
                669,-402,600
                729,430,532
                -500,-761,534
                -322,571,750
                -466,-666,-811
                -429,-592,574
                -355,545,-477
                703,-491,-529
                -328,-685,520
                413,935,-424
                -391,539,-444
                586,-435,557
                -364,-763,-893
                807,-499,-711
                755,-354,-619
                553,889,-390
                """.lines();
    }

    private Stream<String> input14() {
        return """
                --- scanner 1 ---
                686,422,578
                605,423,415
                515,917,-361
                -336,658,858
                95,138,22
                -476,619,847
                -340,-569,-846
                567,-361,727
                -460,603,-452
                669,-402,600
                729,430,532
                -500,-761,534
                -322,571,750
                -466,-666,-811
                -429,-592,574
                -355,545,-477
                703,-491,-529
                -328,-685,520
                413,935,-424
                -391,539,-444
                586,-435,557
                -364,-763,-893
                807,-499,-711
                755,-354,-619
                553,889,-390
                                
                --- scanner 4 ---
                727,592,562
                -293,-554,779
                441,611,-461
                -714,465,-776
                -743,427,-804
                -660,-479,-426
                832,-632,460
                927,-485,-438
                408,393,-506
                466,436,-512
                110,16,151
                -258,-428,682
                -393,719,612
                -211,-452,876
                808,-476,-593
                -575,615,604
                -485,667,467
                -680,325,-822
                -627,-443,-432
                872,-547,-609
                833,512,582
                807,604,487
                839,-516,451
                891,-625,532
                -652,-548,-490
                30,-46,-14
                """.lines();
    }


}