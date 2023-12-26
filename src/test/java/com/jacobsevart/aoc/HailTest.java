package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HailTest {
    String testInput = """
            19, 13, 30 @ -2,  1, -2
            18, 19, 22 @ -1, -1, -2
            20, 25, 34 @ -2, -2, -4
            12, 31, 28 @ -1, -2, -1
            20, 19, 15 @  1, -5, -3""";

    @Test
    public void testParse() {
        var hail = new Hail(new Scanner(testInput));
        hail.hailstones.forEach(System.out::println);
    }

    @Test
    public void testIntersect() {
        var hail = new Hail(new Scanner(testInput));

        Optional<Hail.Coordinate> t = hail.crossPaths(hail.hailstones.get(0), hail.hailstones.get(1));
        assertTrue(t.isPresent());
        assertEquals(14.333, t.get().x(), 0.001);
        assertEquals(15.333, t.get().y(), 0.001);
    }

    @Test
    public void testPartOneSmall() {
        var hail = new Hail(new Scanner(testInput));

        var list = hail.partOne(7, 27);
        list.forEach(System.out::println);
        assertEquals(2, list.size());
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day24.txt");
        assertNotNull(txtFile);

        var hail = new Hail(new Scanner(txtFile));
        var list = hail.partOne(200000000000000L, 400000000000000L);

        assertEquals(21874, list.size());
    }
}