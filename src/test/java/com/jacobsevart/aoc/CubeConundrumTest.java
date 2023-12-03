package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CubeConundrumTest {

    @Test
    public void testPart1() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day2.txt");
        int answer = CubeConundrum.sum(new Scanner(txtFile));
        assertEquals(2720, answer);
    }

    @Test
    public void testPart2() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day2.txt");
        long answer = CubeConundrum.power(new Scanner(txtFile));
        assertEquals(71535, answer);
    }

    @Test
    public void testParse() {
        CubeConundrum.Game got = CubeConundrum.parse("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green");
        assertEquals(new CubeConundrum.Game(4, 2, 6), got);
        assertTrue(CubeConundrum.isPossible(got, 12, 13, 14));
    }

    @Test
    public void testPossibiles() {
        record TestCase(String line, boolean possible) {
        }
        ;
        TestCase[] cases = {
                new TestCase("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green", true),
                new TestCase("Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue", true),
                new TestCase("Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red", false),
                new TestCase("Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red", false),
                new TestCase("Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green", true),
        };

        for (TestCase c : cases) {
            assertEquals(c.possible, CubeConundrum.isPossible(CubeConundrum.parse(c.line), 12, 13, 14));
        }
    }
}