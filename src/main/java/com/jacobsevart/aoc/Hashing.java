package com.jacobsevart.aoc;

import java.lang.reflect.Array;
import java.util.*;

public class Hashing {
    sealed interface Command permits MinusCommand, EqualsCommand {};
    record MinusCommand(String label) implements Command {};
    record EqualsCommand(String label, int focalLength) implements Command {};

    record Lens(String label, int focalLength) {
        @Override
        public String toString() {
            return String.format("[%s %d]", label, focalLength);
        }
    };

    List<List<Lens>> boxes;

    public Hashing() {
        boxes = new ArrayList<>(256);
        for (int i = 0; i < 256; i++) {
            boxes.add(new LinkedList<>());
        }
    }

    void execute(Command c) {
        switch(c) {
            case MinusCommand m:
                boxes.get(hash(m.label)).removeIf(x -> x.label.equals(m.label));
                break;
            case EqualsCommand e:
                var h = hash(e.label);
                for (int i = 0; i < boxes.get(h).size(); i++) {
                    if (boxes.get(h).get(i).label.equals(e.label)) {
                        boxes.get(h).set(i, new Lens(e.label, e.focalLength));
                        return;
                    }
                }

                // assume not found
                boxes.get(h).add(new Lens(e.label, e.focalLength));
        }
    }

    int focusingPower() {
        int acc = 0;
        for (int i = 0; i < boxes.size(); i++) {
            for (int j = 0; j < boxes.get(i).size(); j++) {
                acc += (i + 1) * (j + 1) * boxes.get(i).get(j).focalLength;
            }
        }

        return acc;
    }

    void render() {
        for (int i = 0; i < boxes.size(); i++) {
            if (!boxes.get(i).isEmpty()) {
                System.out.printf("box %d: %s\n", i, boxes.get(i));
            }
        }
    }

    public static List<Command> parse(Scanner in) {
        List<Command> cmds = new ArrayList<>();
        in.useDelimiter(",");
        while (in.hasNext()) {
            String cmd = in.next();

            if (cmd.contains("-")) {
                cmds.add(new MinusCommand(cmd.replace("-", "")));
            } else {
                String[] parts = cmd.split("=");
                cmds.add(new EqualsCommand(parts[0], Integer.parseInt(parts[1])));
            }
        }

        return cmds;
    }


    public static int hashMany(Scanner in) {
        int sum = 0;
        in.useDelimiter(",");
        while (in.hasNext()) {
            String s = in.next();
            int h = hash(s);
            sum += h;
        }

        return sum;
    }

    public static int hash(String in) {
        int cur = 0;
        for (char c : in.toCharArray()) {
            cur += c;
            cur *= 17;
            cur = cur % 256;
        }

        return cur;
    }
}
