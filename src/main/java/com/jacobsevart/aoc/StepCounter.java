package com.jacobsevart.aoc;

import java.util.*;

public class StepCounter {
    List<List<Character>> chars = new ArrayList<>();
    record CoordinateWithLength(int x, int y, int length) {};
    record Coordinate(int x, int y) {};

    CoordinateWithLength start;

    public StepCounter(Scanner in) {
        while (in.hasNextLine()) {
            List<Character> row = new ArrayList<>();
            String line = in.nextLine();

            for (char c : line.toCharArray()) {
                row.add(c);
            }

            chars.add(row);
        }
    }

    Map<Coordinate,List<Integer>> fromAllEdges() {
        Map<Coordinate,List<Integer>> out = new HashMap<>();

        for (int i = 0; i < chars.size(); i++) {
            out.put(new Coordinate(i, 0), Arrays.asList(walk(new CoordinateWithLength(i, 0, 0))));
            out.put(new Coordinate(i, chars.get(0).size() - 1), Arrays.asList(walk(new CoordinateWithLength(i, chars.get(0).size() - 1, 0))));
        }

        for (int j = 0; j < chars.get(0).size(); j++) {
            out.put(new Coordinate(0, j), Arrays.asList(walk(new CoordinateWithLength(0, j, 0))));
            out.put(new Coordinate(chars.size() - 1, j), Arrays.asList(walk(new CoordinateWithLength(chars.size() - 1, j, 0))));
        }

        return out;
    }

    CoordinateWithLength findStart() {
        for (int i = 0; i < chars.size(); i++) {
            for (int j = 0; j < chars.get(0).size(); j++) {
                if (chars.get(i).get(j) == 'S') {
                    return new CoordinateWithLength(i, j, 0);
                }
            }
        }
        throw new IllegalArgumentException();
    }

    public long walk(int n) {
        return gardenPlotsIn(walk(findStart()), n);
    }

    public long gardenPlotsIn(Integer[] reachable, int n) {
        int acc = 0;
        for (int i = 0; i <= n; i += 2) {
            acc += reachable[n - i];
        }

        return acc;
    }

    public long walkInfinite(long budget) {
        Queue<CoordinateWithLength> q = new ArrayDeque<>();
        Integer[] found = new Integer[10000];
        Arrays.fill(found, 0);
        Set<Coordinate> visited = new HashSet<>();

        q.add(findStart());

        long extra = 0L;

        while (!q.isEmpty()) {
            CoordinateWithLength here = q.poll();

            if (budget - here.length < 0) {
               continue;
            }

            if (visited.contains(new Coordinate(here.x, here.y))) continue;

            char hereChar = chars.get(Math.floorMod(here.x, chars.size())).get(Math.floorMod(here.y, chars.get(0).size()));
            if (hereChar != 'S' && hereChar != '.') continue;

            visited.add(new Coordinate(here.x, here.y));

            found[here.length]++;

            q.add(new CoordinateWithLength(here.x - 1, here.y, here.length + 1));
            q.add(new CoordinateWithLength(here.x + 1, here.y, here.length + 1));
            q.add(new CoordinateWithLength(here.x, here.y -1, here.length + 1));
            q.add(new CoordinateWithLength(here.x, here. y + 1, here.length + 1));
        }

        long acc = 0;
        for (long i = 0; i <= budget; i += 2) {
            acc += found[(int) (budget - i)];
        }

        return acc;
    }


    public Integer[] walk(CoordinateWithLength start) {
        Queue<CoordinateWithLength> q = new ArrayDeque<>();
        Integer[] found = new Integer[1000];
        Arrays.fill(found, 0);
        Set<Coordinate> visited = new HashSet<>();

        q.add(start);

        int maxLength = 0;
        while (!q.isEmpty()) {
            CoordinateWithLength here = q.poll();

            if (here.x < 0 || here.x >= chars.size()) continue;
            if (here.y < 0 || here.y >= chars.get(0).size()) continue;
            if (visited.contains(new Coordinate(here.x, here.y))) continue;

            char hereChar = chars.get(here.x).get(here.y);
            if (hereChar != 'S' && hereChar != '.') continue;

            visited.add(new Coordinate(here.x, here.y));

            found[here.length]++;
            maxLength = Math.max(maxLength, here.length);

            q.add(new CoordinateWithLength(here.x - 1, here.y, here.length + 1));
            q.add(new CoordinateWithLength(here.x + 1, here.y, here.length + 1));
            q.add(new CoordinateWithLength(here.x, here.y -1, here.length + 1));
            q.add(new CoordinateWithLength(here.x, here. y + 1, here.length + 1));
        }

       return Arrays.copyOf(found, maxLength);
    }

    String render() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.size(); i++) {
            for (int j = 0; j < chars.get(0).size(); j++) {
                    sb.append(chars.get(i).get(j));
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
