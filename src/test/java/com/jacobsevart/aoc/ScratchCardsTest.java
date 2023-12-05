package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ScratchCardsTest {
    String testInput = """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
            """;

    @Test
    public void testPartTwo() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day4.txt");
        assertNotNull(txtFile);

        assertEquals(5132675, ScratchCards.partTwo(new Scanner(txtFile), 197));
    }

    @Test
    public void testPartTwoSmall() {
        assertEquals(30, ScratchCards.partTwo(new Scanner(testInput), 6));
    }

    @Test
    public void testNumCards() {
        assertEquals(30, ScratchCards.numCards(new int[]{4, 2, 2, 1, 0, 0}));
    }

    @Test
    public void testPartOne() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day4.txt");
        assertNotNull(txtFile);

        assertEquals(21959, ScratchCards.partOne(new Scanner(txtFile)));
    }

    @Test
    public void testWinningNumbersSmall() {
        assertEquals(13, ScratchCards.partOne(new Scanner(testInput)));
    }

    @Test
    public void testWinningNumbers() {
        assertEquals(4, ScratchCards.countWinning("Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"));
        assertEquals(8, ScratchCards.points(4));
        assertEquals(5, ScratchCards.countWinning("Card 1: 41 48 83 86 17  6 | 83 86  6 31 17  9 48 53"));
        assertEquals(16, ScratchCards.points(5));

    }
}