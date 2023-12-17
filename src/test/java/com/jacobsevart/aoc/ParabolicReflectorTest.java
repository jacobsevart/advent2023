package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ParabolicReflectorTest {
    String testInput = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....""";
    @Test
    public void testParse() {
        var reflector = new ParabolicReflector(new Scanner(testInput));
        reflector.render();
    }

    @Test
    public void testPartOneSmall() {
        var reflector = new ParabolicReflector(new Scanner(testInput));
        reflector.tiltNorth();

        String expected = """
                OOOO.#.O..
                OO..#....#
                OO..O##..O
                O..#.OO...
                ........#.
                ..#....#.#
                ..O..#.O.O
                ..O.......
                #....###..
                #....#....""";

        assertEquals(expected, reflector.render());
        assertEquals(136, reflector.totalLoad());
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day14.txt");
        assertNotNull(txtFile);

        var reflector = new ParabolicReflector(new Scanner(txtFile));
        reflector.tiltNorth();

        assertEquals(105784, reflector.totalLoad());
    }

}