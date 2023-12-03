package com.jacobsevart.aoc;

import java.util.*;

public class PartNumbers {

    record Coordinate(int i, int j) {};
    record Part(int num, Coordinate relatedSymbol) {};

    public static long partTwo(Scanner in, int n) {
        return gearRatios(arrangeBySymbol(partNumbers(load(in, n))));
    }

    public static long gearRatios(Map<Coordinate, List<Integer>> bySymbol) {
        int acc = 0;
        for (var entry : bySymbol.entrySet()) {
            if (entry.getValue().size() == 2) {
                acc += (entry.getValue().get(0) * entry.getValue().get(1));
            }
        }

        return acc;
    }

    public static Map<Coordinate, List<Integer>> arrangeBySymbol(List<Part> partList) {
        Map<Coordinate, List<Integer>> bySymbol = new HashMap<>();

        for (Part p : partList) {
            if (!bySymbol.containsKey(p.relatedSymbol)) {
                bySymbol.put(p.relatedSymbol, new ArrayList<>());
            }

            bySymbol.get(p.relatedSymbol).add(p.num);
        }

        return bySymbol;
    }

    public static long partOne(Scanner in, int n) {
        char[][] map = load(in, n);
        List<Part> nums = partNumbers(map);
        long acc = 0;
        for (Part part : nums) {
            acc += part.num;
        }

        return acc;
    }
    public static List<Part> partNumbers(char[][] map) {
        List<Part> nums = new ArrayList<>();

        for (int i = 0; i < map.length; i++) {
            int num = 0;
            int numStartIndex = -1;

            for (int j = 0; j < map[0].length; j++) {
                if (Character.isDigit(map[i][j])) {
                    if (numStartIndex == -1)
                        numStartIndex = j;

                    num *= 10;
                    num += map[i][j] - '0';
                } else if (num > 0) {
                    for (int k = numStartIndex; k < j; k++) {
                        Coordinate adjSymbol = adjacentSymbol(map, i, k);
                        if (adjSymbol != null) {
                            nums.add(new Part(num, adjSymbol));
                            break;
                        }
                    }

                    num = 0;
                    numStartIndex = -1;
                }
            }

            // deal with trailing number
            if (num > 0) {
                for (int k = numStartIndex; k < map[0].length; k++) {
                    Coordinate adjSymbol = adjacentSymbol(map, i, k);
                    if (adjSymbol != null) {
                        nums.add(new Part(num, adjSymbol));
                    }
                }
            }
        }

        return nums;
    }

    public static Coordinate adjacentSymbol(char[][] map, int i, int j) {
        // -1, -1
        if (i - 1 >= 0 && j - 1 >= 0 && isSymbol(map[i - 1][j - 1])) return new Coordinate(i -1, j -1);
        // -1, 0
        if (i - 1 >= 0 && isSymbol(map[i - 1][j])) return new Coordinate(i -1, j);
        // -1, 1
        if (i - 1 >= 0 && j + 1 < map[0].length && isSymbol(map[i - 1][j + 1])) return new Coordinate(i -1, j + 1);
        // 0, -1
        if (j - 1 >= 0 && isSymbol(map[i][j - 1])) return new Coordinate(i, j - 1);
        // 0, 1
        if (j + 1 < map[0].length && isSymbol(map[i][j + 1])) return new Coordinate(i, j + 1);
        // 1, -1
        if (i + 1 < map.length && j - 1 >= 0 && isSymbol(map[i + 1][j - 1])) return new Coordinate (i + 1, j - 1);
        // 1, 0
        if (i + 1 < map.length && isSymbol(map[i + 1][j])) return new Coordinate(i + 1, j);
        // 1, 1
        if (i + 1 < map.length && j + 1 < map[0].length && isSymbol(map[i + 1][j + 1])) return new Coordinate(i + 1, j + 1);

        return null;
    }

    public static boolean isSymbol(char c) {
        return c != '.' && !Character.isDigit(c);
    }

    public static char[][] load(Scanner in, int n) {
        char[][] out = new char[n][];
        for (int i = 0; in.hasNextLine(); i++) {
            out[i] = in.nextLine().toCharArray();
        }

        return out;
    }

}
