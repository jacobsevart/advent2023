package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class HashingTest {
    String testInput = """
            rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7""";
    @Test
    public void testHashing() {
        assertEquals(30, Hashing.hash("rn=1"));
        assertEquals(253, Hashing.hash("cm-"));
    }

    @Test
    public void testPartOne() {
        assertEquals(1320, new Hashing(new Scanner(testInput)).sum);
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day15.txt");
        assertNotNull(txtFile);

        assertEquals(510388, new Hashing(new Scanner(txtFile)).sum);
    }


}