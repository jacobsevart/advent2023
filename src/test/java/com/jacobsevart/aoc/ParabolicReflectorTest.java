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
    public void testCycle() {
        var reflector = new ParabolicReflector(new Scanner(testInput));
        reflector.cycle();

        String expectedOneCycle = """
                .....#....
                ....#...O#
                ...OO##...
                .OO#......
                .....OOO#.
                .O#...O#.#
                ....O#....
                ......OOOO
                #...O###..
                #..OO#....""";

        assertEquals(expectedOneCycle, reflector.render());

        String expectedTwoCycle = """
                .....#....
                ....#...O#
                .....##...
                ..O#......
                .....OOO#.
                .O#...O#.#
                ....O#...O
                .......OOO
                #..OO###..
                #.OOO#...O""";

        reflector.cycle();
        assertEquals(expectedTwoCycle, reflector.render());

        String expectedThreeCycle = """
                .....#....
                ....#...O#
                .....##...
                ..O#......
                .....OOO#.
                .O#...O#.#
                ....O#...O
                .......OOO
                #...O###.O
                #.OOO#...O""";

        reflector.cycle();
        assertEquals(expectedThreeCycle, reflector.render());
    }

    @Test
    public void testPartTwoSmall() {
        assertEquals(64, ParabolicReflector.partTwo(new Scanner(testInput)));
    }

    @Test
    public void testCycleSmall() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day14.txt");
        assertNotNull(txtFile);

        var reflector = new ParabolicReflector(new Scanner(txtFile));

        for (int i = 0; i < 250; i++) {
            reflector.cycle();
            System.out.printf("%2d: %5d\n", i, reflector.totalLoad());
        }
    }

    @Test
    public void testCycleSymmetric() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day14.txt");
        assertNotNull(txtFile);
        var grid = ParabolicReflector.parse(new Scanner(txtFile));

        ParabolicReflector reflector = new ParabolicReflector(grid);
        reflector.tiltWest();
        String referenceWest = reflector.render();
        reflector.tiltEast();
        reflector.tiltWest();
        assertEquals(referenceWest, reflector.render());

        reflector = new ParabolicReflector(grid);
        reflector.tiltEast();
        String referenceEast = reflector.render();
        reflector.tiltWest();
        reflector.tiltEast();;
        assertEquals(referenceEast, reflector.render());

        reflector = new ParabolicReflector(grid);
        reflector.tiltSouth();
        String referenceSouth = reflector.render();
        reflector.tiltNorth();
        reflector.tiltSouth();;
        assertEquals(referenceSouth, reflector.render());

        reflector = new ParabolicReflector(grid);
        reflector.tiltNorth();
        String referenceNorth = reflector.render();
        System.out.println(referenceNorth);
        reflector.tiltSouth();
        reflector.tiltNorth();
        assertEquals(referenceNorth, reflector.render());
    }

    @Test
    public void testCycleIdempotent() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day14.txt");
        assertNotNull(txtFile);
        var grid = ParabolicReflector.parse(new Scanner(txtFile));

        ParabolicReflector reflector = new ParabolicReflector(grid);
        reflector.tiltWest();
        String referenceWest = reflector.render();
        reflector.tiltWest();
        assertEquals(referenceWest, reflector.render());

        reflector = new ParabolicReflector(grid);
        reflector.tiltEast();
        String referenceEast = reflector.render();
        reflector.tiltEast();;
        assertEquals(referenceEast, reflector.render());

        reflector = new ParabolicReflector(grid);
        reflector.tiltSouth();
        String referenceSouth = reflector.render();
        reflector.tiltSouth();;
        assertEquals(referenceSouth, reflector.render());

        reflector = new ParabolicReflector(grid);
        reflector.tiltNorth();
        String referenceNorth = reflector.render();
        reflector.tiltNorth();
        assertEquals(referenceNorth, reflector.render());
    }

    @Test
    public void testPartTwoLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day14.txt");
        assertNotNull(txtFile);

        // I'm not dropping any objects
        // The tilt operations are symmetric
        // The tilt operations are idempotent
        // Fairly high confidence in the load function since already worked on this size input

        assertEquals(0, ParabolicReflector.partTwo(new Scanner(txtFile)));
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