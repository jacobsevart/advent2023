package com.jacobsevart.aoc;

import java.util.*;

public class SeedMaps {
    List<Long> seeds;
    List<AlmanacMap> maps;

    public SeedMaps(Scanner in) {
        // first read seeds
        String line = in.nextLine();
        this.seeds = readSeeds(new Scanner(line.substring("seeds: ".length())));

        // burn newline
        in.nextLine();

        // read maps
        maps = new ArrayList<>();
        while (in.hasNextLine()) maps.add(readNextMap(in));
    }

    record Remapping(long from, long to, long length) {
        long advanceKey(long key) {
            if (key < from || key >= from + length) {
                throw new RuntimeException("key out of range");
            }

            return key - from + to;
        }
    }

    ;

    record Range(long start, long end, long seed) {

    }

    ;

    record AlmanacMap(String from, String to, List<Remapping> ranges) {
    }

    ;

    long partTwo() {
        // interpret seeds as range
        List<Range> seeds = new ArrayList<>(this.seeds.size());
        for (int i = 0; i + 1 < this.seeds.size(); i += 2) {
            long start = this.seeds.get(i);
            long length = this.seeds.get(i + 1);
            seeds.add(new Range(start, start + length - 1, start));
        }

        long min = Long.MAX_VALUE;
        for (Range seed : seeds) {
            List<Range> ranges = new ArrayList<>() {{
                add(seed);
            }};

            for (AlmanacMap map : maps) {
                ranges = transformRanges(ranges, map.ranges, map.to);
            }

            for (Range range : ranges) {
                integrityCheck(range);

                if (range.start < min) {
                    System.out.printf("potential minimum: %s\n", range);
                    min = range.start;
                }
            }
        }

        return min;
    }

    public void integrityCheck(Range range) {
        // walk range.seed through the maps and make sure it matches range.start
        var idx = range.seed;
        for (var map : maps) {
            idx = advance(map, idx);
        }

        if (idx != range.start) {
            throw new RuntimeException(String.format("integrity check fail: f(%d) should be %d was %d\n", range.seed, idx, range.start));
        }
    }

    static List<Range> transformRanges(List<Range> start, List<Remapping> mapRanges, String dest) {
        start.sort(Comparator.comparing(Range::start));
        mapRanges.sort(Comparator.comparing(Remapping::from));

        long waterMark = 0;
        int startIndex = 0;
        int mapIndex = 0;
        List<Range> out = new ArrayList<>();

        while (startIndex < start.size() && mapIndex < mapRanges.size()) {
            Range startRange = start.get(startIndex);
            Remapping remapping = mapRanges.get(mapIndex);

            // check startRange is in order
            for (int i = 0; i + 1 < start.size(); i++) {
                var left = start.get(i);
                var right = start.get(i + 1);

                assert left.start <= left.end && left.end < right.start && right.start <= right.end;
            }

            // remapping is totally before startRange, skip
            if (remapping.from + remapping.length < startRange.start) {
                mapIndex++;
                continue;
            }

            // startRange is totally before mapping, add 1:1
            if (startRange.end < remapping.from) {
                out.add(startRange);
                waterMark = startRange.end + 1;
                startIndex++;

                assert advance(mapRanges, startRange.start) == startRange.start;
                assert advance(mapRanges, startRange.end) == startRange.end;

                continue;
            }

            // any leading unconsumed startRange
            if (startRange.start > waterMark && startRange.start < remapping.from) {
                if (!out.isEmpty() && startRange.start < out.getLast().end) {
                    throw new RuntimeException("out of order");
                }
                out.add(new Range(startRange.start, remapping.from, startRange.seed));
            }

            // use the mapping for the intersection of the mapping and the startRange
            long leftEdge = Math.max(Math.max(startRange.start, remapping.from), waterMark);
            long rightEdge = Math.min(startRange.end, remapping.from + remapping.length - 1);
            assert rightEdge - leftEdge >= 0;

            var range = new Range(remapping.advanceKey(leftEdge), remapping.advanceKey(rightEdge), leftEdge - startRange.start + startRange.seed);
            assert advance(mapRanges, leftEdge) == range.start;
            assert advance(mapRanges, rightEdge) == range.end;
            out.add(range);

            waterMark = rightEdge + 1;

            if (rightEdge >= startRange.end) startIndex++;
            if (range.end >= remapping.to + remapping.length - 1) mapIndex++;
        }

        // trailing unconsumed startRange 1:1
        for (; startIndex < start.size(); startIndex++) {
            var startRange = start.get(startIndex);
            var leftEdge = Math.max(startRange.start, waterMark);
            out.add(new Range(leftEdge, startRange.end, leftEdge - startRange.start + startRange.seed));
        }

        return out;
    }

    long partOne() {
        // seed indexes
        long[] mappings = new long[seeds.size()];
        String mappingsContain = "seed";
        for (int i = 0; i < mappings.length; i++) {
            mappings[i] = seeds.get(i);
        }

        // for each map, advance
        for (AlmanacMap map : maps) {
            assert map.from.equals(mappingsContain);

            for (int i = 0; i < mappings.length; i++) {
                mappings[i] = advance(map, mappings[i]);
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
        return advance(map.ranges, key);
    }

    static long advance(List<Remapping> ranges, long key) {
        for (Remapping r : ranges) {
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

        List<Remapping> ranges = new ArrayList<>();
        while (in.hasNextLine()) {
            line = in.nextLine();
            if (line.isEmpty()) break;

            parts = line.split(" ");
            assert parts.length == 3;
            ranges.add(new Remapping(Long.parseLong(parts[1]), Long.parseLong(parts[0]), Long.parseLong(parts[2])));
        }

        return new AlmanacMap(from, to, ranges);
    }
}
