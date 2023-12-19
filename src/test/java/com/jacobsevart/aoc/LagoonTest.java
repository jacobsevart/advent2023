package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class LagoonTest {
    String testInput = """
            R 6 (#70c710)
            D 5 (#0dc571)
            L 2 (#5713f0)
            D 2 (#d2c081)
            R 2 (#59c680)
            D 2 (#411b91)
            L 5 (#8ceee2)
            U 2 (#caa173)
            L 1 (#1b58a2)
            U 2 (#caa171)
            R 2 (#7807d2)
            U 3 (#a77fa3)
            L 2 (#015232)
            U 2 (#7a21e3)""";


    @Test
    public void testPartOneShoelace() {
        var lagoon = Lagoon.ofPartOne(new Scanner(testInput));
        var path = lagoon.trace();
        assertEquals(62, lagoon.area());
    }

    @Test
    public void testPartTwoShoelace() {
        var lagoon = Lagoon.ofPartTwo(new Scanner(testInput));
        assertEquals(952408144115L, lagoon.area());
    }

    @Test
    public void testPartOneLargeShoelace() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day18.txt");
        assertNotNull(txtFile);
        var lagoon = Lagoon.ofPartOne(new Scanner(txtFile));
        assertEquals(56678, lagoon.area());
    }

    @Test
    public void testPartTwoLargeShoelace() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day18.txt");
        assertNotNull(txtFile);
        var lagoon = Lagoon.ofPartTwo(new Scanner(txtFile));
        assertEquals(79088855654037L, lagoon.area());
    }
}