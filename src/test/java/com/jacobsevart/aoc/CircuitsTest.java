package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CircuitsTest {
    String testInput = """
            broadcaster -> a, b, c
            %a -> b
            %b -> c
            %c -> inv
            &inv -> a""";

    String moreInterestingTestInput = """
            broadcaster -> a
            %a -> inv, con
            &inv -> b
            %b -> con
            &con -> output""";

    @Test
    public void testConstructor() {
        var c = new Circuits(new Scanner(testInput));
        assertEquals(List.of("a", "b", "c"), c.out.get("broadcaster"));
        assertEquals(List.of("b"), c.out.get("a"));
        assertEquals(List.of("c"), c.out.get("b"));
        assertEquals(List.of("broadcaster", "inv"), c.in.get("a"));
    }

    @Test
    public void testSimulateOne() {
        var c = new Circuits(new Scanner(testInput));

        String expected = """
                button -low-> broadcaster
                broadcaster -low-> a
                broadcaster -low-> b
                broadcaster -low-> c
                a -high-> b
                b -high-> c
                c -high-> inv
                inv -low-> a
                a -low-> b
                b -low-> c
                c -low-> inv
                inv -high-> a
                """;


        assertEquals(expected, c.pressButton().pulses());
    }

    @Test
    public void testSimulateTwo() {
        String expectedFirst = """
                button -low-> broadcaster
                broadcaster -low-> a
                a -high-> inv
                a -high-> con
                inv -low-> b
                con -high-> output
                b -high-> con
                con -low-> output
                """;

        String expectedSecond = """
                button -low-> broadcaster
                broadcaster -low-> a
                a -low-> inv
                a -low-> con
                inv -high-> b
                con -high-> output
                """;

        String expectedThird = """
                button -low-> broadcaster
                broadcaster -low-> a
                a -high-> inv
                a -high-> con
                inv -low-> b
                con -low-> output
                b -low-> con
                con -high-> output
                """;

        String expectedFourth = """
                button -low-> broadcaster
                broadcaster -low-> a
                a -low-> inv
                a -low-> con
                inv -high-> b
                con -high-> output
                """;

        var s = new Circuits(new Scanner(moreInterestingTestInput));
        assertEquals(expectedFirst, s.pressButton().pulses());
        assertEquals(expectedSecond, s.pressButton().pulses());
        assertEquals(expectedThird, s.pressButton().pulses());
        assertEquals(expectedFourth, s.pressButton().pulses());
    }

    @Test
    public void testPartOneSmall() {
        var s = new Circuits(new Scanner(testInput));
        assertEquals(32000000, s.partOne());
    }

    @Test
    public void testPartOneSmallInteresting() {
        var s = new Circuits(new Scanner(moreInterestingTestInput));
        assertEquals(11687500, s.partOne());
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day20.txt");
        assertNotNull(txtFile);

        var s = new Circuits(new Scanner(txtFile));
        assertEquals(814934624, s.partOne());
    }

}