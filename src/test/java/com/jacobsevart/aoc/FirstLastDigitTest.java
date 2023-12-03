package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class FirstLastDigitTest {
    @Test
    public void testSumFirstLast() {
        String[] strings = {"1abc2", "pqr3stu8vwx", "a1b2c3d4e5f", "treb7uchet"};
        int[] expected = {12, 38, 15, 77};
        for (int i = 0; i < strings.length; i++) {
            assertEquals(expected[i], FirstLastDigit.valueOfLine(strings[i]));
        }
    }


    @Test
    public void testSumFirstLastWithDigit() {
        record TestCase(String string, int expected) {
        }
        ;

        TestCase[] testcases = {
                new TestCase("two1nine", 29),
                new TestCase("eightwothree", 83),
                new TestCase("abcone2threexyz", 13),
                new TestCase("xtwoone3four", 24),
                new TestCase("4nineeightseven2", 42),
                new TestCase("zoneight234", 14),
                new TestCase("7pqrstsixteen", 76),
        };
        FirstLastDigit fld = new FirstLastDigit();

        for (TestCase testcase : testcases) {
            System.out.println(testcase);
            assertEquals(testcase.expected, fld.valueOfLineWithDigit(testcase.string), testcase.string);
        }
    }

    @Test
    public void testPart1() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day1.txt");
        assertNotNull(txtFile);
        var soln = FirstLastDigit.sumFirstLast(new Scanner(txtFile));
        assertEquals(54362, soln);
        System.out.println(soln);
    }

    @Test
    public void testPart2() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day1.txt");
        assertNotNull(txtFile);
        var soln = new FirstLastDigit().sumPart2(new Scanner(txtFile));
        assertEquals(53515, soln);
        System.out.println(soln);
    }
}