package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class DifferencesTest {

    String testInput = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
            """;

    @Test
    public void testDifferences() {
        var diffs = new Differences(new Scanner(testInput));
        diffs.sequences.forEach(System.out::println);
    }

    @Test
    public void testPartOneSmall() {
        var diffs = new Differences(new Scanner(testInput));
        assertEquals(114, diffs.partOne());
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day9.txt");
        assertNotNull(txtFile);

        var diffs = new Differences(new Scanner(txtFile));
        assertEquals(0, diffs.partOne());
    }

    @Test
    public void testExtrapolate() {
        assertEquals(18, Differences.extrapolate(Arrays.asList(0, 3, 6, 9, 12, 15)));
        assertEquals(28, Differences.extrapolate(Arrays.asList(1, 3, 6, 10, 15, 21)));
        assertEquals(68, Differences.extrapolate(Arrays.asList(10, 13, 16, 21, 30, 45)));
    }

}