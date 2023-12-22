package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class StepCounterTest {
    String testInput = """
            ...........
            .....###.#.
            .###.##..#.
            ..#.#...#..
            ....#.#....
            .##..S####.
            .##..#...#.
            .......##..
            .##.#.####.
            .##..##.##.
            ...........
            """;

    @Test
    public void testWalk() {
        var counter = new StepCounter(new Scanner(testInput));
        var got = counter.walk(6);
        assertEquals(16, got);
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day21.txt");
        assertNotNull(txtFile);

        var counter = new StepCounter(new Scanner(txtFile));
        assertEquals(3572, counter.walk(64));
    }

    @Test
    public void testFromAllEdges() {
        var counter = new StepCounter(new Scanner(testInput));
        var got = counter.fromAllEdges();

        System.out.println(got);
    }

    @Test
    public void testPartTwoSmall() {
        var counter = new StepCounter(new Scanner(testInput));
        assertEquals(16, counter.walkInfinite(6));
        assertEquals(50, counter.walkInfinite(10));
        assertEquals(1594, counter.walkInfinite(50));
        assertEquals(6536, counter.walkInfinite(100));
        assertEquals(167004, counter.walkInfinite(500));
    }

    @Test
    public void testPartTwoLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day21.txt");
        assertNotNull(txtFile);

        var counter = new StepCounter(new Scanner(txtFile));
        assertEquals(3648, counter.walkInfinite(65));
        assertEquals(32781, counter.walkInfinite(196));
        assertEquals(90972, counter.walkInfinite(327));
        assertEquals(178221, counter.walkInfinite(458));
        assertEquals(294528, counter.walkInfinite(589));

        // I have only a vague glimmer of why this works. Thanks to the
        // community here.

        // 3648 + 14604 x + 14529 x^2 at x=202300 -> 594606492802848
    }

}