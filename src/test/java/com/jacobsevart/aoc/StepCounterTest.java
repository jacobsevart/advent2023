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
    public void testPartOneSmall() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day21.txt");
        assertNotNull(txtFile);

        var counter = new StepCounter(new Scanner(txtFile));
        assertEquals(3572, counter.walk(64));
    }

}