package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ComponentsTest {
    String testInput = """
            jqt: rhn xhk nvd
            rsh: frs pzl lsr
            xhk: hfx
            cmg: qnr nvd lhk bvb
            rhn: xhk bvb hfx
            bvb: xhk hfx
            pzl: lsr hfx nvd
            qnr: nvd
            ntq: jqt hfx bvb xhk
            nvd: lhk
            lsr: lhk
            rzs: qnr cmg lsr rsh
            frs: qnr lhk lsr""";

    @Test
    public void testKarger() {
        var random = new Random();

        var c = new Components(new Scanner(testInput));
        assertEquals(54, Components.karger(c.g, random));
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day25.txt");
        assertNotNull(txtFile);

        var random = new Random();
        var c = new Components(new Scanner(txtFile));
        assertEquals(507626, Components.karger(c.g, random));
    }


}