package com.jacobsevart.aoc;

import java.util.*;

public class Circuits {
    enum Kind {
        Conjunction,
        Flipflop,
        Broadcast,
        Output
    }

    record Pulse(String src, String dest, boolean hi) {
        @Override
        public String toString() {
            String description = "high";
            if (!hi) {
                description = "low";
            }
            return String.format("%s -%s-> %s", src, description, dest);
        }
    };

    record Simulation(String pulses, int lowPulses, int highPulses) {};

    Map<String, List<String>> out;
    Map<String, List<String>> in;
    Map<String, Kind> kind;

    Map<String,Boolean> flipFlopState;
    Map<String,Map<String,Boolean>> conjunctionState;

    public Circuits(Scanner scan) {
        in = new HashMap<>();
        out = new HashMap<>();
        kind = new HashMap<>();

        while (scan.hasNextLine()) {
            String[] parts = scan.nextLine().split(" \\-\\> ");
            String[] destinations = parts[1].split(", ");
            String name = null;

            if (parts[0].startsWith("%")) {
                name = parts[0].substring(1);
                kind.put(name, Kind.Flipflop);
            } else if (parts[0].startsWith("&")) {
                name = parts[0].substring(1);
                kind.put(name, Kind.Conjunction);
            } else if (parts[0].equals("broadcaster")) {
                name = parts[0];
                kind.put(parts[0], Kind.Broadcast);
            }

            out.put(name, new ArrayList<>());

            for (String d : destinations) {
                out.get(name).add(d);

                if (!in.containsKey(d)) {
                    in.put(d, new ArrayList<>());
                }

                in.get(d).add(name);
            }
        }

        flipFlopState = new HashMap<>();
        conjunctionState = new HashMap<>();

        // initialize states
        for (String s : in.keySet()) {
            if (kind.get(s) == Kind.Flipflop) {
                flipFlopState.put(s, false);
            } else if (kind.get(s) == Kind.Conjunction) {
                conjunctionState.put(s, new HashMap<>());

                for (String inbound : in.get(s)) {
                    conjunctionState.get(s).put(inbound, false);
                }
            }
        }
    }

    long partOne() {
        long lo = 0;
        long hi = 0;
        for (int i = 0; i < 1000; i++) {
            Simulation sr = pressButton();
            lo += sr.lowPulses();
            hi += sr.highPulses();
        }

        return lo * hi;
    }



    Simulation pressButton() {
        StringBuilder sb = new StringBuilder();
        Queue<Pulse> q = new ArrayDeque<>();
        q.add(new Pulse("button", "broadcaster", false));

        int loPulses = 0;
        int hiPulses = 0;
        while (!q.isEmpty()) {
            Pulse p = q.poll();

            if (p.hi) {
                hiPulses++;
            } else {
                loPulses++;
            }

            String here = p.dest;
            sb.append(p);
            sb.append("\n");

            switch (kind.getOrDefault(here, Kind.Output)) {
                case Kind.Broadcast -> {
                    // broadcaster propagates pulse to each downstream
                    for (String onward : out.get(here)) {
                        q.add(new Pulse(here, onward, p.hi));
                    }
                }
                case Kind.Flipflop -> {
                    // flip-flop ignores hi
                    if (p.hi) continue;

                    // first toggle state
                    flipFlopState.put(here, !flipFlopState.get(here));

                    // then send new state onward
                    for (String onward : out.get(here)) {
                        q.add(new Pulse(here, onward, flipFlopState.get(here)));
                    }
                }
                case Kind.Conjunction -> {
                    // update the conjunction state depending on source
                    Map<String,Boolean> cs = conjunctionState.get(here);
                    cs.put(p.src, p.hi);

                    // onward pulse is lo if all input pluses are hi, else hi
                    boolean onwardPulse = !cs.values().stream().allMatch(x -> x);

                    // forward
                    for (String onward : out.get(here)) {
                        q.add(new Pulse(here, onward, onwardPulse));
                    }
                }
            }
        }

        return new Simulation(sb.toString(), loPulses, hiPulses);
    }
}
