package com.jacobsevart.aoc;

import java.util.*;

public class Lagoon {
    record Edge(String direction, int distance, String color) {};

    List<Edge> edges = new ArrayList<>();
    char[][] grid;
    int pathLength = 0;

    int iStart;
    int jStart;

    public Lagoon(Scanner in) {
        while (in.hasNextLine()) {
            String direction = in.next();
            int distance = in.nextInt();
            String colorSpec = in.next().replace("(", "").replace(")", "");

            edges.add(new Edge(direction, distance, colorSpec));
            pathLength += distance;
        }
    }

    int fill() {
        record Coordinate(int i, int j) {};

        // find an interior cell
        Coordinate start = null;
        for (int i = 0; i < grid.length; i++) {
            int idx = String.valueOf(grid[i]).indexOf(".#.");
            if (idx >= 0) {
                start = new Coordinate(i, idx + 3);
                break;
            }
        }

        if (start == null) {
            throw new RuntimeException("can't find a way in");
        }

        Queue<Coordinate> q = new ArrayDeque<>();
        q.add(start);

        int filled = 0;
        while (!q.isEmpty()) {
            Coordinate c = q.poll();
            if (c.i < 0 || c.i >= grid.length) continue;
            if (c.j < 0 || c.j >= grid[0].length) continue;
            if (grid[c.i][c.j] == '#') continue;

            grid[c.i][c.j] = '#';
            filled++;

            q.add(new Coordinate(c.i + 1, c.j));
            q.add(new Coordinate(c.i -1, c.j));
            q.add(new Coordinate(c.i, c.j - 1));
            q.add(new Coordinate(c.i, c.j + 1));
        }

        return filled + pathLength;
    }

    void draw() {
        int i = iStart;
        int j = jStart;
        for (Edge e : edges) {
            if (e.direction.equals("D")) {
                for (int k = 0; k < e.distance; k++) {
                    grid[i++][j] = '#';
                }
            } else if (e.direction.equals("U")) {
                for (int k = 0; k < e.distance; k++) {
                    grid[i--][j] = '#';
                }
            } else if (e.direction.equals("R")) {
                for (int k = 0; k < e.distance; k++) {
                    grid[i][j++] = '#';
                }
            } else if (e.direction.equals("L")) {
                for (int k = 0; k < e.distance; k++) {
                    grid[i][j--] = '#';
                }
            }
        }
    }

    void sizeCanvas() {
        int iMin = 0;
        int iMax = 0;
        int jMin = 0;
        int jMax = 0;

        int i = 0;
        int j = 0;
        for (Edge e : edges) {
            if (e.direction.equals("D")) {
                i += e.distance;
            } else if (e.direction.equals("U")) {
                i -= e.distance;
            } else if (e.direction.equals("R")) {
                j += e.distance;
            } else if (e.direction.equals("L")) {
                j -= e.distance;
            }

            iMin = Math.min(iMin, i);
            jMin = Math.min(jMin, j);

            iMax = Math.max(iMax, i);
            jMax = Math.max(jMax, j);
        }

        int iSize = iMax - iMin + 1;
        int jSize = jMax - jMin + 1;

        grid = new char[iSize][];
        for (i = 0; i < iSize; i++) {
            grid[i] = new char[jSize];
            for (j = 0; j < jSize; j++) {
                grid[i][j] = '.';
            }
        }

        iStart = -iMin;
        jStart = -jMin;
    }

    String render() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                sb.append(grid[i][j]);
            }
            sb.append('\n');
        }

        return sb.toString();
    }

}
