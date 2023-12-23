package com.jacobsevart.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reflections {
    static List<List<Character>> parse(Scanner in) {
        List<List<Character>> chars = new ArrayList<>();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            List<Character> row = new ArrayList<>();

            for (char c : line.toCharArray()) {
                row.add(c);
            }
            chars.add(row);
        }

        return chars;
    }

//    static int findVerticalReflection(List<List<Character>> in) {
//        for (int i = 0; i + 1 < in.size(); i++) {
//            if (in.get(i).equals(in.get(i + 1))) {
//                // candidate
//                // check i - 1 vs. i + 2, i - 2 vs. i + 3
//
//                boolean confirmed = true;
//                for (int offset = 1; i - offset >= 0 && i + offset < in.size(); offset++) {
//                    if (!in.get(i - offset).equals(in.get(i + 1 + offset))) {
//                        confirmed = false;
//                        break;
//                    }
//                }
//            }
//        }
//    }
}
