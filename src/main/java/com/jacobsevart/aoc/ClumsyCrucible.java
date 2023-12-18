package com.jacobsevart.aoc;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class ClumsyCrucible {
    List<List<Integer>> weights;
    record Coordinate(int x, int y) {};
    record Node(int x, int y, Direction direction) {};

    Map<Direction, Direction> opposites = new HashMap<>() {{
        put(Direction.UP, Direction.DOWN);
        put(Direction.DOWN, Direction.UP);
        put(Direction.LEFT, Direction.RIGHT);
        put(Direction.RIGHT, Direction.LEFT);
    }};


    public ClumsyCrucible(Scanner in) {
        weights = new ArrayList<>();
        while (in.hasNextLine()) {
            List<Integer> row = new ArrayList<>();
            for (char c : in.nextLine().toCharArray()) {
                row.add(c - '0');
            }
            weights.add(row);
        }
    }

    List<Node> neighborsPartTwo(Node c) {
        List<Node> potential = new ArrayList<>();

        for (int i = 4; i <= 10; i++) {
            potential.add(new Node(c.x + i, c.y, Direction.DOWN));
            potential.add(new Node(c.x - i, c.y, Direction.UP));
            potential.add(new Node(c.x, c.y - i, Direction.LEFT));
            potential.add(new Node(c.x, c.y + i, Direction.RIGHT));
        }

        return potential.stream().filter(x -> x.direction != c.direction)
                .filter(x -> opposites.get(c.direction) != x.direction)
                .filter(this::boundsCheck).toList();
    }

    List<Node> neighbors(Node c) {
        return Stream.of(
                new Node(c.x + 3, c.y, Direction.DOWN),
                new Node(c.x + 2, c.y, Direction.DOWN),
                new Node(c.x + 1, c.y, Direction.DOWN),
                new Node(c.x - 1, c.y, Direction.UP),
                new Node(c.x - 2, c.y, Direction.UP),
                new Node(c.x - 3, c.y, Direction.UP),
                new Node(c.x, c.y - 3, Direction.LEFT),
                new Node(c.x, c.y - 2, Direction.LEFT),
                new Node(c.x, c.y - 1, Direction.LEFT),
                new Node(c.x, c.y + 1, Direction.RIGHT),
                new Node(c.x, c.y + 2, Direction.RIGHT),
                new Node(c.x, c.y + 3, Direction.RIGHT)
        ).filter(x -> x.direction != c.direction)
                .filter(x -> opposites.get(c.direction) != x.direction)
                .filter(this::boundsCheck).toList();
    }

    int incrementalCost(Node a, Node b) {
        int cost = 0;

        if (b.direction == Direction.DOWN) {
            for (int i = b.x; i > a.x; i--) {
                cost += weights.get(i).get(b.y);
            }
        } else if (b.direction == Direction.UP) {
            for (int i = b.x; i < a.x; i++) {
                cost += weights.get(i).get(b.y);
            }
        } else if (b.direction == Direction.LEFT) {
            for (int i = b.y; i < a.y; i++) {
                cost += weights.get(b.x).get(i);
            }
        } else if (b.direction == Direction.RIGHT) {
            for (int i = b.y; i > a.y; i--) {
                cost += weights.get(b.x).get(i);
            }
        }

        return cost;
    }

    boolean boundsCheck(Node c) {
        if (c.x < 0 || c.x >= weights.size()) return false;
        if (c.y < 0 || c.y >= weights.get(0).size()) return false;

        return true;
    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    };

    static char directionChar(Direction d) {
        if (d == Direction.UP) return '^';
        if (d == Direction.DOWN) return 'v';
        if (d == Direction.LEFT) return '<';
        if (d == Direction.RIGHT) return '>';

        throw new RuntimeException();
    }

    record ShortestPath(List<Node> path, int cost) {};

    ShortestPath shortestPath(Function<Node, List<Node>> neighborMethod) {
        Map<Node, Integer> dist = new HashMap<>();
        PriorityQueue<Node> q = new PriorityQueue<>(Comparator.comparing(x -> dist.getOrDefault(x, Integer.MAX_VALUE)));

        for (Direction d : Direction.values()) {
            dist.put(new Node(0, 0, d), 0); // all directions out of the gate are potential starts
        }

        Map<Node, Node> prev = new HashMap<>();

        for (int i = 0; i < weights.size(); i++) {
            for (int j = 0; j < weights.get(0).size(); j++) {
                for (Direction d : Direction.values()) {
                    q.add(new Node(i, j, d));
                }
            }
        }
        System.out.println("Initialized");

        Node u = null;
        boolean found = false;
        int i = 0;
        int e = 0;
        while (!q.isEmpty()) {
            u = q.poll();
            i++;
            if (u.x == weights.size() - 1 && u.y == weights.get(0).size() - 1) {
                found = true;
                break;
            }

            for (Node v : neighborMethod.apply(u)) {
                e++;
                int alt = dist.getOrDefault(u, Integer.MAX_VALUE - 100) + incrementalCost(u, v);
                if (alt < dist.getOrDefault(v, Integer.MAX_VALUE - 100)) {
                    dist.put(v, alt);
                    prev.put(v, u);

                    // decrease priority: remove and re-add
                    q.remove(v);
                    q.add(v);
                }
            }
        }

        assert found;
        System.out.printf("Path cost: %d\n", dist.get(u));
        System.out.printf("Explored %d nodes %d edges\n", i, e);

        int cost = dist.get(u);

        // reconstruct path
        List<Node> reversePath = new ArrayList<>();
        reversePath.add(u);
        while (u.x != 0 || u.y != 0) {
            u = prev.get(u);
            if (u == null) throw new IllegalArgumentException();
            reversePath.add(u);
        }

        return new ShortestPath(reversePath, cost);
    }

    String renderPath(List<Node> path) {
        Map<Coordinate, Direction> coordDir = new HashMap<>();
        for (Node n : path) {
            assert !coordDir.containsKey(new Coordinate(n.x, n.y));

            coordDir.put(new Coordinate(n.x, n.y), n.direction);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < weights.size(); i++) {
            for (int j = 0; j < weights.get(0).size(); j++) {
                var coord = new Coordinate(i, j);
                if (coordDir.containsKey(coord)) {
                    sb.append(directionChar(coordDir.get(coord)));
                } else {
                    sb.append('.');
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    String render() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < weights.size(); i++) {
            for (int j = 0; j < weights.get(0).size(); j++) {
                sb.append(weights.get(i).get(j));
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
