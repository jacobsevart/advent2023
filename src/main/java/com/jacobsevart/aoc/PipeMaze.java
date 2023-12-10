package com.jacobsevart.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class PipeMaze {
    record Coordinate(int x, int y) {};

    List<List<Character>> maze;
    int startI, startJ;

    public PipeMaze(Scanner in) {
        maze = new ArrayList<>();
        for (int i = 0; in.hasNextLine(); i++) {
            List<Character> line = new ArrayList<>();
            int j = 0;
            for (Character c : in.nextLine().toCharArray()) {
                line.add(c);
                if (c == 'S') {
                    startI = i;
                    startJ = j;
                }
                j++;
            }
            maze.add(line);
        }
    }

    String draw(List<Coordinate> coords) {
        StringBuilder sb = new StringBuilder();
        for (int i =0 ; i < maze.size(); i++) {
            for (int j = 0; j < maze.size(); j++) {
                if (coords.contains(new Coordinate(i, j))) {
                    sb.append(maze.get(i).get(j));
                } else {
                   sb.append(".");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    int partOne() {
        return cyclePath().size() / 2;
    }

    List<Coordinate> cyclePath() {
        return cyclePath(new Coordinate(startI, startJ), new ArrayList<>());
    }

    List<Coordinate> cyclePath(Coordinate ptr, List<Coordinate> path) {
        char node = maze.get(ptr.x).get(ptr.y);
        if (node == 'S'  && path.size() > 2) return path;
        if (path.contains(ptr)) return null;

        path.add(ptr);

        for (Coordinate neighbor : neighbors(ptr)) {
            List<Coordinate> foundPath = cyclePath(neighbor, path);
            if (foundPath != null) {
                return foundPath;
            }
        }

        return null;
    }

    List<Coordinate> neighbors(Coordinate coords) {
        List<Coordinate> out = new ArrayList<>();

        char c = maze.get(coords.x).get(coords.y);
        if (c == '.') {
            return out;
        } else if (c == '|') {
            append(out, new Coordinate(coords.x - 1, coords.y));
            append(out, new Coordinate(coords.x + 1, coords.y));
        } else if (c == '-') {
            append(out, new Coordinate(coords.x, coords.y - 1));
            append(out, new Coordinate(coords.x, coords.y + 1));
        } else if (c == 'L') {
            append(out, new Coordinate(coords.x - 1, coords.y));
            append(out, new Coordinate(coords.x, coords.y + 1));
        } else if (c == 'J') {
            append(out, new Coordinate(coords.x - 1, coords.y));
            append(out, new Coordinate(coords.x, coords.y - 1));
        } else if (c == '7') {
            append(out, new Coordinate(coords.x + 1, coords.y));
            append(out, new Coordinate(coords.x, coords.y - 1));
        } else if (c == 'F') {
            append(out, new Coordinate(coords.x + 1, coords.y));
            append(out, new Coordinate(coords.x, coords.y + 1));
        } else if (c == 'S') {
            // check out our own neighbors for connection back to us
            var north = new Coordinate(coords.x - 1, coords.y);
            if (boundsCheck(north) && neighbors(north).contains(coords)) {
                out.add(north);
            }

            var south = new Coordinate(coords.x + 1, coords.y);
            if (boundsCheck(south) && neighbors(south).contains(coords)) {
                out.add(south);
            }

            var west = new Coordinate(coords.x, coords.y -1);
            if (boundsCheck(west) && neighbors(west).contains(coords)) {
                out.add(west);
            }

            var east = new Coordinate(coords.x, coords.y + 1);
            if (boundsCheck(east) && neighbors(east).contains(coords)) {
                out.add(east);
            }
        }

        return out;
    }

    void append(List<Coordinate> out, Coordinate coords) {
        if (boundsCheck(coords)) out.add(coords);
    }

    boolean boundsCheck(Coordinate coords) {
        if (coords.x >= 0 && coords.x < maze.size()) {
            return coords.y >= 0 && coords.y < maze.get(0).size();
        }
        return false;
    }


}
