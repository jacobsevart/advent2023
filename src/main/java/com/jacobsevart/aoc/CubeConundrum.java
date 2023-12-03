package com.jacobsevart.aoc;

import java.util.Scanner;

public class CubeConundrum {
    record Game(int red, int green, int blue) {};

    public static long power(Scanner in) {
        long acc = 0;

        for (int i = 1; in.hasNextLine(); i++) {
            String line = in.nextLine();
            Game g = parse(line);
            int power = (g.red * g.green * g.blue);
            acc += power;
        }

        return acc;
    }

    public static int sum(Scanner in) {
        int acc = 0;

        for (int i = 1; in.hasNextLine(); i++) {
            String line = in.nextLine();
            boolean possible = isPossible(parse(line), 12, 13, 14);

//            System.out.printf("%3d %b : %s\n", i, possible, line);
            if (possible) acc += i;
        }

        return acc;
    }

    public static boolean isPossible(Game g, int red, int green, int blue) {
        return g.red <= red && g.blue <= blue && g.green <= green;
    }

    public static Game parse(String line) {
        int colon = line.indexOf(':');
        line = line.substring(colon + 1);

        int maxRed = 0;
        int maxGreen = 0;
        int maxBlue = 0;

        for (String turn : line.split(";")) {
            int red = 0;
            int green = 0;
            int blue = 0;

            for (String color : turn.split(",")) {
                String[] spaceSeparated = color.trim().split(" ");
                int num = Integer.parseInt(spaceSeparated[0]);
                switch (spaceSeparated[1]) {
                    case "red": red = num; break;
                    case "blue": blue = num; break;
                    case "green": green = num; break;
                    default: throw new RuntimeException(String.format("unknown color %s", spaceSeparated[1]));
                }
            }

            if (red > maxRed) maxRed = red;
            if (green > maxGreen) maxGreen = green;
            if (blue > maxBlue) maxBlue = blue;
        }

        return new Game(maxRed, maxGreen, maxBlue);
    }
}
