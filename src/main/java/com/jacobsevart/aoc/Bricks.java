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
//            return String.format("%c x:(%d,%d), y:(%d,%d), z:(%d,%d)", 'A' + id, xmin, xmax, ymin, ymax, zmin, zmax);
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

    public List<Integer> removable(Map<Integer, List<Integer>> supports) {
        // want to find the bricks who are not the only supporter of their supporteees

        List<Integer> out = new ArrayList<>();

        Map<Integer, List<Integer>> supportedBy = new HashMap<>();
        for (var entry : supports.entrySet()) {
            for (var supportee : entry.getValue()) {
                if (!supportedBy.containsKey(supportee)) {
                    supportedBy.put(supportee, new ArrayList<>());
                }

                supportedBy.get(supportee).add(entry.getKey());
            }
        }


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
            System.out.println(i);
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
