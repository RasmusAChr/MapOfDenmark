package com.telos.mapofdenmark.Shortest_Route;

import java.util.ArrayList;

public class EdgeWeightedDigraph {
    private final int V;
    private final Bag<DirectedEdge>[] adj;

    public EdgeWeightedDigraph(int V, ArrayList<ArrayList<DirectedEdge>> roads){
        this.V = V;
        adj = (Bag<DirectedEdge>[]) new Bag[V];

        int i = 0;
        for (ArrayList<DirectedEdge> road : roads) {
            adj[i] = new Bag<>();
            for (DirectedEdge e : road) {
                adj[i].add(e);
            }
            i++;
        }
    }
    public void addEdge(DirectedEdge e)
    {
        int v = e.from();
        adj[v].add(e);
    }
    public Iterable<DirectedEdge> adj(int v)
    { return adj[v]; }


    public int V() {
        return V;
    }
}
