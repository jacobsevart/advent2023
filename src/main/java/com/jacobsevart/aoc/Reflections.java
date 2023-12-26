package com.jacobsevart.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Reflections {
    static List<List<Character>> parse(Scanner in) {
        List<List<Character>> chars = new ArrayList<>();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.isEmpty()) break;
            List<Character> row = new ArrayList<>();

            for (char c : line.toCharArray()) {
                row.add(c);
            }
            chars.add(row);
        }

        return chars;
    }

    static int partOne(Scanner in) {
        int acc = 0;
        while (in.hasNextLine()) {
            var chars = parse(in);
            var vertical = findVerticalReflection(chars, 0, -1);
            if (vertical >= 0) acc += vertical + 1;

            var horiz = findHorizontalReflection(chars, 0, -1);
            if (horiz >= 0) acc += 100 * (horiz + 1);
        }

        return acc;
    }

    static int partTwo(Scanner in) {
        int acc = 0;
        while (in.hasNextLine()) {
            var chars = parse(in);
            var vertical = findVerticalReflection(chars, 0, -1);
            var vertical2 = findVerticalReflection(chars, 1, vertical);
            if (vertical2 >= 0) acc += vertical2 + 1;

            var horiz = findHorizontalReflection(chars, 0, -1);
            var horiz2 = findHorizontalReflection(chars, 1, horiz);
            if (horiz2 >= 0) acc += 100 * (horiz2 + 1);
        }

        return acc;
    }

    static int arrayDiff(char[] a, char[] b) {
        if (a.length != b.length) throw new RuntimeException();

        int diffs = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) diffs++;
        }

        return diffs;
    }

    static int listDiff(List<Character> a, List<Character> b) {
        if (a.size() != b.size()) throw new RuntimeException();

        int diffs = 0;
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) != b.get(i)) diffs++;
        }

        return diffs;
    }

    static int findVerticalReflection(List<List<Character>> in, int tolerance, int alreadyKnow) {
        char[][] cols = new char[in.get(0).size()][];
        for (int j = 0; j < in.get(0).size(); j++) {
            // extract col
            char[] col = new char[in.size()];
            for (int i = 0; i < in.size(); i++) {
                col[i] = in.get(i).get(j);
            }
            cols[j] = col;
        }

        for (int j = 0; j + 1 < cols.length; j++) {
            if (j == alreadyKnow) continue;

            int coldiff = arrayDiff(cols[j], cols[j + 1]);
            if (tolerance - coldiff >= 0) {
                boolean confirmed = true;
                for (int offset = 1; j - offset >= 0 && j + 1 + offset < cols.length; offset++) {

                    int checkDiff = arrayDiff(cols[j - offset], cols[j + 1 + offset]);

                    if (tolerance - coldiff - checkDiff < 0) {
                        confirmed = false;
                        break;
                    }
                }

                if (confirmed) {
                    return j;
                }
            }
        }

        return -1;
    }

    static int findHorizontalReflection(List<List<Character>> in, int tolerance, int alreadyKnow) {
        for (int i = 0; i + 1 < in.size(); i++) {
            if (i == alreadyKnow) continue;

            int rowdiff = listDiff(in.get(i), in.get(i + 1));

            if (tolerance - rowdiff >= 0) {
                // candidate
                // check i - 1 vs. i + 2, i - 2 vs. i + 3

                boolean confirmed = true;
                for (int offset = 1; i - offset >= 0 && i + offset < in.size() - 1; offset++) {

                    int checkdiff = listDiff(in.get(i - offset), in.get(i + 1 + offset));

                    if (tolerance - rowdiff - checkdiff < 0) {
                        confirmed = false;
                        break;
                    }
                }

                if (confirmed) {
                    return i;
                }
            }
        }

        return -1;
    }


}
