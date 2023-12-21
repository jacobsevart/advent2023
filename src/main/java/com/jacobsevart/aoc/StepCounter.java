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

    void addIfLegal(Queue<CoordinateWithLength> q, CoordinateWithLength c) {
        if (c.x < 0 || c.x >= chars.size()) return;
        if (c.y < 0 || c.y >= chars.get(0).size()) return;
        char hereChar = chars.get(c.x).get(c.y);
        if (hereChar == 'O' || hereChar == '#') return;

        q.add(c);
    }

    public int walk(int n) {
        Queue<CoordinateWithLength> q = new ArrayDeque<>();

        for (int i = 0; i < chars.size(); i++) {
            for (int j = 0; j < chars.get(0).size(); j++) {
                if (chars.get(i).get(j) == 'S') {
                    q.add(new CoordinateWithLength(i, j, 0));
                }
            }
        }

        int[] found = new int[n + 1];


        while (!q.isEmpty()) {
            CoordinateWithLength here = q.poll();

            if (here.x < 0 || here.x >= chars.size()) continue;
            if (here.y < 0 || here.y >= chars.get(0).size()) continue;
            char hereChar = chars.get(here.x).get(here.y);
            if (hereChar != 'S' && hereChar != '.') continue;

            chars.get(here.x).set(here.y, (char) ('a' + here.length));

            found[here.length]++;

            if (here.length == n) continue;

            addIfLegal(q, new CoordinateWithLength(here.x - 1, here.y, here.length + 1));
            addIfLegal(q, new CoordinateWithLength(here.x + 1, here.y, here.length + 1));
            addIfLegal(q, new CoordinateWithLength(here.x, here.y -1, here.length + 1));
            addIfLegal(q, new CoordinateWithLength(here.x, here. y + 1, here.length + 1));
        }

        int acc = 0;
        for (int i = 0; i < found.length; i += 2) {
            acc += found[found.length - 1 - i];
        }

        return acc;
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
