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

        assertEquals(0, hwl.reach("AAA", "ZZZ"));
    }



}