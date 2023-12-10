package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class HauntedWastelandTest {
    String testInput = """
            RL
                        
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)""";

    String testInputLoop = """
            LLR
                        
            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)""";

    String testInputSimultaenous = """
            LR
                        
            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)""";

    @Test
    public void testParse() {
        var hwl = new HauntedWasteland(new Scanner(testInput));

        hwl.nodeSpecs.values().forEach(System.out::println);
    }

    @Test
    public void testReach() {
        var hwl = new HauntedWasteland(new Scanner(testInput));
        assertEquals(2, hwl.reach("AAA", "ZZZ"));
    }

    @Test
    public void testReachLoop() {
        var hwl = new HauntedWasteland(new Scanner(testInputLoop));
        assertEquals(6, hwl.reach("AAA", "ZZZ"));
    }

    @Test
    public void testPartOne() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day8.txt");
        assertNotNull(txtFile);

        var hwl = new HauntedWasteland(new Scanner(txtFile));

        assertEquals(14257, hwl.reach("AAA", "ZZZ"));
    }

    @Test
    public void testPartTwoSmall() {
        var hwl = new HauntedWasteland(new Scanner(testInputSimultaenous));
        assertEquals(6, hwl.reachSimulatenous());
    }

    @Test
    public void testPartTwoLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day8.txt");
        assertNotNull(txtFile);

        var hwl = new HauntedWasteland(new Scanner(txtFile));
        assertEquals(16187743689077L, hwl.reachSimulatenous());
    }

    @Test
    public void testGcdLcm() {
        assertEquals(6, HauntedWasteland.gcd(48, 18));
        assertEquals(6, HauntedWasteland.lcm(2, 3));
    }
}