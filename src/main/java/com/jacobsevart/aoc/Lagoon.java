package com.jacobsevart.aoc;

import java.util.*;

public class Lagoon {
    record Edge(String direction, long distance) {};
    record Coordinate(long i, long j) {};

    List<Edge> edges;
    long pathLength;

    public Lagoon(List<Edge> edges) {
        this.edges = edges;
        pathLength = edges.stream().map(Edge::distance).reduce(0L, Long::sum);
    }

    static Lagoon ofPartOne(Scanner in) {
        List<Edge> edges = new ArrayList<>();
        while (in.hasNextLine()) {
            String direction = in.next();
            int distance = in.nextInt();
            in.next();

            edges.add(new Edge(direction, distance));
        }

        return new Lagoon(edges);
    }

    static Lagoon ofPartTwo(Scanner in) {
        List<Edge> edges = new ArrayList<>();
        while (in.hasNextLine()) {
            in.next(); // burn first col
            in.next(); // burn second col
            String spec = in.next()
                    .replace("(", "")
                    .replace(")", "")
                    .replace("#", "");

            int here = HexFormat.fromHexDigits(spec);
            int distance = here >> 4;

            String direction;
            switch (here & 0x00000F) {
                case 0:
                    direction = "R";
                    break;
                case 1:
                    direction = "D";
                    break;
                case 2:
                    direction = "L";
                    break;
                case 3:
                    direction = "U";
                    break;
                default:
                    throw new IllegalArgumentException("unknown direction");
            }



            edges.add(new Edge(direction, distance));
        }

        return new Lagoon(edges);
    }


    public long area() {
        // Pick's theorem! TIL.
        // A = i + (b / 2) + 1
        // A given by shoelace
        var interior = shoelace(trace()) - (pathLength / 2) + 1;

        return interior + pathLength;
    }

    long shoelace(List<Coordinate> coords) {
        long acc = 0;
        for (int i = 0; i + 1 < coords.size(); i++) {
            Coordinate a = coords.get(i);
            Coordinate b = coords.get(i + 1);

            long here = (a.i * b.j) - (a.j * b.i);
            acc += here;
        }

        Coordinate first = coords.get(0);
        Coordinate last = coords.get(coords.size() - 1);
        acc += ((long) last.i * first.j) - ((long) last.j * first.i);
        acc = Math.abs(acc);
        return acc / 2;
    }

    List<Coordinate> trace() {
        List<Coordinate> out = new ArrayList<>();
        long i = 0;
        long j = 0;

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

            out.add(new Coordinate(i, j));
        }

        return out;
    }

}
