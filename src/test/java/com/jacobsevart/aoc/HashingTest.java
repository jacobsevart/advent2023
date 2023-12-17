package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;
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
        assertEquals(1320, Hashing.hashMany(new Scanner(testInput)));
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day15.txt");
        assertNotNull(txtFile);

        assertEquals(510388, Hashing.hashMany(new Scanner(txtFile)));
    }

    @Test
    public void testParse() {
        List<Hashing.Command> commands = Hashing.parse(new Scanner(testInput));
        commands.forEach(System.out::println);
    }

    @Test
    public void testPartTwoSmall() {
        Hashing h = new Hashing();
        List<Hashing.Command> cmds = Hashing.parse(new Scanner(testInput));
        for (Hashing.Command cmd : cmds) {
            System.out.println(cmd);
            h.execute(cmd);
            h.render();
        }
        assertEquals(145, h.focusingPower());
    }

    @Test
    public void testPartTwoLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day15.txt");
        assertNotNull(txtFile);

        Hashing h = new Hashing();
        List<Hashing.Command> cmds = Hashing.parse(new Scanner(txtFile));
        for (Hashing.Command cmd : cmds) {
            h.execute(cmd);
        }
        assertEquals(291774, h.focusingPower());
    }
}