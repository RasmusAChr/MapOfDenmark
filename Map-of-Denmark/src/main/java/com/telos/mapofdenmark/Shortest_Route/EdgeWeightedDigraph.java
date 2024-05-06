package com.telos.mapofdenmark.Shortest_Route;

import java.io.Serializable;

/**
 *  This is the class for the weighted directed graph used int the implemetation of the shortespath algorithm.
 *  It consists of vertices and directed edges with weights.
 */
public class EdgeWeightedDigraph implements Serializable {
    private final int V;
    private final Bag<DirectedEdge>[] adj;

    /**
     * This is the contrustor for the weighted directed graph. That initializes the graph to the size of vv
     * @param V the size of the weighted directed graph( the number of vertices)
     */
    public EdgeWeightedDigraph(int V){
        this.V = V;
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<DirectedEdge>();
    }

    /**
     *  Adds a directed edge to the graph
     * @param e the directeddd edge to add
     */
    public void addEdge(DirectedEdge e)
    {
        int v = e.from();
        adj[v].add(e);
    }
    /**
     * Returns an iterable containing the directed edges adjacent to vertex 'v'.
     *
     * @param v is the vertes for which adjacent edges are attach.
     * @return an iterable containing the directed edges adjacent to vertex 'v'
     */
    public Iterable<DirectedEdge> adj(int v)
    { return adj[v]; }

    /**
     * Returns the number of vertices in the graph.
     *
     * @return the number of vertices in the graph
     */
    public int V() {
        return V;
    }
}
