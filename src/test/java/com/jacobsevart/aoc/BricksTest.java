package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class BricksTest {
    String testInput = """
            1,0,1~1,2,1
            0,0,2~2,0,2
            0,2,3~2,2,3
            0,0,4~0,2,4
            2,0,5~2,2,5
            0,1,6~2,1,6
            1,1,8~1,1,9
            """;

    public BricksTest() {
        super();
    }

    @Test
    public void testBricks() {
        var br = new Bricks(new Scanner(testInput));
        var got = br.drop();

        assertEquals(List.of(1, 2), got.get(0));
        assertEquals(List.of(3, 4), got.get(1));
        assertEquals(List.of(3, 4), got.get(2));
        assertEquals(List.of(5), got.get(3));
        assertEquals(List.of(5), got.get(4));
        assertEquals(List.of(6), got.get(5));
        assertEquals(List.of(), got.get(6));
    }

    @Test
    public void testRemovable() {
        var br = new Bricks(new Scanner(testInput));
        var got = br.drop();

        assertEquals(List.of(1, 2, 3, 4, 6), br.removable(got));
    }

    @Test
    public void testPartOneSmall() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day22.txt");
        assertNotNull(txtFile);

        var br = new Bricks(new Scanner(txtFile));
        assertEquals(405, br.removable(br.drop()).size());
    }

    @Test
    public void testPartTwoSmall() {
        var br = new Bricks(new Scanner(testInput));
        assertEquals(7, br.partTwo());
    }

    @Test
    public void testPartTwoLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day22.txt");
        assertNotNull(txtFile);

        var br = new Bricks(new Scanner(txtFile));
        assertEquals(61297, br.partTwo());
    }

}