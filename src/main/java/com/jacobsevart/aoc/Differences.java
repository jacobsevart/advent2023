package com.jacobsevart.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Differences {
    List<List<Integer>> sequences = new ArrayList<>();
    public Differences(Scanner in) {
        while (in.hasNextLine()) {
            Scanner line = new Scanner(in.nextLine());
            List<Integer> seq = new ArrayList<>();
            while (line.hasNextInt()) {
                seq.add(line.nextInt());
            }
            sequences.add(seq);
        }
    }

    public int partOne() {
        return sequences.stream().map(Differences::extrapolate).reduce(0, Integer::sum);
    }

    public int partTwo() {
        return sequences.stream().map(List::reversed).map(Differences::extrapolate).reduce(0, Integer::sum);
    }

    static int extrapolate(List<Integer> sequence) {
        if (sequence.stream().allMatch(x -> x == 0)) return 0;

        List<Integer> differences = new ArrayList<>();
        for (int i = 0; i + 1< sequence.size(); i++) {
            differences.add(sequence.get(i + 1) - sequence.get(i));
        }

        int addend = extrapolate(differences);
        int last = sequence.getLast();

        return addend + last;
    }


}
