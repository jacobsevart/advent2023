package com.jacobsevart.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SailboatRace {
    List<Race> races;
    record Race(long time, long distance) {};
    public SailboatRace(Scanner in)  {
        races = new ArrayList<>();
        Scanner times = new Scanner(in.nextLine());
        times.next("Time:");
        Scanner distances = new Scanner(in.nextLine());
        distances.next("Distance:");

        while (times.hasNextLong() && distances.hasNextLong()) {
            races.add(new Race(times.nextLong(), distances.nextLong()));
        }
    }

    long partOne() {
        return races
                .stream()
                .map(SailboatRace::waysToBeatRecord)
                .reduce(1L, (a, b) -> a * b);
    }

    static long waysToBeatRecord(Race race) {
        long ways = 0;
        for (int i = 0; i < race.time; i++) {
            long distance = (race.time - i) * i;
            if (distance > race.distance) ways++;
        }

        return ways;
    }


}
