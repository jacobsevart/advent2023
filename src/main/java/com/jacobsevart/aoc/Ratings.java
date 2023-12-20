package com.jacobsevart.aoc;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Array;
import java.util.*;
import java.util.stream.Stream;

import com.jacobsevart.aoc.grammar.RatingsGrammar;
import com.jacobsevart.aoc.grammar.RatingsLexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;


public class Ratings {
    RatingsGrammar r;
    Map<String, RatingsGrammar.WorkflowContext> workflows;
    List<Part> parts;

    record Part(int x, int m, int a, int s) {
        int sum() {
            return x + m + a + s;
        }
    };

    record Interval(int lo, int hi) {
        long span() {
            return hi - lo + 1;
        }
    };
    record Constraints(Interval x, Interval m, Interval a, Interval s) {
        long possibilities() {
            return x.span() * m.span() * a.span() * s.span();
        }
    };


    public Ratings(String s) {
        this(CharStreams.fromString(s));
    }

    public Ratings(InputStream s) throws IOException {
        this(CharStreams.fromStream(s));
    }

    public Ratings(CharStream s) {
        workflows = new HashMap<>();
        parts = new ArrayList<>();

        RatingsLexer l = new RatingsLexer(s);
        CommonTokenStream tokens = new CommonTokenStream(l);
        RatingsGrammar r = new RatingsGrammar(tokens);

        for (var workflow : r.workflows().workflow()) {
            workflows.put(workflow.ID().getText(), workflow);
        }

        for (var part : r.parts().part()) {
            parts.add(getPart(part));
        }
    }

    long count2(List<Constraints> constraints) {
        long acc = 0;
        for (Constraints c : constraints) {
            acc += c.possibilities();
        }

        return acc;
    }

    static Interval updateInterval(Interval i, RatingsGrammar.ComparisonContext comparison) {
        int ref = Integer.parseInt(comparison.children.get(2).getText());

        switch (comparison.children.get(1).getText()) {
            case ">" -> { return new Interval(ref + 1, i.hi); }
            case "<" -> { return new Interval(i.lo, ref - 1); }
            default -> throw new RuntimeException();
        }
    }

    List<Constraints> walk() {
        Constraints c = new Constraints(new Interval(1, 4000), new Interval(1, 4000), new Interval(1, 4000), new Interval(1, 4000));
        List<Constraints> terminal = new ArrayList<>();
        walk("in", c, terminal);

        return terminal;
    }

   void walk(String ruleName, Constraints c, List<Constraints> terminal) {
        RatingsGrammar.WorkflowContext workflow = workflows.get(ruleName);

        Constraints original = c;

        for (var stmt : workflow.stmts().stmt()) {
            c = original; // restore

            if (stmt.comparison() != null) {
                switch (stmt.comparison().children.get(0).getText()) {
                    case "x" -> c = new Constraints(updateInterval(c.x, stmt.comparison()), c.m, c.a, c.s);
                    case "m" -> c = new Constraints(c.x, updateInterval(c.m, stmt.comparison()), c.a, c.s);
                    case "a" -> c = new Constraints(c.x, c.m, updateInterval(c.a, stmt.comparison()), c.s);
                    case "s" -> c = new Constraints(c.x, c.m, c.a, updateInterval(c.s, stmt.comparison()));
                }
            }

            String consequence = stmt.consequence().getText();

            if (consequence.equals("A")) {
                terminal.add(c);
            } else if (!consequence.equals("R")){
                walk(consequence, c, terminal);
            }
        }
    }

    int partOne() {
        return matchingParts().map(Part::sum).reduce(0, Integer::sum);
    }

    Stream<Part> matchingParts() {
        return parts.stream().filter(x -> evaluate("in", x));
    }

    static Part getPart(RatingsGrammar.PartContext part) {
        return new Part(
                Integer.parseInt(part.INT(0).getText()),
                Integer.parseInt(part.INT(1).getText()),
                Integer.parseInt(part.INT(2).getText()),
                Integer.parseInt(part.INT(3).getText()));
    }

    static boolean compare(RatingsGrammar.ComparisonContext comparison, Part p) {
        if (comparison == null) return true;

        String var = comparison.children.get(0).getText();
        String comparator = comparison.children.get(1).getText();
        String reference = comparison.children.get(2).getText();

        int ref = Integer.parseInt(reference);
        int val = 0;
        switch (var) {
            case "x" -> val = p.x;
            case "m" -> val = p.m;
            case "a" -> val = p.a;
            case "s" -> val = p.s;
            default -> throw new RuntimeException();
        }

        boolean result;
        switch (comparator) {
            case "<" -> result = val < ref;
            case ">" -> result = val > ref;
            default -> throw new RuntimeException();
        }

        return result;
    }

    boolean evaluate(String rule, Part p) {
        var workflow = workflows.get(rule);

        for (var stmt : workflow.stmts().stmt()) {
            String consequence = stmt.consequence().getText();

            if (!compare(stmt.comparison(), p)) {
                continue;
            }

            switch (consequence) {
                case "R" -> {
                    return false;
                }
                case "A" -> {
                    return true;
                }
                default -> {
                    return evaluate(consequence, p);
                }
            }
        }

        throw new RuntimeException();
    }
}
