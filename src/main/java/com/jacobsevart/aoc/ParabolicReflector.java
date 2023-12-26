package com.jacobsevart.aoc;

import java.util.*;

public class ParabolicReflector {
    List<List<Character>> grid;
    Map<Character, Integer> counts;

    public ParabolicReflector(Scanner in) {
        grid = parse(in);
        counts = countObjects();
    }

    public ParabolicReflector(List<List<Character>> grid) {
        // copy the array in so it can be reused
        this.grid = new ArrayList<>();
        for (int i = 0; i < grid.size(); i++) {
            List<Character> row = new ArrayList<>();
            for (int j = 0; j < grid.get(0).size(); j++) {
                row.add(grid.get(i).get(j));
            }
            this.grid.add(row);
        }

        counts = countObjects();
    }

    Map<Character, Integer> countObjects() {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(0).size(); j++) {
                char c = grid.get(i).get(j);
                if (map.containsKey(c)) {
                    map.put(c, map.get(c) + 1);
                } else {
                    map.put(c, 1);
                }
            }
        }

        return map;
    }

    Integer MAX_ITERATIONS = 100000;

    static long partTwo(Scanner in) {
        var grid = parse(in);

        var r1 = new ParabolicReflector(grid);
        var foundCycle = r1.cycleLength();

        long length = foundCycle.end - foundCycle.start;
        long cycle = ((long) 1000000000 - foundCycle.start) % length;

        System.out.printf("length: %d, warmup: %d, spin: %d\n", length, foundCycle.start, cycle);

        var r2 = new ParabolicReflector(grid);
        for (int i = 0; i < foundCycle.start + cycle; i++) {
            r2.cycle();
        }

        return r2.totalLoad();
    }

    record Cycle(int start, int end) {};
    Cycle cycleLength() {
        Map<String, Integer> seen = new HashMap<>();
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            cycle();

            String got = render();
            if (seen.containsKey(got)) {
                // The +1s turn out to be important.. later we are going to
                // cycle n times, and here at i=0 we have cycled 1 time.
                return new Cycle(seen.get(got) + 1, i + 1);
            }

            seen.put(got, i);
        }

        throw new RuntimeException("max iterations, not stable");
    }

    void cycle() {
        tiltNorth();
        tiltWest();
        tiltSouth();
        tiltEast();
    }

    public void tiltNorth() {
        for (int j = 0; j < grid.get(0).size(); j++) {
            int slideTo = grid.size();

            for (int i = 0; i < grid.size(); i++) {
                if (grid.get(i).get(j) == '#') {
                    slideTo = i + 1;
                } else if (grid.get(i).get(j) == '.') {
                    if (slideTo > i) {
                        slideTo = i;
                    }
                } else if (grid.get(i).get(j) == 'O') {
                    if (slideTo < i) {
                        grid.get(slideTo++).set(j, 'O'); // swap boulder to slideTo
                        grid.get(i).set(j, '.'); // replace boulder with empty space
                    }
                    // keep advancing slideTo past any obstacles
                    while (slideTo < grid.size() && grid.get(slideTo).get(j) != '.') slideTo++;
                }
            }
        }

        if (!countObjects().equals(counts)) throw new RuntimeException();
    }

    public void tiltWest() {
        for (int i = 0; i < grid.get(0).size(); i++) {
            int slideTo = grid.size();

            for (int j = 0; j < grid.size(); j++) {
                if (grid.get(i).get(j) == '#') {
                    slideTo = j + 1;
                } else if (grid.get(i).get(j) == '.') {
                    if (slideTo > j) {
                        slideTo = j;
                    }
                } else if (grid.get(i).get(j) == 'O') {
                    if (slideTo < j) {
                        grid.get(i).set(slideTo++, 'O'); // swap boulder to slideTo
                        grid.get(i).set(j, '.'); // replace boulder with empty space
                    }
                    // keep advancing slideTo past any obstacles
                    while (slideTo < grid.size() && grid.get(i).get(slideTo) != '.') slideTo++;
                }
            }
        }
        if (!countObjects().equals(counts)) throw new RuntimeException();

    }

    public void tiltEast() {
        for (int i = 0; i < grid.get(0).size(); i++) {
            int slideTo = 0;

            for (int j = grid.size() - 1; j >= 0; j--) {
                if (grid.get(i).get(j) == '#') {
                    slideTo = j - 1;
                } else if (grid.get(i).get(j) == '.') {
                    if (slideTo < j) {
                        slideTo = j;
                    }
                } else if (grid.get(i).get(j) == 'O') {
                    if (slideTo > j) {
                        grid.get(i).set(slideTo--, 'O'); // swap boulder to slideTo
                        grid.get(i).set(j, '.'); // replace boulder with empty space
                    }
                    // keep advancing slideTo past any obstacles
                    while (slideTo >= 0 && grid.get(i).get(slideTo) != '.') slideTo--;
                }
            }
        }
        if (!countObjects().equals(counts)) throw new RuntimeException();

    }

    public void tiltSouth() {
        for (int j = 0; j < grid.get(0).size(); j++) {
            int slideTo = 0;

            for (int i = grid.size() - 1; i >= 0; i--) {
                if (grid.get(i).get(j) == '#') {
                    slideTo = i - 1;
                } else if (grid.get(i).get(j) == '.') {
                    if (slideTo < i) {
                        slideTo = i;
                    }
                } else if (grid.get(i).get(j) == 'O') {
                    if (slideTo > i) {
                        grid.get(slideTo--).set(j, 'O'); // swap boulder to slideTo
                        grid.get(i).set(j, '.'); // replace boulder with empty space
                    }
                    // keep advancing slideTo past any obstacles
                    while (slideTo >= 0 && grid.get(slideTo).get(j) != '.') slideTo--;
                }
            }
        }
        if (!countObjects().equals(counts)) throw new RuntimeException();
    }

    public long totalLoad() {
        long weight = 0;

        for (int i = 0; i < grid.size(); i++) {
            long boulders = grid.get(i).stream().filter(x -> x == 'O').count();

            weight += boulders * (grid.size() - i);
        }

        return weight;
    }

    String getColumn(int j) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grid.size(); i++) {
            sb.append(grid.get(i).get(j));
        }

        return sb.toString();
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(0).size(); j++) {
                sb.append(grid.get(i).get(j));
            }

            if (i < grid.size() - 1) sb.append("\n");
        }

        return sb.toString();
    }

    static List<List<Character>> parse(Scanner in) {
        List<List<Character>> out = new ArrayList<>();

        while (in.hasNextLine()) {
            List<Character> row = new ArrayList<>();
            for (char c : in.nextLine().toCharArray()) {
                row.add(c);
            }
            out.add(row);
        }

        return out;
    }


}
