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

    record Simulation(String pulses, int lowPulses, int highPulses, int period) {};

    Map<String, List<String>> out;
    Map<String, List<String>> in;
    Map<String, Kind> kind;

    Map<String,Boolean> flipFlopState;
    Map<String,Map<String,Boolean>> conjunctionState;
    long ticks = 0;
    int buttonPresses;
    int watchButton;

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

        resetState();
    }

    void resetState() {
        flipFlopState = new HashMap<>();
        conjunctionState = new HashMap<>();
        ticks = 0L;
        buttonPresses = 0;
        watchButton = 0;

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

    long partTwo() {
        long lcm = 1L;
        for (String connection : in.get("nr")) {
            for (int i = 0; i < 5000; i++) {
                int p = pressButton(false, connection).period;
                if (p > 0) {
                    lcm = HauntedWasteland.lcm(lcm, p);
                    break;
                }
            }

            resetState();
        }

        return lcm;
    }


    Simulation pressButton() {
        return pressButton(true, "");
    }

    Simulation pressButton(boolean log, String watch) {
        buttonPresses++;

        StringBuilder sb = new StringBuilder();
        Queue<Pulse> q = new ArrayDeque<>();
        q.add(new Pulse("button", "broadcaster", false));

        boolean watchState = !watch.isEmpty() && isConjunctionOutputHigh(watch);

        int loPulses = 0;
        int hiPulses = 0;
        while (!q.isEmpty()) {
            Pulse p = q.poll();
            ticks++;

            if (!watch.isEmpty() && isConjunctionOutputHigh(watch) != watchState) {
                if (watchButton > 0) {
                    return new Simulation(sb.toString(), loPulses, hiPulses, buttonPresses - watchButton + 1);
                }

                watchButton = buttonPresses;
                watchState = !watchState;
            }

            if (p.hi) {
                hiPulses++;
            } else {
                loPulses++;
            }

            String here = p.dest;

            if (log) {
                sb.append(p);
                sb.append("\n");
            }

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
                    conjunctionState.get(here).put(p.src, p.hi);

                    // onward pulse is lo if all input pluses are hi, else hi
                    boolean onwardPulse = isConjunctionOutputHigh(here);

                    // forward
                    for (String onward : out.get(here)) {
                        q.add(new Pulse(here, onward, onwardPulse));
                    }
                }
            }
        }

        return new Simulation(sb.toString(), loPulses, hiPulses, 0);
    }

    boolean isConjunctionOutputHigh(String s) {
        return !conjunctionState.get(s).values().stream().allMatch(x -> x);
    }
}
