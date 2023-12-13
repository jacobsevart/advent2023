package com.jacobsevart.aoc;

import java.util.*;

public class SpringConditions {
    record Row(List<Run> conditions, int[] runs) {};
    record Run(char c, int length) {};

    List<Row> rows = new ArrayList<>();


    public SpringConditions(Scanner in) {
        while (in.hasNextLine()) {
            String springs = in.next();
            String possibles = in.next();
            String[] nums = possibles.split(",");
            int[] runs = new int[nums.length];
            for (int i = 0; i < nums.length; i++) {
                runs[i] = Integer.parseInt(nums[i]);
            }

            rows.add(new Row(runs(springs), runs));
        }
    }

    long partOne() {
        return rows
                .stream()
                .map(SpringConditions::possibilities)
                .reduce(0, Integer::sum);
    }

    long partTwo() {
        return rows
                .stream()
                .map(SpringConditions::repeat)
                .map(SpringConditions::possibilities)
                .reduce(0, Integer::sum);
    }

    static Row repeat(Row r) {
        List<Run> conditions = new ArrayList<>();
        for (int i = 0; i < r.conditions.size(); i++) {
            conditions.addAll(r.conditions);
            if (i + i < r.conditions.size()) conditions.add(new Run('?', 1));
        }

        int[] runs = new int[5 * r.runs.length];
        for (int i = 0; i < 5 * r.runs.length; i++) {
            runs[i] = r.runs[i % r.runs.length];
        }

        return new Row(conditions, runs);
    }

    static int possibilities(Row row) {
        int sz = row.conditions.stream().map(Run::length).reduce(Integer::sum).get();

        int n = 0;
        for (var possibility : generate(sz, row.runs, 0)) {
            boolean isPossible = check(row.conditions, possibility);
            System.out.printf("%s : %s\n", render(possibility), isPossible);

            if (isPossible) n++;
        }

        System.out.printf("%s: %d\n", render(row.conditions), n);
        return n;
    }

    static List<Run> runs(String possibles) {
        List<Run> charRuns = new ArrayList<>();
        char prev = possibles.charAt(0);
        int n = 1;
        for (int i = 1 ; i < possibles.length(); i++) {
            if (possibles.charAt(i) != prev) {
                charRuns.add(new Run(prev, n));
                n = 0;
            }
            prev = possibles.charAt(i);
            n++;
        }
        charRuns.add(new Run(prev, n));

        return charRuns;
    }

    public static boolean check(String record, String possible) {
        return check(runs(record), runs(possible));
    }

    static boolean check(List<Run> record, List<Run> possible) {
        String actual = render(record);
        String proposed = render(possible);

        for (int i = 0; i < actual.length(); i++) {
            char a = actual.charAt(i);
            if (a == '?') continue;
            if (a != proposed.charAt(i)) return false;
        }

        return true;
    }

    public static List<List<Run>> generate(int n, int[] ks, int x) {
        List<List<Run>> out = new ArrayList<>();
        if (x == ks.length) return out;
        if (n < ks[x]) return out;

        int k = ks[x];

        if (x == ks.length - 1) {
            for (int i = 0; i + k <= n; i++) {
                List<Run> row = new ArrayList<>();
                if (i > 0) row.add(new Run('.', i));
                row.add(new Run('#', k));
                if (i + k < n) row.add(new Run('.', n - i - k));
                out.add(row);
            }

            return out;
        }

        for (int i = 0; i + k <= n; i++) {
            for (List<Run> partial : generate(n - k - i - 1, ks, x + 1)) {
                List<Run> row = new ArrayList<>();
                if (i > 0) row.add(new Run('.', i));
                row.add(new Run('#', k));
                row.add(new Run('.', 1));
                row.addAll(partial);
                out.add(row);
            }
        }

        return out;
    }

    public static String render(List<Run> runs) {
        StringBuilder sb = new StringBuilder();
        for (Run r : runs) {
            for (int i = 0; i < r.length; i++) sb.append(r.c);
        }

        return sb.toString();
    }


}
