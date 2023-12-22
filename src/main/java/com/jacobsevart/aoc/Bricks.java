package com.jacobsevart.aoc;

import java.util.*;

public class Bricks {
    record Brick(int xmin, int xmax, int ymin, int ymax, int zmin, int zmax, int id) {
        Brick fall() {
            return new Brick(xmin, xmax, ymin, ymax, zmin - 1, zmax - 1, id);
        }

        @Override
        public String toString() {
            return String.format("%c", 'A' + id);
        }
    };

    LinkedList<Brick> bricks;

    public Bricks(Scanner in) {
        bricks = new LinkedList<>();

        int id = 0;
        while (in.hasNextLine()) {
            String[] brickSpec = in.nextLine().split("~");
            String[] partsLeft = brickSpec[0].split(",");
            String[] partsRight = brickSpec[1].split(",");

            Brick br = new Brick(Integer.parseInt(partsLeft[0]), Integer.parseInt(partsRight[0]),
                    Integer.parseInt(partsLeft[1]), Integer.parseInt(partsRight[1]),
                    Integer.parseInt(partsLeft[2]), Integer.parseInt(partsRight[2]), id++);

            bricks.add(br);
        }

        bricks.sort(Comparator.comparing(Brick::zmax));
    }

    public Map<Integer, List<Integer>> getSupportedBy(Map<Integer, List<Integer>> supports) {
        Map<Integer, List<Integer>> supportedBy = new HashMap<>();
        for (Brick brick : bricks) {
            supportedBy.put(brick.id, new ArrayList<>());
        }

        for (var entry : supports.entrySet()) {
            for (var supportee : entry.getValue()) {
                supportedBy.get(supportee).add(entry.getKey());
            }
        }

        return supportedBy;
    }

    public List<Integer> kahnTopo(Map<Integer, List<Integer>> supportsOriginal) {
        // copy the "supports" map since we will be deleting from it
        Map<Integer, List<Integer>> supports = new HashMap<>();
        for (var entry : supportsOriginal.entrySet()) {
            ArrayList<Integer> val = new ArrayList<>(entry.getValue());
            supports.put(entry.getKey(), val);
        }

        // need this for reference when iterating edges
        var supportedBy = getSupportedBy(supports);

        List<Integer> out = new ArrayList<>();
        Queue<Integer> q = new ArrayDeque<>();

        // add all nodes with zero in-degree
        for (var entry : supportedBy.entrySet()) {
            if (entry.getValue().isEmpty()) {
                q.add(entry.getKey());
            }
        }

        while (!q.isEmpty()) {
            Integer n = q.poll();
            out.add(n);

            Iterator<Integer> neighbors = supports.get(n).iterator();
            while (neighbors.hasNext()) {
                Integer m = neighbors.next();

                neighbors.remove(); // remove from forward map
                supportedBy.get(m).remove(n); // remove from backwards map

                if (supportedBy.get(m).isEmpty()) {
                    q.add(m);
                }
            }
        }

        return out;
    }

    public List<Integer> complement(List<Integer> given) {
        HashSet<Integer> givenSet = new HashSet<>(given);
        return bricks.stream().map(x -> x.id).filter(x -> !givenSet.contains(x)).toList();
    }

    public int partTwo() {
        var supports = drop();
        var startingPoints = complement(removable(supports));
        var topoOrder = kahnTopo(supports);
        var supportedBy = getSupportedBy(supports);

        int acc = 0;

        for (int startingPoint : startingPoints) {
            Set<Integer> component = new HashSet<>();
            component.add(startingPoint);

            boolean[] fallen = new boolean[bricks.size()];
            fallen[startingPoint] = true;

            for (int node : topoOrder.subList(topoOrder.indexOf(startingPoint), topoOrder.size())) {
                if (component.stream().noneMatch(x -> supports.get(x).contains(node))) {
                    continue;
                }

                if (!supportedBy.get(node).stream().allMatch(x -> fallen[x])) {
                    continue;
                }

                component.add(node);
                fallen[node] = true;
                acc++;
            }
        }

        return acc;
    }

    public List<Integer> removable(Map<Integer, List<Integer>> supports) {
        // want to find the bricks who are not the only supporter of their supporteees

        List<Integer> out = new ArrayList<>();
        var supportedBy = getSupportedBy(supports);

        for (var entry : supports.entrySet()) {
            boolean critical = false;
            for (var supportee : entry.getValue()) {
                // are we the only supporter?

                if (supportedBy.get(supportee).size() == 1) {
                    critical = true;
                    break;
                }
            }

            if (!critical) {
                out.add(entry.getKey());
            }
        }

        return out;
    }


    public Map<Integer, List<Integer>> drop() {
        Map<Integer, List<Integer>> supports = new HashMap<>();
        for (Brick brick : bricks) {
            supports.put(brick.id, new ArrayList<>());
        }

        for (int i = 0; i < bricks.size(); i++) { // for each brick
            if (i % 100 == 0) System.out.printf("%d / %d\n", i, bricks.size());

            while (bricks.get(i).zmin > 1) { // until settled
                Brick proposed = bricks.get(i).fall();

                boolean intersectAny = false;
                for (int j = 0; j < i; j++) { // compare to all lower bricks
                    if (touching(proposed, bricks.get(j))) {
                        intersectAny = true;

                        supports.get(bricks.get(j).id).add(bricks.get(i).id);
                    }
                }

                if (intersectAny) break;
                bricks.set(i, proposed);
            }
        }

        return supports;
    }

    static boolean touching(Brick a, Brick b) {
        if (a.xmin <= b.xmax && a.xmax >= b.xmin) {
            if (a.ymin <= b.ymax && a.ymax >= b.ymin) {
                return a.zmin <= b.zmax && a.zmax >= b.zmin;
            }
        }

        return false;
    }
}
