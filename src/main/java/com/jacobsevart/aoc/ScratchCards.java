package com.jacobsevart.aoc;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ScratchCards {
    public static long partOne(Scanner lines) {
        long points = 0;
        while (lines.hasNextLine()) {
            String line = lines.nextLine();
            points += countWinning(line);
        }

        return points;
    }
    public static int countWinning(String line) {
        System.out.println(line);
        Set<Integer> winningNumbers = new HashSet<>();
        int num = 0;
        int i = 0;

        // consume through "Game N: "
        for (; i < line.length() && line.charAt(i) != ':'; i++) {}
        i += 2;

        // consume winning numbers until '|'
        for (; i < line.length() && line.charAt(i) != '|'; i++) {
            if (Character.isDigit(line.charAt(i))) {
                num *= 10;
                num += line.charAt(i) - '0';
            } else if (num > 0 && Character.isSpaceChar(line.charAt(i))) {
                winningNumbers.add(num);
                num = 0;
            }
        }

        System.out.println(winningNumbers);

        int matches = 0;
        for (; i < line.length(); i++) {
            if (Character.isDigit(line.charAt(i))) {
                num *= 10;
                num += line.charAt(i) - '0';
            } else if (Character.isSpaceChar(line.charAt(i)) && num > 0) {
                assert num < 100;
                if (winningNumbers.contains(num)) {
                    System.out.printf("match %d\n", num);
                    if (matches == 0) {
                        matches = 1;
                    } else {
                        matches *= 2;
                    }
                }
                num = 0;
            }
        }
        if (num > 0 && winningNumbers.contains(num)) {
            if (matches == 0) {
                matches = 1;
            } else {
                matches *= 2;
            }
        }

        return matches;
    }
}
