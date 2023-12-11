package com.jacobsevart.aoc;

import java.util.*;

public class CosmicExpansion {
    public record Coordinate(long x, long y) {};

    List<List<Character>> grid;
    List<Integer> emptyRows = new ArrayList<>();
    List<Integer> emptyCols = new ArrayList<>();

    public CosmicExpansion(Scanner in) {
        grid = new ArrayList<>();
        while (in.hasNextLine()) {
            List<Character> row = new ArrayList<>();
            for (Character c : in.nextLine().toCharArray()) {
                row.add(c);
            }
            grid.add(row);
        }

        for (int i = 0; i < grid.size(); i++) {
            if (grid.get(i).stream().allMatch(x -> x == '.')) {
                emptyRows.add(i);
            }
        }

        for (int j = 0; j < grid.get(0).size(); j++) {
            var allEmpty = true;
            for (List<Character> row : grid) {
                if (row.get(j) != '.') {
                    allEmpty = false;
                    break;
                }
            }
            if (allEmpty) emptyCols.add(j);
        }
    }

    List<Coordinate> expandedGalaxies(long padding) {
        return galaxies().stream().map(galaxy -> {
            var cntEmptyRows = emptyRows.stream().filter(r -> r < galaxy.x).count();
            var cntEmptyCols = emptyCols.stream().filter(r -> r < galaxy.y).count();
            return new Coordinate(galaxy.x + cntEmptyRows * padding, galaxy.y + cntEmptyCols * padding);
        }).toList();
    }

    public void expand() {
        // first find empty cols
        Set<Integer> emptyCols = new HashSet<>();
        for (int j = 0; j < grid.get(0).size(); j++) {
            var allEmpty = true;
            for (List<Character> row : grid) {
                if (row.get(j) != '.') {
                    allEmpty = false;
                    break;
                }
            }
            if (allEmpty) emptyCols.add(j);
        }

        // rebuild rows
        List<List<Character>> newGrid = new ArrayList<>();

        for (List<Character> row : grid) {
            List<Character> newRow = new ArrayList<>();
            for (int j = 0; j < grid.get(0).size(); j++) {
                if (emptyCols.contains(j)) newRow.add('.');
                newRow.add(row.get(j));
            }

            newGrid.add(newRow);

            if (row.stream().allMatch(x -> x == '.')) {
                newGrid.add(newRow); // duplicate
            }
        }

        grid = newGrid;
    }

    public long sumDistances(List<Coordinate> galaxies) {
        long acc = 0;

        for (int i = 0; i + 1 < galaxies.size(); i++) {
            for (int j = i + 1; j < galaxies.size(); j++) {
                var path = shortestPath(galaxies.get(i), galaxies.get(j));
                acc += path;
            }
        }

        return acc;
    }

     long shortestPath(Coordinate src, Coordinate dest) {
        return Math.abs(dest.x - src.x) + Math.abs(dest.y - src.y);
     }

    List<Coordinate> galaxies() {
        List<Coordinate> out = new ArrayList<>();
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(0).size(); j++) {
                if (grid.get(i).get(j) == '#') out.add(new Coordinate(i, j));
            }
        }

        return out;
    }

    String draw() {
        StringBuilder sb = new StringBuilder();
        int g = 0;
        for (List<Character> characters : grid) {
            for (int j = 0; j < grid.get(0).size(); j++) {
                var c = characters.get(j);
                if (c == '#') {
                    sb.append(++g);
                } else {
                    sb.append(c);
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
