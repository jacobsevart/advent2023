package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class SeedMapsTest {
    String testInput = """
            seeds: 79 14 55 13
                        
            seed-to-soil map:
            50 98 2
            52 50 48
                        
            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15
                        
            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4
                        
            water-to-light map:
            88 18 7
            18 25 70
                        
            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13
                        
            temperature-to-humidity map:
            0 69 1
            1 0 69
                        
            humidity-to-location map:
            60 56 37
            56 93 4""";

    @Test
    public void testPartOne() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day5.txt");
        assertNotNull(txtFile);

        assertEquals(0, SeedMaps.partOne(new Scanner(txtFile)));
    }

    @Test
    public void testPartOneSmall() {
        assertEquals(35, SeedMaps.partOne(new Scanner(testInput)));
    }

    public void testAdvance(long in, long out) {
        var ranges = new ArrayList<SeedMaps.Range>();
        ranges.add(new SeedMaps.Range(0, 15, 37));
        ranges.add(new SeedMaps.Range(37, 52, 2));
        ranges.add(new SeedMaps.Range(39, 0, 15));
        var map = new SeedMaps.AlmanacMap("soil", "fertilizer", ranges);


        assertEquals(out, SeedMaps.advance(map, in));
    }

    @Test
    public void testAdvance() {
        testAdvance(0, 15);
        testAdvance(36, 51);
        testAdvance(37, 52);
        testAdvance(38, 53);
        testAdvance(39, 0);
        testAdvance(49, 10);
        testAdvance(53, 14);
    }

    @Test
    public void testParse() {
        String in = """
                soil-to-fertilizer map:
                0 15 37
                37 52 2
                39 0 15""";

        SeedMaps.AlmanacMap map = SeedMaps.readNextMap(new Scanner(in));
        assertEquals("soil", map.from());
        assertEquals("fertilizer", map.to());

        assertEquals(new SeedMaps.Range(15, 0, 37), map.ranges().get(0));
        assertEquals(new SeedMaps.Range(52, 37, 2), map.ranges().get(1));
        assertEquals(new SeedMaps.Range(0, 39, 15), map.ranges().get(2));
    }


}
