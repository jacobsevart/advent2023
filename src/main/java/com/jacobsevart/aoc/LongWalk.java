package com.jacobsevart.aoc;

import java.util.*;
import java.util.stream.Stream;

public class LongWalk {
    List<List<Character>> grid;

    public record Coordinate(int x, int y) {};
    public record CoordinateWithLength(int x, int y, int length) {};

    public LongWalk(Scanner in) {
        grid = new ArrayList<>();
        while (in.hasNextLine()) {
            String line = in.nextLine();

            List<Character> row = new ArrayList<>();
            for (char c : line.toCharArray()) {
                row.add(c);
            }

            grid.add(row);
        }
    }

    public Map<Coordinate, Set<CoordinateWithLength>> reduceDag(Coordinate start, Coordinate dest) {
        Map<Coordinate, Set<CoordinateWithLength>> edges = new HashMap<>();
        Set<Coordinate> visited = new HashSet<>();
        visited.add(start);

        reduceDag(start, start, 0, dest, visited, edges);
        checkDag(start, edges, new ArrayList<>());

        return edges;
    }

    public void reduceDag(Coordinate start, Coordinate here, int length, Coordinate dest, Set<Coordinate> visitedOriginal, Map<Coordinate, Set<CoordinateWithLength>> edges) {
        var visited = new HashSet<>(visitedOriginal);
        visited.add(here);

        if (here.equals(dest)) {
            if (!edges.containsKey(start)) edges.put(start, new HashSet<>());

            edges.get(start).add(new CoordinateWithLength(here.x, here.y, length));
            edges.put(here, new HashSet<>()); // no outward edges, but so it will show up
        }

        List<Coordinate> uniqueNeighbors = boundsChecked(neighbors(here)).stream().filter(x -> !visited.contains(x)).filter(x -> !x.equals(start)).toList();
        if (uniqueNeighbors.isEmpty()) return;

        if (uniqueNeighbors.size() == 1) {
            reduceDag(start, uniqueNeighbors.get(0), length + 1, dest, visited, edges);
            return;
        }

        edges.putIfAbsent(start, new HashSet<>());
        edges.get(start).add(new CoordinateWithLength(here.x, here.y, length + 1));
        for (Coordinate n : uniqueNeighbors) {
            reduceDag(here, n, 0, dest, visited, edges);
        }
    }

    public void checkDag(Coordinate ptr, Map<Coordinate, Set<CoordinateWithLength>> edges, List<Coordinate> path) {
        if (path.contains(ptr)) throw new RuntimeException(String.format("not a dag: %s :  %s", ptr, path));

        path.add(ptr);

        if (!edges.containsKey(ptr)) {
            System.out.printf("no edges out of %s\n", ptr);
            return;
        }

        for (var edge : edges.get(ptr)) {
            checkDag(new Coordinate(edge.x, edge.y), edges, path);
        }

        path.remove(ptr);
    }

    List<Coordinate> boundsChecked(Stream<Coordinate> s) {
        return s
                .filter(x -> x.x >= 0 && x.x < grid.size() && x.y >= 0 && x.y < grid.get(0).size())
                .filter(x -> grid.get(x.x).get(x.y) != '#')
                .toList();
    }

    Stream<Coordinate> neighbors(Coordinate c) {
        switch (grid.get(c.x).get(c.y)) {
            case '.' -> {
                return Stream.of(new Coordinate(c.x - 1, c.y), new Coordinate(c.x + 1, c.y), new Coordinate(c.x, c.y -1), new Coordinate(c.x, c.y + 1));
            }
            case '>' -> {
                return Stream.of(new Coordinate(c.x, c.y + 1));
            }
            case '<' -> {
                return Stream.of(new Coordinate(c.x, c.y - 1));
            }
            case 'v' -> {
                return Stream.of(new Coordinate(c.x + 1, c.y));
            }
            case '^' -> {
                return Stream.of(new Coordinate(c.x - 1, c.y));
            }
            default ->  {
                throw new RuntimeException(String.format("unknown char %c", grid.get(c.x).get(c.y)));
            }
        }
    }

    public int longestPath(Coordinate origin, Coordinate destination, Map<Coordinate, Set<CoordinateWithLength>> edges) {
        Map<Coordinate, Integer> paths = new HashMap<>();
        Map<Coordinate, Coordinate> prev = new HashMap<>();

        int longest = longestPath(origin, destination, paths, prev, edges);

        Coordinate ptr = destination;
        while (ptr != origin) {
            ptr = prev.get(ptr);
            if (ptr == null) {
                break;
            }
            grid.get(ptr.x).set(ptr.y, 'O');
        }

        return longest;
    }

    private static int longestPath(Coordinate origin, Coordinate destination, Map<Coordinate, Integer> paths, Map<Coordinate, Coordinate> prev, Map<Coordinate, Set<CoordinateWithLength>> edges) {
        // https://jeffe.cs.illinois.edu/teaching/algorithms/book/06-dfs.pdf

        if (origin.equals(destination)) return 0;

        if (!paths.containsKey(origin)) {
            paths.put(origin, Integer.MIN_VALUE);

            for (CoordinateWithLength w : edges.getOrDefault(origin, Set.of())) {
                int replace = w.length + longestPath(new Coordinate(w.x, w.y), destination, paths, prev, edges);

                if (replace > paths.get(origin)) {
                    paths.put(origin, replace);
                    prev.put(new Coordinate(w.x, w.y), origin);
                }
            }

        }

        return paths.get(origin);
    }

    String render(Map<Coordinate, Set<CoordinateWithLength>> edges) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(0).size(); j++) {
                if (edges.containsKey(new Coordinate(i, j))) {
                    sb.append("*");
                } else {
                    sb.append(grid.get(i).get(j));
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    String render() {
        StringBuilder sb = new StringBuilder();
        for (var row : grid) {
            for (var c : row) {
                sb.append(c);
            }
            sb.append("\n");
        }

        return sb.toString();
    }

}
