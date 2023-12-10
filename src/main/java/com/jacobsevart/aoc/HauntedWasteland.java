package com.jacobsevart.aoc;

import java.util.*;

public class HauntedWasteland {
    record NodeSpec(String id, String left, String right) {};
    Map<String, NodeSpec> nodeSpecs = new HashMap<>();
    String startNode = null;
    char[] instructions;
    public HauntedWasteland(Scanner in) {
        instructions = in.nextLine().toCharArray();
        in.nextLine(); // burn empty

        while (in.hasNextLine()) {
            String nodeID = in.next();
            if (startNode == null) startNode = nodeID;
            in.next("=");
            String left = in.next();
            left = left.replace(",", "").replace("(", "");
            String right = in.next();
            right = right.replace(")", "");

            if (nodeSpecs.containsKey(nodeID)) throw new RuntimeException("duplicate key");
            nodeSpecs.put(nodeID, new NodeSpec(nodeID, left, right));
        }
    }

    long reach(String src, String target) {
        int cnt = 0;
        String ptr = src;
        while (true) {
            for (int i = 0; i < instructions.length && !ptr.equals(target); i++) {
                var node = nodeSpecs.get(ptr);
                System.out.printf("node: %s ", node);
                if (instructions[i] == 'L') {
                    System.out.printf("left\n");
                    ptr = node.left;
                } else if (instructions[i] == 'R') {
                    System.out.printf("right\n");
                    ptr = node.right;
                } else {
                    throw new RuntimeException("invalid instruction");
                }
                cnt++;
            }

            if (ptr.equals(target)) return cnt;
        }
    }
}
