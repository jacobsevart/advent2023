package com.jacobsevart.aoc;

import java.util.*;

public class Components {
    Graph g;

    static class Graph {
        record Node(List<String> ids) {};
        record Edge(Node a, Node b) {};

        Map<Node, Set<Node>> adjacency;
        Set<Node> nodes;
        Set<Edge> edges;

        public Graph(Graph g) {
            // Make a copy, since we will be mutating
            adjacency = new HashMap<>();
            nodes = new HashSet<>(g.nodes);
            edges = new HashSet<>(g.edges);

            for (var edge : edges) {
                adjacency.putIfAbsent(edge.a, new HashSet<>());
                adjacency.get(edge.a).add(edge.b);

                adjacency.putIfAbsent(edge.b, new HashSet<>());
                adjacency.get(edge.b).add(edge.a);
            }
        }

        public Graph(Map<String, List<String>> original) {
            // From original parse
            adjacency = new HashMap<>();
            nodes = new HashSet<>();
            edges = new HashSet<>();

            for (var entry : original.entrySet()) {
                for (var destination : entry.getValue()) {
                    Node src = new Node(List.of(entry.getKey()));
                    Node dest = new Node(List.of(destination));

                    nodes.add(src);
                    nodes.add(dest);

                    adjacency.putIfAbsent(src, new HashSet<>());
                    adjacency.get(src).add(dest);

                    adjacency.putIfAbsent(dest, new HashSet<>());
                    adjacency.get(dest).add(src);

                    var forwardEdge = new Edge(src, dest);
                    var backwardsEdge = new Edge(dest, src);
                    if (!edges.contains(forwardEdge) && !edges.contains(backwardsEdge)) {
                        edges.add(forwardEdge);
                    }
                }
            }
        }

        public void contract(Node a, Node b) {
            var combinedIDs = new ArrayList<>(a.ids);
            combinedIDs.addAll(b.ids);

            var newNode = new Node(combinedIDs);

            // remove this edge
            edges.remove(new Edge(a, b));
            adjacency.get(a).remove(b);
            adjacency.get(b).remove(a);

            // new node will have all the outward edges of a and b combined
            Set<Node> newNodeOut = new HashSet<>();
            newNodeOut.addAll(adjacency.get(a));
            newNodeOut.addAll(adjacency.get(b));
            adjacency.put(newNode, newNodeOut);
            nodes.add(newNode);

            for (Node n : newNodeOut) {
                edges.add(new Edge(newNode, n));
            }

            // re-point all edges into a as edges into new
            for (Node out : adjacency.get(a)) {
                adjacency.get(out).remove(a);
                adjacency.get(out).add(newNode);
                edges.remove(new Edge(out, a));
                edges.remove(new Edge(a, out));
            }

            // re-point all edges into b as edges into new
            for (Node out : adjacency.get(b)) {
                adjacency.get(out).remove(b);
                adjacency.get(out).add(newNode);
                edges.remove(new Edge(out, b));
                edges.remove(new Edge(b, out));
            }

            adjacency.remove(a);
            adjacency.remove(b);

            nodes.remove(a);
            nodes.remove(b);
        }
    }

    public static Integer karger(Graph g, Random r) {
        Graph original = new Graph(g);
        Integer answer = null;

        for (int k = 0; k < 10000; k++) {
            if (k % 100 == 0) System.out.println(k);

            while (g.nodes.size() > 2) {
                int n = r.nextInt(g.edges.size());
                var iter = g.edges.iterator();
                Graph.Edge e = iter.next();

                // TODO: replace this with an O(1) indexed access data structure
                for (int i = 0; i < n - 1; i++) {
                    e = iter.next();
                }

                assert e != null;

                g.contract(e.a, e.b);
            }

            List<Graph.Node> remain = g.nodes.stream().toList();
            Set<String> idsLeft = new HashSet<>(remain.get(0).ids);
            Set<String> idsRight = new HashSet<>(remain.get(1).ids);

            List<Graph.Edge> cutset = new ArrayList<>();
            for (var edge : original.edges) {
                if (idsLeft.contains(edge.a.ids.get(0)) && idsRight.contains(edge.b.ids.get(0)) || idsLeft.contains(edge.b.ids.get(0)) && idsRight.contains(edge.a.ids.get(0))) {
                    cutset.add(edge);
                }
            }

            if (cutset.size() == 3) {
                answer = idsLeft.size() * idsRight.size();
                break;
            }

            g = new Graph(original); // try again with fresh graph
        }

        return answer;
    }

    public Components(Scanner in) {
        Map<String, List<String>> edges = new HashMap<>();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] bigParts = line.split(":");
            String[] littleParts = bigParts[1].trim().split(" ");

            edges.putIfAbsent(bigParts[0], new ArrayList<>());
            edges.get(bigParts[0]).addAll(Arrays.asList(littleParts));

            for (String s : littleParts) {
                edges.putIfAbsent(s, new ArrayList<>());
                edges.get(s).add(bigParts[0]);
            }
        }

        g = new Graph(edges);
    }
}
