package com.jacobsevart.aoc;

import java.util.*;

public class SeedMaps {
    record Range(long from, long to, long length) {
    }

    ;

    record AlmanacMap(String from, String to, List<Range> ranges) {
    }

    ;

    record Key(String kind, long index) {
    }

    ;

    static long partOne(Scanner in) {
        // first read seeds
        String line = in.nextLine();
        assert line.startsWith("seeds: ");
        List<Long> seeds = readSeeds(new Scanner(line.substring("seeds: ".length())));
        System.out.printf("num seeds: %d\n", seeds.size());
        in.nextLine(); // burn empty line

        // seed indexes
        long[] mappings = new long[seeds.size()];
        String mappingsContain = "seed";
        for (int i = 0; i < mappings.length; i++) {
            mappings[i] = seeds.get(i);
        }

        // for each map, advance
        while (in.hasNextLine()) {
            AlmanacMap map = readNextMap(in);
            assert map.from.equals(mappingsContain);

            for (int i = 0; i < mappings.length; i++) {
//                long tmp = mappings[i];

                mappings[i] = advance(map, mappings[i]);

//                System.out.printf("%12s %5d -> %12s %5d\n", map.from, tmp, map.to, mappings[i]);
            }

            mappingsContain = map.to;
        }

        // finally we have seed -> location
        assert mappingsContain.equals("location");

        long minLocation = Long.MAX_VALUE;
        for (int i = 0; i < seeds.size(); i++) {
            System.out.printf("%2d -> %2d\n", seeds.get(i), mappings[i]);
            if (mappings[i] < minLocation) minLocation = mappings[i];
        }

        return minLocation;
    }

    static long advance(AlmanacMap map, long key) {
        for (Range r : map.ranges) {
            if (key < r.from || key >= r.from + r.length) continue;

            return r.to + (key - r.from);
        }

        return key;
    }

    static List<Long> readSeeds(Scanner line) {
        List<Long> seeds = new ArrayList<>();

        while (line.hasNextLong()) {
            seeds.add(line.nextLong());
        }
        return seeds;
    }

    static AlmanacMap readNextMap(Scanner in) {
        String from;
        String to;
        // read header
        String line = in.nextLine();
        assert line.contains("map");
        line = line.replace(" map:", "");
        String[] parts = line.split("-");
        assert parts.length == 3;
        from = parts[0];
        assert parts[1].equals("to");
        to = parts[2];

        List<Range> ranges = new ArrayList<>();
        while (in.hasNextLine()) {
            line = in.nextLine();
            if (line.isEmpty()) break;

            parts = line.split(" ");
            assert parts.length == 3;
            ranges.add(new Range(Long.parseLong(parts[1]), Long.parseLong(parts[0]), Long.parseLong(parts[2])));
        }

        return new AlmanacMap(from, to, ranges);
    }
}
