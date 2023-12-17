package com.jacobsevart.aoc;

import java.util.*;
import java.util.stream.Stream;

public class ClumsyCrucible {
    List<List<Integer>> weights;
    record Coordinate(int x, int y) {};

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

    List<Coordinate> neighbors(Coordinate c) {
        return Stream.of(
                new Coordinate(c.x + 1, c.y),
                new Coordinate(c.x - 1, c.y),
                new Coordinate(c.x, c.y - 1),
                new Coordinate(c.x, c.y + 1)
        ).filter(this::boundsCheck).toList();
    }

    boolean boundsCheck(Coordinate c) {
        if (c.x < 0 || c.x >= weights.size()) return false;
        if (c.y < 0 || c.y >= weights.get(0).size()) return false;

        return true;
    }

    List<Coordinate> shortestPath() {
        PriorityQueue<Coordinate> q = new PriorityQueue<>(Comparator.comparing(c -> weights.get(c.x).get(c.y)));
        Map<Coordinate, Integer> dist = new HashMap<>();

        Coordinate origin = new Coordinate(0, 0);
        Coordinate dest = new Coordinate(weights.size() - 1, weights.get(0).size() - 1);
        dist.put(origin, 0);

        Map<Coordinate, Coordinate> prev = new HashMap<>();

        for (int i = 0; i < weights.size(); i++) {
            for (int j = 0; j < weights.get(0).size(); j++) {
                q.add(new Coordinate(i, j));
            }
        }

        while (!q.isEmpty()) {
            var u = q.poll();
            if (u.equals(dest) && prev.containsKey(dest)) break;

            for (Coordinate v : neighbors(u)) {
                int alt = dist.getOrDefault(u, Integer.MAX_VALUE - 10) + weights.get(v.x).get(v.y);
                if (alt < dist.getOrDefault(v, Integer.MAX_VALUE - 10)) {
                    dist.put(v, alt);
                    prev.put(v, u);

                    // decrease priority: remove and re-add
                    q.remove(v);
                    q.add(v);
                }
            }
        }

        // reconstruct path
        List<Coordinate> reversePath = new ArrayList<>();
        Coordinate ptr = dest;
        while (!ptr.equals(origin)) {
            ptr = prev.get(ptr);
            if (ptr == null) throw new RuntimeException();
            reversePath.add(ptr);
        }

        return reversePath.reversed();
    }

    String renderPath(List<Coordinate> pathList) {
        Set<Coordinate> pathSet = new HashSet<>(pathList);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < weights.size(); i++) {
            for (int j = 0; j < weights.get(0).size(); j++) {
                if (pathSet.contains(new Coordinate(i, j))) {
                    sb.append("*");
                } else {
                    sb.append(weights.get(i).get(j));
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
