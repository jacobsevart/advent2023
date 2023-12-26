package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionsTest {
    String testInputVertical = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.""";

    String testInputHorizontal = """
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#""";

    String testInput = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
                        
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#""";

    @Test
    public void testFindHorizontalReflection() {
        var map = Reflections.parse(new Scanner(testInputVertical));
        assertEquals(-1, Reflections.findHorizontalReflection(map, 0, -1));
        map = Reflections.parse(new Scanner(testInputHorizontal));
        assertEquals(3, Reflections.findHorizontalReflection(map, 0, -1));
    }

    @Test
    public void testFindVerticalReflection() {
        var map = Reflections.parse(new Scanner(testInputHorizontal));
        assertEquals(-1, Reflections.findVerticalReflection(map, 0, -1));

        var map2 = Reflections.parse(new Scanner(testInputVertical));
        assertEquals(4, Reflections.findVerticalReflection(map2, 0, -1));
    }

    @Test
    public void testPartOneSmall() {
        assertEquals(405, Reflections.partOne(new Scanner(testInput)));
    }

    @Test
    public void testPartTwoSmall() {
        assertEquals(400, Reflections.partTwo(new Scanner(testInput)));
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day13.txt");
        assertNotNull(txtFile);

        assertEquals(27502, Reflections.partOne(new Scanner(txtFile)));
    }

    @Test
    public void testPartTwoLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day13.txt");
        assertNotNull(txtFile);

        assertEquals(31947, Reflections.partTwo(new Scanner(txtFile)));
    }
}