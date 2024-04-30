package com.telos.mapofdenmark.Shortest_Route;
//Class aquired from slides from algo
// Credit algs4 sedgewick and wayne

import java.io.Serializable;

public class DirectedEdge implements Serializable {
    private final int V,W;
    private final double weightbike,weightcar;
    public DirectedEdge(int V, int W, double weightbike, double weightcar) {
        this.V = V;
        this.W = W;
        this.weightbike = weightbike;
        this.weightcar = weightcar;
    }

    public int from(){return V;}
    public int to(){return W;}
    public double weight(boolean vehicle){
        if(vehicle) return weightbike;
        return weightcar;
    }

    public String toString(){
        return "from: " + from() + ", to: " + to() + ", weightcar: " + weightcar+ ", weightbike: " + weightbike;
    }
}
