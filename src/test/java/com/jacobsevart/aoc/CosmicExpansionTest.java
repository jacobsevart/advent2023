package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CosmicExpansionTest {
    String testInput = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....""";

    @Test
    public void testConstructor() {
        var cosmic = new CosmicExpansion(new Scanner(testInput));

        String expectExpanded = """
                ....1........
                .........2...
                3............
                .............
                .............
                ........4....
                .5...........
                ............6
                .............
                .............
                .........7...
                8....9.......
                """;

        cosmic.expand();
        String got = cosmic.draw();
        assertEquals(expectExpanded, got);
    }
    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day11.txt");
        assertNotNull(txtFile);

        var cosmic = new CosmicExpansion(new Scanner(txtFile));
        cosmic.expand();
        System.out.println(cosmic.draw());
        assertEquals(10490062L, cosmic.sumDistances(cosmic.galaxies()));
    }

    @Test
    public void testPartTwoLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day11.txt");
        assertNotNull(txtFile);

        var cosmic = new CosmicExpansion(new Scanner(txtFile));
        assertEquals(382979724122L, cosmic.sumDistances(cosmic.expandedGalaxies(999999L)));
    }

    @Test
    public void testPartOneSmall() {
        var cosmic = new CosmicExpansion(new Scanner(testInput));
        cosmic.expand();
        assertEquals(374L, cosmic.sumDistances(cosmic.galaxies()));
    }

    @Test
    public void testPartTwoSmall() {
        var cosmic = new CosmicExpansion(new Scanner(testInput));
        assertEquals(1030L, cosmic.sumDistances(cosmic.expandedGalaxies(9L)));
        assertEquals(8410L, cosmic.sumDistances(cosmic.expandedGalaxies(99L)));
    }

    @Test
    public void testShortestPath() {
        var cosmic = new CosmicExpansion(new Scanner(testInput));
        cosmic.expand();
        System.out.println(cosmic.draw());

        assertEquals(9,
                cosmic
                .shortestPath(cosmic.galaxies().get(4), cosmic.galaxies().get(8)));

    }

    @Test
    public void testExpandedCoordinates() {
        var cosmic = new CosmicExpansion(new Scanner(testInput));
        cosmic.expand();
        var expected = cosmic.galaxies();
        System.out.println(cosmic.draw());

        cosmic = new CosmicExpansion(new Scanner(testInput));
        var expanded = cosmic.expandedGalaxies(1L);

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), expanded.get(i), String.format("mismatch at %d", i));
        }
    }
}