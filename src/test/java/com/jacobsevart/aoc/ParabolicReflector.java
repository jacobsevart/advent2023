package com.jacobsevart.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ParabolicReflector {
    List<List<Character>> grid;
    public ParabolicReflector(Scanner in) {
        grid = parse(in);
    }

    public void tiltNorth() {
        for (int j = 0; j < grid.get(0).size(); j++) {
            int slideTo = grid.size();

            for (int i = 0; i < grid.size(); i++) {
                if (grid.get(i).get(j) == '#') {
                    slideTo = i + 1;
                } else if (grid.get(i).get(j) == '.') {
                    if (slideTo > i) {
                        slideTo = i;
                    }
                } else if (grid.get(i).get(j) == 'O') {
                    if (slideTo < i) {
                        grid.get(slideTo++).set(j, 'O'); // swap boulder to slideTo
                        grid.get(i).set(j, '.'); // replace boulder with empty space
                    }
                    // keep advancing slideTo past any obstacles
                    while (slideTo < grid.size() && grid.get(slideTo).get(j) != '.') slideTo++;
                }
            }
        }
    }

    public int totalLoad() {
        int[] rowWeight = new int[grid.size()];

        for (int j = 0 ; j < grid.get(0).size(); j++) {
            for (int i = 0; i < grid.size(); i++) {
                if (grid.get(i).get(j) == 'O') {
                    rowWeight[i] += grid.size() - i;
                }
            }
        }

        return Arrays.stream(rowWeight).sum();
    }

    String getColumn(int j) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grid.size(); i++) {
            sb.append(grid.get(i).get(j));
        }

        return sb.toString();
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(0).size(); j++) {
                sb.append(grid.get(i).get(j));
            }

            if (i < grid.size() - 1) sb.append("\n");
        }

        return sb.toString();
    }

    static List<List<Character>> parse(Scanner in) {
        List<List<Character>> out = new ArrayList<>();

        while (in.hasNextLine()) {
            List<Character> row = new ArrayList<>();
            for (char c : in.nextLine().toCharArray()) {
                row.add(c);
            }
            out.add(row);
        }

        return out;
    }


}
