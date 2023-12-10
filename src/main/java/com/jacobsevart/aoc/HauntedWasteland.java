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

    long reachSimulatenous() {
        return nodeSpecs
                .keySet()
                .stream()
                .filter(x -> x.endsWith("A"))
                .map(x -> reach(x, "Z"))
                .reduce(1L, HauntedWasteland::lcm);
    }

    static long gcd(long a, long b) {
        if (b == 0) return a;
        return gcd(Math.min(a, b), Math.max(a, b) % Math.min(a, b));
    }

    static long lcm(long a, long b) {
        return (a * b) / gcd(a, b);
    }

    long reach(String src, String target) {
        int cnt = 0;
        String ptr = src;
        while (true) {
            for (int i = 0; i < instructions.length && !ptr.endsWith(target); i++) {
                var node = nodeSpecs.get(ptr);
                if (instructions[i] == 'L') {
                    ptr = node.left;
                } else if (instructions[i] == 'R') {
                    ptr = node.right;
                } else {
                    throw new RuntimeException("invalid instruction");
                }
                cnt++;
            }

            if (ptr.endsWith(target)) return cnt;
        }
    }
}
