package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Comparator;
import java.util.HashSet;
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
    public void testLongestPath() {
        var walk = new LongWalk(new Scanner(testInput));

        LongWalk.Coordinate origin = new LongWalk.Coordinate(0, 1);
        LongWalk.Coordinate destination =  new LongWalk.Coordinate(walk.grid.size() - 1, walk.grid.get(0).size() - 2);

        var dag = walk.reduceGraph(walk::neighborsPartOne);

        var longestPath = walk.longestPath(origin, destination, dag);
        System.out.println(walk.render());

        assertEquals(94, longestPath);
    }

    @Test
    public void testPartTwoSmall() {
        var walk = new LongWalk(new Scanner(testInput));
        assertEquals(154, walk.partTwo());
    }

    @Test
    public void testPartTwoSmallOldWay() {
        var walk = new LongWalk(new Scanner(testInput));
        var reduction = walk.reduceGraph(walk::neighborsPartTwo);
        System.out.printf("number of nodes: %d\n", reduction.size());
        System.out.printf("number of edges: %d\n", reduction.values().stream().map(Set::size).reduce(0, Integer::sum));
        var paths = walk.allPaths(reduction);
        System.out.printf("number of paths: %d\n", paths.size());

        assertEquals(154, paths.stream().map(x -> x.stream().map(y -> y.length()).reduce(0, Integer::sum)).max(Comparator.naturalOrder()).get());
    }

    @Test
    public void testPartTwoLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day23.txt");
        assertNotNull(txtFile);

        var walk = new LongWalk(new Scanner(txtFile));
        assertEquals(6426, walk.partTwo()); // 14 seconds
    }

    @Test
    public void testDagReductionSmall() {
        var walk = new LongWalk(new Scanner(testInput));
        LongWalk.Coordinate origin = new LongWalk.Coordinate(0, 1);
        LongWalk.Coordinate destination =  new LongWalk.Coordinate(walk.grid.size() - 1, walk.grid.get(0).size() - 2);

        var dag = walk.reduceGraph(walk::neighborsPartOne);
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
        var dag = walk.reduceGraph(walk::neighborsPartOne);
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


        var dag = walk.reduceGraph(walk::neighborsPartOne);
        var path = walk.longestPath(origin, destination, dag);
        assertEquals(2318, path);
        System.out.println(walk.render());
    }
}