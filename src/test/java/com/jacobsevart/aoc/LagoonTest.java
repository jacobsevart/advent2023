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
    public void testConstructor() {
        var lagoon = new Lagoon(new Scanner(testInput));
        lagoon.sizeCanvas();
        lagoon.draw();

        String expected = """
                #######
                #.....#
                ###...#
                ..#...#
                ..#...#
                ###.###
                #...#..
                ##..###
                .#....#
                .######
                """;

        assertEquals(expected, lagoon.render());
        assertEquals(62, lagoon.fill());
        System.out.println(lagoon.render());
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day18.txt");
        assertNotNull(txtFile);
        var lagoon = new Lagoon(new Scanner(txtFile));
        lagoon.sizeCanvas();
        lagoon.draw();
        assertEquals(56678, lagoon.fill());
    }





}