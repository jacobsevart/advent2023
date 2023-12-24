package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LongWalkTest {
    String testInput = """
            #.#####################
            #.......#########...###
            #######.#########.#.###
            ###.....#.>.>.###.#.###
            ###v#####.#v#.###.#.###
            ###.>...#.#.#.....#...#
            ###v###.#.#.#########.#
            ###...#.#.#.......#...#
            #####.#.#.#######.#.###
            #.....#.#.#.......#...#
            #.#####.#.#.#########v#
            #.#...#...#...###...>.#
            #.#.#v#######v###.###v#
            #...#.>.#...>.>.#.###.#
            #####v#.#.###v#.#.###.#
            #.....#...#...#.#.#...#
            #.#########.###.#.#.###
            #...###...#...#...#.###
            ###.###.#.###v#####v###
            #...#...#.#.>.>.#.>.###
            #.###.###.#.###.#.#v###
            #.....###...###...#...#
            #####################.#""";

    @Test
    public void testPartOneSmall() {
        var walk = new LongWalk(new Scanner(testInput));
        assertEquals(94, walk.longestPath(walk::neighborsPartOne));
    }

    @Test
    public void testPartTwoSmall() {
        var walk = new LongWalk(new Scanner(testInput));
        assertEquals(154, walk.longestPath(walk::neighborsPartTwo));
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day23.txt");
        assertNotNull(txtFile);

        var walk = new LongWalk(new Scanner(txtFile));
        assertEquals(2318, walk.longestPath(walk::neighborsPartOne));
    }

    @Test
    public void testPartTwoLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day23.txt");
        assertNotNull(txtFile);

        var walk = new LongWalk(new Scanner(txtFile));
        assertEquals(6426, walk.longestPath(walk::neighborsPartTwo)); // 14 seconds
    }

}