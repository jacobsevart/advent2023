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
    public void testPartTwo() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day10.txt");
        assertNotNull(txtFile);

        var maze = new PipeMaze(new Scanner(txtFile));
        var path = maze.cyclePath();
        String cycle = maze.draw(path);

        maze = new PipeMaze(new Scanner(cycle));
        assertEquals(567, maze.partTwo());
    }

    @Test
    public void testCountContained() {
        String testInput = """
                ...........
                .S-------7.
                .|F-----7|.
                .||.....||.
                .||.....||.
                .|L-7.F-J|.
                .|..|.|..|.
                .L--J.L--J.
                ...........""";

        var maze = new PipeMaze(new Scanner(testInput));
        var path = maze.cyclePath();
        maze.floodFill(path, new PipeMaze.Coordinate(0, 0));
        System.out.println(maze.draw());
        assertEquals(4, maze.countCharater('.'));
    }

    @Test
    public void testCountContainedClosed() {
        String testInput = """
                ..........
                .S------7.
                .|F----7|.
                .||....||.
                .||....||.
                .|L-7F-J|.
                .|..||..|.
                .L--JL--J.
                ..........
                """;

        var maze = new PipeMaze(new Scanner(testInput));
        assertEquals(4, maze.partTwo());
    }

    @Test
    public void testCountContainsOpenBig() {
        String testInput = """
                .F----7F7F7F7F-7....
                .|F--7||||||||FJ....
                .||.FJ||||||||L7....
                FJL7L7LJLJ||LJ.L-7..
                L--J.L7...LJS7F-7L7.
                ....F-J..F7FJ|L7L7L7
                ....L7.F7||L7|.L7L7|
                .....|FJLJ|FJ|F7|.LJ
                ....FJL-7.||.||||...
                ....L---J.LJ.LJLJ...""";

        var maze = new PipeMaze(new Scanner(testInput));
        assertEquals(8, maze.partTwo());
    }
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