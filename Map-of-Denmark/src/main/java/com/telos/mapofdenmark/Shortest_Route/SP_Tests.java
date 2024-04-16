package com.telos.mapofdenmark.Shortest_Route;

public class SP_Tests {

    public static void main(String[] args) {
        EdgeWeightedDigraph Digraph = new EdgeWeightedDigraph(10);
        Digraph.addEdge(new DirectedEdge(0, 1, 0.5));
        Digraph.addEdge(new DirectedEdge(1, 2, 0.5));
        Digraph.addEdge(new DirectedEdge(2, 3, 0.5));
        Digraph.addEdge(new DirectedEdge(3, 4, 0.5));
        Digraph.addEdge(new DirectedEdge(4, 5, 0.5));
        Digraph.addEdge(new DirectedEdge(0, 5, 0.5));
        SP Dijkstra = new SP(Digraph,0);

        System.out.println(Dijkstra.pathTo(5));

    }
}
