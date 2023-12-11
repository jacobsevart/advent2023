package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CamelCardsTest {
    String testInput = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
            """;

    @Test
    public void testCardClassification() {
        assertEquals(CamelCards.Kind.FIVE_KIND, CamelCards.Hand.assignKind(new char[]{'A', 'A', 'A', 'A', 'A'}));
        assertEquals(CamelCards.Kind.FOUR_KIND, CamelCards.Hand.assignKind(new char[]{'A', 'A', '8', 'A', 'A'}));
        assertEquals(CamelCards.Kind.FULL_HOUSE, CamelCards.Hand.assignKind(new char[]{'A', 'A', 'A', 'K', 'K'}));
        assertEquals(CamelCards.Kind.THREE_KIND, CamelCards.Hand.assignKind(new char[]{'T', 'T', 'T', '9', '8'}));
        assertEquals(CamelCards.Kind.TWO_PAIR, CamelCards.Hand.assignKind(new char[]{'2', '3', '4', '3', '2'}));
        assertEquals(CamelCards.Kind.ONE_PAIR, CamelCards.Hand.assignKind(new char[]{'A', '2', '3', 'A', '4'}));
        assertEquals(CamelCards.Kind.HIGH_CARD, CamelCards.Hand.assignKind(new char[]{'2', '3', '4', '5', '6'}));


        assertEquals(CamelCards.Kind.FULL_HOUSE, CamelCards.Hand.assignKindTwo("AAJKK".toCharArray()));
        assertEquals(CamelCards.Kind.THREE_KIND, CamelCards.Hand.assignKindTwo("AQJJK".toCharArray()));
        assertEquals(CamelCards.Kind.THREE_KIND, CamelCards.Hand.assignKindTwo("AQJKK".toCharArray()));
        assertEquals(CamelCards.Kind.FIVE_KIND, CamelCards.Hand.assignKindTwo("1JJJJ".toCharArray()));
        assertEquals(CamelCards.Kind.FIVE_KIND, CamelCards.Hand.assignKindTwo("JJJJJ".toCharArray()));
        // two pair with jokers is impossible
        assertEquals(CamelCards.Kind.ONE_PAIR, CamelCards.Hand.assignKindTwo("12J35".toCharArray()));
    }

    @Test
    public void testCompare() {
        CamelCards.Hand[] cards = {
                new CamelCards.Hand("32T3K"),
                new CamelCards.Hand("T55J5"),
                new CamelCards.Hand("KK677"),
                new CamelCards.Hand("KTJJT"),
                new CamelCards.Hand("QQQJA"),
        };

        assertEquals(1, cards[4].compareTo(cards[1]));
        assertEquals(-1, cards[3].compareTo(cards[2]));
        assertEquals(1, cards[4].compareTo(cards[1]));

        Arrays.sort(cards);

        assertEquals(new CamelCards.Hand("32T3K"), cards[0]);
        assertEquals(new CamelCards.Hand("KTJJT"), cards[1]);
        assertEquals(new CamelCards.Hand("KK677"), cards[2]);
        assertEquals(new CamelCards.Hand("T55J5"), cards[3]);
        assertEquals(new CamelCards.Hand("QQQJA"), cards[4]);
    }

    @Test
    public void testPartOneSmall() {
        assertEquals(6440, CamelCards.computeWinnings(new Scanner(testInput), false));
    }

    @Test
    public void testPartTwoSmall() {
        assertEquals(5905, CamelCards.computeWinnings(new Scanner(testInput), true));
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day7.txt");
        assertNotNull(txtFile);

        assertEquals(247823654, CamelCards.computeWinnings(new Scanner(txtFile), false));
    }

    @Test
    public void testPartTwoLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day7.txt");
        assertNotNull(txtFile);

        assertEquals(245461700L, CamelCards.computeWinnings(new Scanner(txtFile), true));
    }

}