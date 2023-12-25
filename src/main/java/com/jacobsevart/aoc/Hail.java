package com.jacobsevart.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Hail {
    public record Stone(long x, long y, long z, long dx, long dy, long dz) {
    }

    ;

    public record Coordinate(double x, double y) {
    }

    ;

    public record Collision(int i, int j, Coordinate position) {
    }

    ;

    List<Stone> hailstones = new ArrayList<>();

    public Hail(Scanner in) {
        while (in.hasNextLine()) {
            String line = in.nextLine();
            System.out.println(line);
            String[] parts = line.split(" @ ");

            String[] posParts = parts[0].split(", ");
            String[] velParts = parts[1].split(", ");

            hailstones.add(new Stone(
                    Long.parseLong(posParts[0].trim()),
                    Long.parseLong(posParts[1].trim()),
                    Long.parseLong(posParts[2].trim()),
                    Long.parseLong(velParts[0].trim()),
                    Long.parseLong(velParts[1].trim()),
                    Long.parseLong(velParts[2].trim())));
        }
    }

    public List<Collision> partOne(long testMin, long testMax) {
        List<Collision> found = new ArrayList<>();

        for (int i = 0; i < hailstones.size(); i++) {
            for (int j = i + 1; j < hailstones.size(); j++) {
                var got = crossPaths(hailstones.get(i), hailstones.get(j));
                if (got.isPresent()) {
                    var collision = got.get();
                    if (collision.x >= testMin && collision.x <= testMax && collision.y >= testMin && collision.y < testMax) {
                        found.add(new Collision(i, j, collision));
                    }
                }
            }
        }

        return found;
    }

    // Part Two solved using sympy, see day24_part2.py.

    public Optional<Coordinate> crossPaths(Stone a, Stone b) {
        if (b.dx * a.dy == (a.dx * b.dy)) return Optional.empty();

        double divisor = (b.dx * a.dy) - (a.dx * b.dy);

        double t1 = b.dy * (a.x - b.x) + b.dx * (b.y - a.y);
        t1 = t1 / divisor;

        double t2 = a.dy * (a.x - b.x) + a.dx * (b.y - a.y);
        t2 = t2 / divisor;

        if (t1 < 0 || t2 < 0) return Optional.empty();

        return Optional.of(new Coordinate(a.x + a.dx * t1, a.y + a.dy * t1));
    }
}
