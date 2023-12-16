package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class LightBeamsTest {
    String testInput = """
            .|...\\....
            |.-.\\.....
            .....|-...
            ........|.
            ..........
            .........\\
            ..../.\\\\..
            .-.-/..|..
            .|....-|.\\
            ..//.|....
            """;

    @Test
    public void testPartOneSmall() {
        var beams = new LightBeams(new Scanner(testInput));
        assertEquals(46, beams.trace());
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day16.txt");
        assertNotNull(txtFile);

        var beams = new LightBeams(new Scanner(txtFile));
        assertEquals(6855, beams.trace());
    }

}