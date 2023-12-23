package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

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
    public void testLongestPath() {
        var walk = new LongWalk(new Scanner(testInput));

        LongWalk.Coordinate origin = new LongWalk.Coordinate(0, 1);
        LongWalk.Coordinate destination =  new LongWalk.Coordinate(walk.grid.size() - 1, walk.grid.get(0).size() - 2);

        var dag = walk.reduceDag(origin, destination);

        var longestPath = walk.longestPath(origin, destination, dag);
        System.out.println(walk.render());

        assertEquals(94, longestPath);
    }

    @Test
    public void testDagReductionSmall() {
        var walk = new LongWalk(new Scanner(testInput));
        LongWalk.Coordinate origin = new LongWalk.Coordinate(0, 1);
        LongWalk.Coordinate destination =  new LongWalk.Coordinate(walk.grid.size() - 1, walk.grid.get(0).size() - 2);

        var dag = walk.reduceDag(origin, destination);
        System.out.println(walk.render(dag));
        dag.entrySet().forEach(System.out::println);
    }

    @Test
    public void testDagReductionLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day23.txt");
        assertNotNull(txtFile);

        var walk = new LongWalk(new Scanner(txtFile));
        LongWalk.Coordinate origin = new LongWalk.Coordinate(0, 1);
        LongWalk.Coordinate destination =  new LongWalk.Coordinate(walk.grid.size() - 1, walk.grid.get(0).size() - 2);
        var dag = walk.reduceDag(origin, destination);
        dag.entrySet().forEach(System.out::println);
        System.out.println(walk.render(dag));
        System.out.println(dag.size());
    }

    @Test
    public void testLongestPathLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day23.txt");
        assertNotNull(txtFile);

        var walk = new LongWalk(new Scanner(txtFile));
        LongWalk.Coordinate origin = new LongWalk.Coordinate(0, 1);
        LongWalk.Coordinate destination =  new LongWalk.Coordinate(walk.grid.size() - 1, walk.grid.get(0).size() - 2);


        var dag = walk.reduceDag(origin, destination);
        var path = walk.longestPath(origin, destination, dag);
        assertEquals(2318, path);
        System.out.println(walk.render());
    }
}