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
            var vertical = findVerticalReflection(chars);
            if (vertical >= 0) acc += vertical + 1;

            var horiz = findHorizontalReflection(chars);
            if (horiz >= 0) acc += 100 * (horiz + 1);
        }

        return acc;
    }

    static int findVerticalReflection(List<List<Character>> in) {
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
            if (Arrays.equals(cols[j], cols[j + 1])) {
                boolean confirmed = true;
                for (int offset = 1; j - offset >= 0 && j + 1 + offset < cols.length; offset++) {
                    if (!Arrays.equals(cols[j - offset], cols[j + 1 + offset])) {
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

    static int findHorizontalReflection(List<List<Character>> in) {
        for (int i = 0; i + 1 < in.size(); i++) {
            if (in.get(i).equals(in.get(i + 1))) {
                // candidate
                // check i - 1 vs. i + 2, i - 2 vs. i + 3

                boolean confirmed = true;
                for (int offset = 1; i - offset >= 0 && i + offset < in.size() - 1; offset++) {
                    if (!in.get(i - offset).equals(in.get(i + 1 + offset))) {
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
