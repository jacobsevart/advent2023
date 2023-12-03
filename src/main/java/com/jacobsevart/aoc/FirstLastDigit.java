package com.jacobsevart.aoc;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FirstLastDigit {
    Node dfa;
    Node reverseDFA;
    Map<String, Integer> forwardEntries;

    public FirstLastDigit() {
        forwardEntries = getEntries(false);
        dfa = buildTree(forwardEntries);
        reverseDFA = buildTree(getEntries(true));
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        var acc = 0;
        while (input.hasNextLine()) {
            acc += valueOfLine(input.nextLine());
        }

        System.out.println(acc);
    }

    public static int sumFirstLast(Scanner in) {
        int acc = 0;
        for (String line = in.nextLine(); in.hasNextLine(); line = in.nextLine()) {
            acc += valueOfLine(line);
        }

        return acc;
    }

    public long sumPart2(Scanner in) {
        long acc = 0;
        while (in.hasNextLine()) {
            String line = in.nextLine();
            int val = valueOfLineWithDigit(line);
            System.out.printf("line: %s, val: %d\n", line, val);
            acc += valueOfLineWithDigit(line);
        }

        return acc;
    }

    public static int valueOfLine(String line) {
        char[] arr = line.toCharArray();

        int i = 0;
        for (; i < arr.length && (arr[i] < '0' || arr[i] > '9'); i++) {
        }
        ;

        int j = arr.length - 1;
        for (; j > 0 && (arr[j] < '0' || arr[j] > '9'); j--) {
        }
        ;

        return (((arr[i] - '0') * 10) + (arr[j] - '0'));
    }

    public int valueOfLineWithDigit(String line) {
        Node node = dfa;
        int backtrackTo = 0;
        int firstDigit = -1;
        for (int i = 0; i < line.length(); i++) {
            if (node != dfa && !node.next.containsKey(line.charAt(i))) {
                node = dfa;
                i = backtrackTo;
            }

            if (node == dfa && node.next.containsKey(line.charAt(i))) {
                backtrackTo = i + 1;
            }

            node = node.next.getOrDefault(line.charAt(i), dfa);
            if (node.isDigit) {
                firstDigit = node.digit;
                break;
            }
        }
        if (firstDigit == -1) throw new RuntimeException("no first digit in " + line);

        node = reverseDFA;
        int lastDigit = -1;
        for (int i = line.length() - 1; i >= 0; i--) {
            if (node != reverseDFA && !node.next.containsKey(line.charAt(i))) {
                node = reverseDFA;
                i = backtrackTo;
            }

            if (node == reverseDFA && node.next.containsKey(line.charAt(i))) {
                backtrackTo = i - 1;
            }

            node = node.next.getOrDefault(line.charAt(i), reverseDFA);
            if (node.isDigit) {
                lastDigit = node.digit;
                break;
            }
        }

        if (lastDigit == -1) throw new RuntimeException("no last digit in " + line);


        return (firstDigit * 10) + lastDigit;
    }

    public static Map<String, Integer> getEntries(boolean reverse) {
        Map<String, Integer> entries = new HashMap<>();
        for (int i = 1; i < 10; i++) {
            entries.put(String.format("%d", i), i);
        }
        String[] words = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        for (int i = 0; i < words.length; i++) {
            if (reverse) {
                entries.put(new StringBuilder().append(words[i]).reverse().toString(), i + 1);
            } else {
                entries.put(words[i], i + 1);
            }
        }

        return entries;
    }

    public static Node buildTree(Map<String, Integer> entries) {
        Node root = new Node();

        for (Map.Entry<String, Integer> entry : entries.entrySet()) {
            Node node = root;
            for (int i = 0; i < entry.getKey().length(); i++) {
                if (!node.next.containsKey(entry.getKey().charAt(i))) {
                    node.next.put(entry.getKey().charAt(i), new Node());
                }

                node = node.next.get(entry.getKey().charAt(i));
            }

            node.isDigit = true;
            node.digit = entry.getValue();
        }

        return root;
    }

    public static class Node {
        boolean isDigit;
        int digit;
        Map<Character, Node> next;

        public Node() {
            this.next = new HashMap<>();
        }
    }
}
