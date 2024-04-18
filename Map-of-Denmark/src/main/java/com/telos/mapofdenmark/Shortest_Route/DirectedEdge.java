package com.telos.mapofdenmark.Shortest_Route;
//Class aquired from slides from algo

import java.io.Serializable;

public class DirectedEdge implements Serializable {
    private final int V,W;
    private final double weight;
    public DirectedEdge(int V, int W, double weight) {
        this.V = V;
        this.W = W;
        this.weight = weight;
    }

    public int from(){return V;}
    public int to(){return W;}
    public double weight(){return weight;}

    public String toString(){
        return "from: " + from() + ", to: " + to() + ", weight: " + weight();
    }
}
