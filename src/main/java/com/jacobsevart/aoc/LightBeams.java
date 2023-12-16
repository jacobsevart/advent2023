package com.jacobsevart.aoc;

import java.util.*;

public class LightBeams {
    List<List<Character>> grid;
    List<List<Set<BeamDirection>>> visited;

    public LightBeams(Scanner in) {
        grid = new ArrayList<>();
        visited = new ArrayList<>();
        while (in.hasNextLine()) {
            List<Character> row = new ArrayList<>();
            List<Set<BeamDirection>> visitedRow = new ArrayList<>();
            for (char c : in.nextLine().toCharArray()) {
                row.add(c);
                visitedRow.add(new HashSet<>());
            }

            grid.add(row);
            visited.add(visitedRow);
        }
    }

    enum BeamDirection {
        RIGHT,
        LEFT,
        UP,
        DOWN,
    }

    record Coordinate(int x, int y, BeamDirection direction) {};

    void resetVisited() {
        for (int i = 0; i < visited.size(); i++) {
            for (int j = 0; j < visited.get(0).size(); j++) {
                visited.get(i).set(j, new HashSet<>());
            }
        }
    }

    int findMax() {
        int currentMax = 0;
        for (int i = 0; i < grid.size(); i++) {
            currentMax = Math.max(currentMax, trace(new Coordinate(i, 0, BeamDirection.RIGHT)));
            resetVisited();
            currentMax = Math.max(currentMax, trace(new Coordinate(i, grid.get(0).size() - 1, BeamDirection.LEFT)));
            resetVisited();
        }

        for (int j = 0; j < grid.get(0).size(); j++) {
            currentMax = Math.max(currentMax, trace(new Coordinate(0, j, BeamDirection.DOWN)));
            resetVisited();
            currentMax = Math.max(currentMax, trace(new Coordinate(grid.size() - 1, j, BeamDirection.UP)));
            resetVisited();
        }

        return currentMax;
    }

    public int trace() {
        return trace(new Coordinate(0, 0, BeamDirection.RIGHT));
    }

    public int trace(Coordinate origin) {
        Queue<Coordinate> origins = new LinkedList<>();
        origins.add(origin);

        int i = 0;

        while (!origins.isEmpty()) {
            Coordinate c = origins.poll();

            if (!boundsCheck(c)) continue;

            int x = c.x;
            int y = c.y;

            Set<BeamDirection> visitedHere = visited.get(x).get(y);
            if (visitedHere.contains(c.direction)) continue;
            if (visitedHere.isEmpty()) i++;
            visitedHere.add(c.direction);

            char here = grid.get(x).get(y);

            Coordinate next = null;
            if (here == '.') {
                next = step(c, c.direction);
            } else if (here == '/') {
                if (c.direction == BeamDirection.RIGHT) {
                    next = step(c, BeamDirection.UP);
                } else if (c.direction == BeamDirection.LEFT) {
                    next = step(c, BeamDirection.DOWN);
                } else if (c.direction == BeamDirection.UP) {
                    next = step(c, BeamDirection.RIGHT);
                } else if (c.direction == BeamDirection.DOWN){
                    next = step(c, BeamDirection.LEFT);
                }
            } else if (here == '\\') {
                if (c.direction == BeamDirection.RIGHT) {
                    next = step(c, BeamDirection.DOWN);
                } else if (c.direction == BeamDirection.LEFT) {
                    next = step(c, BeamDirection.UP);
                } else if (c.direction == BeamDirection.UP) {
                    next = step(c, BeamDirection.LEFT);
                } else if (c.direction == BeamDirection.DOWN){
                    next = step(c, BeamDirection.RIGHT);
                }
            } else if (here == '|') {
                if (c.direction == BeamDirection.DOWN || c.direction == BeamDirection.UP) {
                    next = step(c, c.direction);
                } else {
                    next = step(c, BeamDirection.UP);
                    origins.add(step(c, BeamDirection.DOWN));
                }
            } else if (here == '-') {
                if (c.direction == BeamDirection.LEFT || c.direction == BeamDirection.RIGHT) {
                    next = step(c, c.direction);
                } else {
                    next = step(c, BeamDirection.RIGHT);
                    origins.add(step(c, BeamDirection.LEFT));
                }
            } else {
                throw new RuntimeException("invalid character");
            }

            origins.add(next);
        }

        return i;
    }

    void render() {
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(0).size(); j++) {
                if (!visited.get(i).get(j).isEmpty()) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    boolean boundsCheck(Coordinate c) {
        if (c.x < 0 || c.x >= grid.size()) return false;
        if (c.y < 0 || c.y >= grid.get(0).size()) return false;

        return true;
    }

    static Coordinate step(Coordinate c, BeamDirection direction) {
        if (direction == BeamDirection.RIGHT){
            return new Coordinate(c.x, c.y + 1, direction);
        } else if (direction == BeamDirection.LEFT) {
            return new Coordinate(c.x, c.y - 1, direction);
        } else if (direction == BeamDirection.UP) {
            return new Coordinate (c.x - 1, c.y, direction);
        } else if (direction == BeamDirection.DOWN) {
            return new Coordinate(c.x + 1, c.y, direction);
        } else {
            throw new RuntimeException("inexhaustive");
        }
    }
}
