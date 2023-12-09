package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.function.BiConsumer;

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
    public void testTransformRanges() {
        List<SeedMaps.Range> startRanges = new ArrayList<>() {{
            add(new SeedMaps.Range(55, 67, 0));
            add(new SeedMaps.Range(79, 92, 1));
        }};

        List<SeedMaps.Remapping> mapRanges = new ArrayList<>() {{
            add(new SeedMaps.Remapping(98, 50, 2));
            add(new SeedMaps.Remapping(50, 52, 48));
        }};


        startRanges = SeedMaps.transformRanges(startRanges, mapRanges, "");
        startRanges.forEach(System.out::println);
        System.out.println();

        mapRanges = new ArrayList<>() {{
            add(new SeedMaps.Remapping(53, 49, 8));
        }};

        startRanges = SeedMaps.transformRanges(startRanges, mapRanges, "");
        startRanges.forEach(System.out::println);
        System.out.println();

        mapRanges = new ArrayList<>() {{
            add(new SeedMaps.Remapping(25, 18, 70));
        }};

        startRanges = SeedMaps.transformRanges(startRanges, mapRanges, "");
        startRanges.forEach(System.out::println);

    }

    @Test
    public void testPartTwoLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day5.txt");
        assertNotNull(txtFile);

        var solution = new SeedMaps(new Scanner(txtFile));
        assertEquals(1493866, solution.partTwo());
    }

    @Test
    public void testPartTwoSmall() {
        assertEquals(46, new SeedMaps(new Scanner(testInput)).partTwo());
    }

    @Test
    public void testPartOne() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day5.txt");
        assertNotNull(txtFile);

        assertEquals(174137457, new SeedMaps(new Scanner(txtFile)).partOne());
    }

    @Test
    public void testPartOneSmall() {
        assertEquals(35, new SeedMaps(new Scanner(testInput)).partOne());
    }

    @Test
    public void testAdvanceFunction() {
        BiConsumer<Long, Long> testAdvance = (Long in, Long out) -> {
            var ranges = new ArrayList<SeedMaps.Remapping>();
            ranges.add(new SeedMaps.Remapping(0, 15, 37));
            ranges.add(new SeedMaps.Remapping(37, 52, 2));
            ranges.add(new SeedMaps.Remapping(39, 0, 15));
            assertEquals(out, SeedMaps.advance(ranges, in));
        };

        testAdvance.accept(0L, 15L);
        testAdvance.accept(36L, 51L);
        testAdvance.accept(37L, 52L);
        testAdvance.accept(38L, 53L);
        testAdvance.accept(39L, 0L);
        testAdvance.accept(49L, 10L);
        testAdvance.accept(53L, 14L);
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

        assertEquals(new SeedMaps.Remapping(15, 0, 37), map.ranges().get(0));
        assertEquals(new SeedMaps.Remapping(52, 37, 2), map.ranges().get(1));
        assertEquals(new SeedMaps.Remapping(0, 39, 15), map.ranges().get(2));
    }
}
