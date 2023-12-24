package com.jacobsevart.aoc;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    int longestPath(Function<Coordinate, Stream<Coordinate>> neighborProvider) {
        // first, DFS the graph to discover the nodes where we have an actual choice
        var reduction = discoverNodes(neighborProvider);

        // second, explore from all sources to discover additional links into these nodes that were suppressed by the "visited" set originally
        discoverAdditionalEdges(reduction, neighborProvider);

        System.out.printf("number of nodes: %d\n", reduction.size());

        // dot visualization
        for (var entry : reduction.entrySet()) {
            for (var node : entry.getValue()) {
                System.out.printf("\"%s\" -> \"%s\" [label=%d];\n", entry.getKey(), node, node.length);
            }
        }

        // count edges
        int edges = reduction.values().stream().map(Set::size).reduce(0, Integer::sum);
        System.out.printf("number of edges: %d\n", edges);

        // there's no longest-simple-path algorithm, just DFS to produce all paths & choose the max length
        var paths = allPaths(reduction);
        System.out.printf("number of paths: %d\n", paths.size());

        var longestPath = paths
                .stream()
                .max(Comparator.comparing(LongWalk::pathLength))
                .get();

        for (CoordinateWithLength node : longestPath) {
            grid.get(node.x).set(node.y, '*');
        }

        System.out.println(render());

        return pathLength(longestPath);
    }

    static int pathLength(List<CoordinateWithLength> path) {
        return path.stream().map(x -> x.length).reduce(0, Integer::sum);
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


    public Map<Coordinate, Set<CoordinateWithLength>> discoverNodes(Function<Coordinate, Stream<Coordinate>> neighborFinder) {
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

            List<Coordinate> uniqueNeighbors = boundsChecked(neighborFinder.apply(frame.here)).stream().filter(x -> !visited.contains(x)).filter(x -> !x.equals(frame.here)).toList();
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

    public void discoverAdditionalEdges(Map<Coordinate, Set<CoordinateWithLength>> edges, Function<Coordinate, Stream<Coordinate>> neighborProvider) {
        // In the first pass we visited each edge once, but edges may have in-degree > 1. A full graph traversal takes too long
        // but we can run an exploration from each node with a fresh Visited set, stopping when we reach any other turning point.
        Set<LongWalk.Coordinate> nodeSet = new HashSet<>();
        for (var entry : edges.entrySet()) {
            nodeSet.add(entry.getKey());

            for (var node : entry.getValue()) {
                nodeSet.add(new LongWalk.Coordinate(node.x(), node.y()));
            }
        }

        for (var from : nodeSet) {
            Set<Coordinate> visited = new HashSet<>();
            Stack<CoordinateWithLength> stk = new Stack<>();

            stk.add(new CoordinateWithLength(from.x, from.y, 0));

            while (!stk.isEmpty()) {
                CoordinateWithLength frame = stk.pop();

                Coordinate c = new Coordinate(frame.x, frame.y);
                if (visited.contains(c)) continue;
                visited.add(c);

                if (nodeSet.contains(c) && !c.equals(from)) {
                    edges.putIfAbsent(from, new HashSet<>());

                    // look for the same edge with a potentialy different length
                    Optional<CoordinateWithLength> found = edges.get(from).stream().filter(x -> x.x == frame.x && x.y == frame.y).findAny();

                    // if our length is longer, we win
                    if (found.isPresent() && frame.length > found.get().length) {
                        edges.get(from).remove(found.get());
                        edges.get(from).add(frame);
                    } else if (found.isEmpty()) {
                        edges.get(from).add(frame);
                    }

                    // note: if our length is the same or worse, don't bother

                    // don't explore past known nodes; we'll call again from all nodes as source
                    continue;
                }

                for (Coordinate n : boundsChecked(neighborProvider.apply(c))) {
                    stk.add(new CoordinateWithLength(n.x, n.y, frame.length + 1));
                }
            }
        }
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
