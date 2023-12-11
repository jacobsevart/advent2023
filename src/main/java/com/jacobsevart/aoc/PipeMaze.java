package com.jacobsevart.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

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

    int partTwo() {
        scale();
        System.out.println(draw());
        for (int i = 0; i < maze.size(); i++) {
            floodFillStack(new PipeMaze.Coordinate(i, 0));
            floodFillStack(new PipeMaze.Coordinate(i, maze.get(0).size() - 1));
        }

        for (int i = 0; i < maze.get(0).size(); i++) {
            floodFillStack(new PipeMaze.Coordinate(0, i));
            floodFillStack(new PipeMaze.Coordinate(maze.size() - 1, i));
        }
        System.out.println(draw());

        downScale();

        System.out.println(draw());

        return countCharater('.');
    }

    int countCharater(char c) {
        int cnt = 0;
        for (int i = 0; i < maze.size(); i++) {
            for (int j = 0; j < maze.get(0).size(); j++) {
                if (maze.get(i).get(j) == c) {
                    cnt++;
                }
            }
        }

        return cnt;
    }

    void floodFill(Coordinate node) {
        if (!boundsCheck(node)) return;
        if (maze.get(node.x).get(node.y) != '.') return;

        maze.get(node.x).set(node.y, '0');

        // north
        floodFill(new Coordinate(node.x -1, node.y));
        floodFill(new Coordinate(node.x + 1, node.y));
        floodFill(new Coordinate(node.x, node.y - 1));
        floodFill(new Coordinate(node.x, node.y + 1));
    }

    void floodFillStack(Coordinate start) {
        Stack<Coordinate> stack = new Stack<>();
        stack.push(start);

        while (!stack.empty()) {
            Coordinate node = stack.pop();

            if (!boundsCheck(node)) continue;
            if (maze.get(node.x).get(node.y) != '.') continue;

            maze.get(node.x).set(node.y, '0');

            stack.push(new Coordinate(node.x, node.y + 1));
            stack.push(new Coordinate(node.x, node.y - 1));
            stack.push(new Coordinate(node.x + 1, node.y));
            stack.push(new Coordinate(node.x - 1, node.y));
        }
    }


    void scale() {
        List<List<Character>> firstPass = new ArrayList<>();
        for (int i=0; i < maze.size(); i++) {
            firstPass.add(interpolateColumns(i));
        }

        List<List<Character>> secondPass = new ArrayList<>();

        for (int i = 0; i < firstPass.size(); i++) {
            secondPass.add(firstPass.get(i));
            secondPass.add(interpolateRows(firstPass, i));
        }

        maze = secondPass;
    }

    private List<Character> interpolateColumns(int i) {
        List<Character> row = new ArrayList<>();
        for (int j = 0; j < maze.get(0).size(); j++) {
            char c = maze.get(i).get(j);
            row.add(maze.get(i).get(j));
            if (c == '-' || c == 'L' || c == 'F') {
                row.add('-');
            } else {
                if (j + 1 < maze.get(0).size()) {
                    char c2 = maze.get(i).get(j + 1);
                    if (c2 == 'J' || c2 == '7' || c2 == '-') {
                        row.add('-');
                    } else {
                        row.add('.');
                    }
                } else {
                    row.add('.');
                }
            }
        }
        return row;
    }

    private List<Character> interpolateRows(List<List<Character>> firstPass, int i) {
        List<Character> row = new ArrayList<>();

        for (int j = 0; j < firstPass.get(i).size(); j++) {
            char c = firstPass.get(i).get(j);
            if (c == '|' || c == '7' || c == 'F') {
                row.add('|');
            } else {
                if (i + 1 < firstPass.size()) {
                    char c2 = firstPass.get(i + 1).get(j);
                    if (c2 == '|' || c2 == 'L' || c2 == 'J') {
                        row.add('|');
                    } else {
                        row.add('.');
                    }
                } else {
                    row.add('.');
                }
            }
        }
        return row;
    }

    void downScale() {
        List<List<Character>> out = new ArrayList<>();
        for (int i=0; i + 1 < maze.size(); i += 2) {
            List<Character> row = new ArrayList<>();
            for (int j = 0; j + 1 < maze.get(0).size(); j += 2) {
                row.add(maze.get(i).get(j));
            }
            out.add(row);
        }

        maze = out;
    }

    String draw(List<Coordinate> coords) {
        StringBuilder sb = new StringBuilder();
        for (int i =0 ; i < maze.size(); i++) {
            for (int j = 0; j < maze.get(0).size(); j++) {
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

    String draw() {
        StringBuilder sb = new StringBuilder();
        for (int i =0 ; i < maze.size(); i++) {
            for (int j = 0; j < maze.get(0).size(); j++) {
                sb.append(maze.get(i).get(j));
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
