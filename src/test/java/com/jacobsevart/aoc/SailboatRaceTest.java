package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class SailboatRaceTest {
    String testInput = """
            Time:      7  15   30
            Distance:  9  40  200
            """;

    @Test
    public void testParse() {
        var race = new SailboatRace(new Scanner(testInput));
        assertEquals(new SailboatRace.Race(7, 9), race.races.get(0));
        assertEquals(new SailboatRace.Race(15, 40), race.races.get(1));
        assertEquals(new SailboatRace.Race(30, 200), race.races.get(2));
    }

    @Test
    public void testWays() {
       var race = new SailboatRace(new Scanner(testInput));
       int[] expected = {4,8,9};
       for (int i = 0; i < race.races.size(); i++) {
           assertEquals(expected[i], SailboatRace.waysToBeatRecord(race.races.get(i)));
       }
    }

    @Test
    public void testPartOneSmall() {
        assertEquals(288, new SailboatRace(new Scanner(testInput)).partOne());
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day6.txt");
        assertNotNull(txtFile);

        assertEquals(1159152, new SailboatRace(new Scanner(txtFile)).partOne());
    }

}