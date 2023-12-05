package com.jacobsevart.aoc;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ScratchCards {

    public static long partTwo(Scanner in, int n) {
        int[] matchesPerCard = new int[n];
        for (int i = 0; in.hasNextLine(); i++) {
            String line = in.nextLine();
            matchesPerCard[i] = countWinning(line);
        }

        return numCards(matchesPerCard);
    }

    public static int numCards(int[] matchesPerCard) {
        int[] copies = new int[matchesPerCard.length];
        for (int i = 0; i < matchesPerCard.length; i++)
            copies[i] = 1;

        for (int i = 0; i < matchesPerCard.length; i++) {
            for (int j = i + 1; j <= i + matchesPerCard[i] && j < matchesPerCard.length; j++) {
                copies[j] += copies[i];
            }
        }

        int sum = 0;
        for (int i = 0; i < copies.length; i++) {
            sum += copies[i];
        }

        return sum;
    }

    public static long partOne(Scanner lines) {
        long points = 0;
        while (lines.hasNextLine()) {
            String line = lines.nextLine();
            points += points(countWinning(line));
        }

        return points;
    }

    public static long points(long winning) {
        if (winning == 0) return 0;
        if (winning == 1) return 1;

        return 1L << (winning - 1);
    }

    public static int countWinning(String line) {
//        System.out.println(line);
        Set<Integer> winningNumbers = new HashSet<>();
        int num = 0;
        int i = 0;

        // consume through "Game N: "
        for (; i < line.length() && line.charAt(i) != ':'; i++) {
        }
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

        int matches = 0;
        for (; i < line.length(); i++) {
            if (Character.isDigit(line.charAt(i))) {
                num *= 10;
                num += line.charAt(i) - '0';
            } else if (Character.isSpaceChar(line.charAt(i)) && num > 0) {
                assert num < 100;
                if (winningNumbers.contains(num)) {
                    matches++;
                }
                num = 0;
            }
        }
        if (num > 0 && winningNumbers.contains(num)) matches++;

        return matches;
    }
}
