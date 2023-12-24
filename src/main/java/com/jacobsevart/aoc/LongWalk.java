package com.jacobsevart.aoc;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class LongWalk {
    List<List<Character>> grid;

    public record Coordinate(int x, int y) {
        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }
    };
    public record CoordinateWithLength(int x, int y, int length) {
        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }
    };

    Coordinate startNode;
    Coordinate endNode;

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

        startNode = new LongWalk.Coordinate(0, 1);
        endNode =  new LongWalk.Coordinate(grid.size() - 1, grid.get(0).size() - 2);
    }

    int partTwo() {
        var reduction = discoverNodes();
        discoverAdditionalEdges(reduction);
        System.out.printf("number of nodes: %d\n", reduction.size());

        for (var entry : reduction.entrySet()) {
            for (var node : entry.getValue()) {
                System.out.printf("\"%s\" -> \"%s\" [label=%d];\n", entry.getKey(), node, node.length);
            }
        }

        int edges = reduction.values().stream().map(Set::size).reduce(0, Integer::sum);
        System.out.printf("number of edges: %d\n", edges);

        var paths = allPaths(reduction);
        System.out.printf("number of paths: %d\n", paths.size());

        return paths.stream().map(x -> x.stream().map(CoordinateWithLength::length).reduce(0, Integer::sum)).max(Comparator.naturalOrder()).get();
    }

    List<List<CoordinateWithLength>> allPaths(Map<Coordinate, Set<CoordinateWithLength>> edges) {
        List<List<CoordinateWithLength>> allPaths = new ArrayList<>();

        dfsAllPaths(new CoordinateWithLength(startNode.x, startNode.y, 0), new HashSet<>(), new ArrayList<>(), allPaths, edges);

        return allPaths;
    }

    private void dfsAllPaths(CoordinateWithLength ptr, Set<Coordinate> pathSet, List<CoordinateWithLength> path, List<List<CoordinateWithLength>> collector, Map<Coordinate, Set<CoordinateWithLength>> edges) {
        if (pathSet.contains(new Coordinate(ptr.x, ptr.y))) return;

        Coordinate c = new Coordinate(ptr.x, ptr.y);

        path = new ArrayList<>(path);
        path.add(ptr);

        pathSet = new HashSet<>(pathSet);
        pathSet.add(c);

        if (ptr.x == endNode.x && ptr.y == endNode.y) {
            if (collector.size() % 100_000 == 0) System.out.println(collector.size());
            collector.add(path);
            return;
        }

        for (var edge : edges.get(c)) {
            dfsAllPaths(edge, pathSet, path, collector, edges);
        }
    }

    public Map<Coordinate, Set<CoordinateWithLength>> reduceGraph(Function<Coordinate, Stream<Coordinate>> neighborFinder) {
        Map<Coordinate, Set<CoordinateWithLength>> edges = new HashMap<>();
        Set<Coordinate> visited = new HashSet<>();

        reduceGraph(startNode, startNode, 0, visited, edges, neighborFinder);
//        checkDag(start, edges, new ArrayList<>());

        return edges;
    }

    public void reduceGraph(Coordinate start, Coordinate here, int length, Set<Coordinate> visited, Map<Coordinate, Set<CoordinateWithLength>> edges, Function<Coordinate, Stream<Coordinate>> neighborFinder) {
        if (visited.contains(here)) return;

        visited.add(here);

        if (here.equals(endNode)) {
            if (!edges.containsKey(start)) edges.put(start, new HashSet<>());

            edges.get(start).add(new CoordinateWithLength(here.x, here.y, length));
            edges.put(here, new HashSet<>()); // no outward edges, but so it will show up
        }

        List<Coordinate> uniqueNeighbors = boundsChecked(neighborFinder.apply(here)).stream().filter(x -> !visited.contains(x)).filter(x -> !x.equals(start)).toList();
        if (uniqueNeighbors.isEmpty()) {
            visited.remove(here);
            return;
        }

        if (uniqueNeighbors.size() == 1) {
            reduceGraph(start, uniqueNeighbors.get(0), length + 1, visited, edges, neighborFinder);
            visited.remove(here);
            return;
        }

        CoordinateWithLength newLink = new CoordinateWithLength(here.x, here.y, length + 1);
        if (!edges.getOrDefault(start, Set.of()).contains(newLink)) {
            System.out.printf("add edge! %s -> %s\n", start, here);
        }

        edges.putIfAbsent(start, new HashSet<>());
        edges.get(start).add(newLink);
        for (Coordinate n : uniqueNeighbors) {
            reduceGraph(here, n, 0, visited, edges, neighborFinder);
        }

        visited.remove(here);
    }

    public Map<Coordinate, Set<CoordinateWithLength>> discoverNodes() {
        Map<Coordinate, Set<CoordinateWithLength>> edges = new HashMap<>();
        Set<Coordinate> visited = new HashSet<>();

        record StackFrame(Coordinate start, Coordinate here, int length) {};

        Stack<StackFrame> stk = new Stack<>();
        stk.add(new StackFrame(startNode, startNode, 0));

        while (!stk.isEmpty()) {
            StackFrame frame = stk.pop();
            visited.add(frame.here);

            if (frame.here.equals(endNode)) {
                edges.putIfAbsent(frame.start, new HashSet<>());

                edges.get(frame.start).add(new CoordinateWithLength(frame.here.x, frame.here.y, frame.length));
                edges.put(frame.here, new HashSet<>()); // no outward edges, but so it will show up
                continue;
            }

            List<Coordinate> uniqueNeighbors = boundsChecked(neighborsPartTwo(frame.here)).stream().filter(x -> !visited.contains(x)).filter(x -> !x.equals(frame.here)).toList();
            if (uniqueNeighbors.isEmpty()) continue;

            if (uniqueNeighbors.size() == 1) {
                stk.push(new StackFrame(frame.start, uniqueNeighbors.get(0), frame.length + 1));
            } else {
                edges.putIfAbsent(frame.start, new HashSet<>());
                edges.get(frame.start).add(new CoordinateWithLength(frame.here.x,frame. here.y, frame.length));

                for (Coordinate n : uniqueNeighbors) {
                    stk.push(new StackFrame(frame.here, n, 0));
                }
            }
        }

        return edges;
    }

    public void discoverAdditionalEdges(Map<Coordinate, Set<CoordinateWithLength>> edges) {
        Set<LongWalk.Coordinate> nodeSet = new HashSet<>();
        for (var entry : edges.entrySet()) {
            nodeSet.add(entry.getKey());

            for (var node : entry.getValue()) {
                nodeSet.add(new LongWalk.Coordinate(node.x(), node.y()));
            }
        }

        for (var node : nodeSet) {
            checkConnectivity(node, nodeSet, edges);
        }
    }

    void checkConnectivity(Coordinate from, Set<Coordinate> stoppingPoints, Map<Coordinate, Set<CoordinateWithLength>> edges) {
        Set<Coordinate> visited = new HashSet<>();
        Stack<CoordinateWithLength> stk = new Stack<>();

        stk.add(new CoordinateWithLength(from.x, from.y, 0));

        while (!stk.isEmpty()) {
            CoordinateWithLength frame = stk.pop();

            Coordinate c = new Coordinate(frame.x, frame.y);
            if (visited.contains(c))     continue;
            visited.add(c);

            if (stoppingPoints.contains(c) && !c.equals(from)) {

                edges.putIfAbsent(from, new HashSet<>());

                Optional<CoordinateWithLength> found = edges.get(from).stream().filter(x -> x.x == frame.x && x.y == frame.y).findAny();
                if (found.isPresent()) {
                    if (frame.length > found.get().length) {
                        edges.get(from).remove(found.get());
                        edges.get(from).add(frame);
                    }

                    // avoid adding duplicate

                } else {
                    edges.get(from).add(frame);
                    System.out.printf("discover new edge %s -> %s\n", from, c);
                }

                continue; // can go further from another traversal
            }

            for (Coordinate n : boundsChecked(neighborsPartTwo(c))) {
                stk.add(new CoordinateWithLength(n.x, n.y, frame.length + 1));
            }
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

    Stream<Coordinate> neighborsPartOne(Coordinate c) {
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

    Stream<Coordinate> neighborsPartTwo(Coordinate c) {
        return Stream.of(
                new Coordinate(c.x - 1, c.y),
                new Coordinate(c.x + 1, c.y),
                new Coordinate(c.x, c.y -1),
                new Coordinate(c.x, c.y + 1));
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
