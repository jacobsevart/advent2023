package com.jacobsevart.aoc;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SpringConditionsTest {
    String testInput = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1""";
    @Test
    public void testConstructor() {
        var s = new SpringConditions(new Scanner(testInput));
    }

    @Test
    public void testPartOneLarge() {
        InputStream txtFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("day12.txt");
        assertNotNull(txtFile);

        var sc = new SpringConditions(new Scanner(txtFile));
        assertEquals(7221, sc.partOne());
    }

    @Test
    public void testPartOneSmall() {
        var sc = new SpringConditions(new Scanner(testInput));
        assertEquals(21, sc.partOne());
    }

    @Test
    public void testPossibilites() {
        assertEquals(10, SpringConditions.possibilities(new SpringConditions.Row(SpringConditions.runs("?###????????"), new int[]{3,2,1})));
        assertEquals(4, SpringConditions.possibilities(new SpringConditions.Row(SpringConditions.runs(".??..??...?##."), new int[]{1, 1, 3})));
    }

    @Test
    public void testGenerate() {
        var out = SpringConditions.generate(7, new int[]{2, 1}, 0);
        assertEquals(10, out.size());
    }

    @Test
    public void testCheck() {
        assertTrue(SpringConditions.check("????????", ".##..#.."));
        assertTrue(SpringConditions.check("?###????????", ".###.##..#.."));
        assertTrue(SpringConditions.check("?###????????", ".###..##..#."));

        assertTrue(SpringConditions.check(".??..??...?##.", ".#...#....###."));
    }
}