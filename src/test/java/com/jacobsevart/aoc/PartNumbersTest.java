package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PartNumbersTest {
    String sample = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
            """;

    @Test

    public void testPartNumbers() {
        char[][] map = PartNumbers.load(new Scanner(sample), 10);
        List<PartNumbers.Part> partNumbers = PartNumbers.partNumbers(map);
        assertEquals(List.of(467, 35, 633, 617, 592, 755, 664, 598), partNumbers.stream().map(PartNumbers.Part::num).toList());
        assertEquals(4361, PartNumbers.partOne(new Scanner(sample), 10));
    }

    @Test
    public void testPivot() {
        var partNumbers = PartNumbers.partNumbers(PartNumbers.load(new Scanner(sample), 10));
        var pivoted = PartNumbers.arrangeBySymbol(partNumbers);
    }

    @Test
    public void testPartOne() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day3.txt");
        assertNotNull(txtFile);

        assertEquals(509115, PartNumbers.partOne(new Scanner(txtFile), 140));
    }

    @Test
    public void testPartTwo() {
        assertEquals(467835, PartNumbers.partTwo(new Scanner(sample), 10));

        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day3.txt");
        assertNotNull(txtFile);

        assertEquals(75220503, PartNumbers.partTwo(new Scanner(txtFile), 140));
    }

}