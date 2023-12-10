package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.channels.Pipe;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PipeMazeTest {
    String testInputComplex = """
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.LJ""";

    String testInputSimple = """
            -L|F7
            7S-7|
            L|7||
            -L-J|
            L|-JF""";

    @Test
    public void testPartOne() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day10.txt");
        assertNotNull(txtFile);

        var maze = new PipeMaze(new Scanner(txtFile));
        assertEquals(6754, maze.partOne());
    }
    @Test
    public void testPartOneSmall() {
        var maze = new PipeMaze(new Scanner(testInputSimple));
        assertEquals(4, maze.partOne());
        maze = new PipeMaze(new Scanner(testInputComplex));
        assertEquals(8, maze.partOne());
    }
    @Test
    public void testCycleSimple() {
        var maze = new PipeMaze(new Scanner(testInputSimple));
        var path = maze.cyclePath();

        String expected = """
                .....
                .S-7.
                .|.|.
                .L-J.
                .....
                """;

        assertEquals(expected, maze.draw(path));
        assertEquals(8, path.size());
    }
    @Test
    public void testCycleComplex() {
        var maze = new PipeMaze(new Scanner(testInputComplex));
        var path = maze.cyclePath();

        String expected = """
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...
            """;

        assertEquals(expected, maze.draw(path));
        assertEquals(16, path.size());
    }

}