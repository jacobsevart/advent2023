package com.jacobsevart.aoc;

import java.util.*;


public class CamelCards {
    public enum Kind {
        FIVE_KIND,
        FOUR_KIND,
        FULL_HOUSE,
        THREE_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }
   static List<Character> possibleCards = Arrays.asList('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2');

    record Hand(char[] cards, Kind kind) implements Comparable {
        Hand(String cards) {
            this(cards.toCharArray(), assignKind(cards.toCharArray()));
        }
        Hand(char[] cards) {
            this(cards, assignKind(cards));
        }

        static Kind assignKind(char[] cards) {
            Map<Character, Integer> freq = new HashMap<>();
            for (char c : cards) {
                int count = 0;
                if (freq.containsKey(c)) {
                    count = freq.get(c);
                }
                freq.put(c, count + 1);
            }

            Kind kind = null;
            int maxOccurrences = freq.values().stream().max(Comparator.naturalOrder()).get();
            if (maxOccurrences == 5) {
                kind = Kind.FIVE_KIND;
            } else if (maxOccurrences == 4) {
                kind = Kind.FOUR_KIND;
            } else if (maxOccurrences == 3) {
                if (freq.containsValue(2)) {
                    kind = Kind.FULL_HOUSE;
                } else {
                    kind = Kind.THREE_KIND;
                }
            } else {
                int twos = 0;
                for (int frequency : freq.values()) {
                    if (frequency == 2) twos++;
                }

                if (twos == 2) {
                    kind = Kind.TWO_PAIR;
                } else if (twos == 1) {
                    kind = Kind.ONE_PAIR;
                } else {
                    kind = Kind.HIGH_CARD;
                }
            }

            return kind;
        }
        @Override
        public int compareTo(Object o) {
            if (o == null) throw new NullPointerException();
            if (!(o instanceof Hand h)) throw new IllegalArgumentException();

            if (this.kind.compareTo(h.kind) < 0)  {
                return 1;
            } else if (this.kind.compareTo(h.kind) > 0) {
                return -1;
            }

            for (int i = 0; i < this.cards.length; i++) {
                Integer left = possibleCards.indexOf(this.cards[i]);
                Integer right = possibleCards.indexOf(h.cards[i]);

                int cmp = left.compareTo(right);
                if (cmp < 0) {
                    return 1;
                } else if (cmp > 0) {
                    return -1;
                }
            }

            return 0;
        }

        public boolean equals(Object o) {
            if (o == null) throw new NullPointerException();
            if (!(o instanceof Hand h)) throw new IllegalArgumentException();

            return this.compareTo(h) == 0;
        }

        @Override
        public String toString() {
            return String.format("%s:%s", String.valueOf(cards), kind.toString());
        }
    }

    record Bet(Hand hand, int bid) {};

    public static long partOne(Scanner in) {
        List<Bet> bets = new ArrayList<>();

        while (in.hasNext()) {
            Hand h = new Hand(in.next());
            int bet = in.nextInt();

            bets.add(new Bet(h, bet));
        }

        bets.sort(Comparator.comparing(Bet::hand));

        int winnings = 0;
        for (int i = 0; i < bets.size(); i++) {
            int rank = i + 1;
            winnings += rank * bets.get(i).bid;
        }

        return winnings;
    }
}
