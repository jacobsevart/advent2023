package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ClumsyCrucibleTest {
    String testInput = """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533""";

    @Test
    public void testConstructor() {
        var c = new ClumsyCrucible(new Scanner(testInput));
        System.out.println(c.render());
    }

    @Test
    public void testShortestPath() {
        var c = new ClumsyCrucible(new Scanner(testInput));
        var path = c.shortestPath();

        System.out.println(c.renderPath(path));
    }
}