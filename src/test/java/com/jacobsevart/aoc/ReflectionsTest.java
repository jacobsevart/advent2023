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
        assertEquals(-1, Reflections.findHorizontalReflection(map));
        map = Reflections.parse(new Scanner(testInputHorizontal));
        assertEquals(3, Reflections.findHorizontalReflection(map));
    }

    @Test
    public void testFindVerticalReflection() {
        var map = Reflections.parse(new Scanner(testInputHorizontal));
        assertEquals(-1, Reflections.findVerticalReflection(map));

        var map2 = Reflections.parse(new Scanner(testInputVertical));
        assertEquals(4, Reflections.findVerticalReflection(map2));
    }

    @Test
    public void testPartOneSmall() {
        assertEquals(405, Reflections.partOne(new Scanner(testInput)));
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day13.txt");
        assertNotNull(txtFile);

        assertEquals(0, Reflections.partOne(new Scanner(txtFile)));
    }
}