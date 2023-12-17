package com.jacobsevart.aoc;

import java.util.Scanner;

public class Hashing {
    int sum;

    public Hashing(Scanner in) {
        sum = 0;
        in.useDelimiter(",");
        while (in.hasNext()) {
            String s = in.next();
            int h = hash(s);
            sum += h;
        }
    }

    public static int hash(String in) {
        int cur = 0;
        for (char c : in.toCharArray()) {
            cur += c;
            cur *= 17;
            cur = cur % 256;
        }

        return cur;
    }
}
