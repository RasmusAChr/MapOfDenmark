package com.telos.mapofdenmark.Shortest_Route;
//Class aquired from slides from algo
// Credit algs4 sedgewick and wayne

import java.io.Serializable;
/**
 * The DirectedEdge class represents a directed edge in a weighted graph.
 * The class provides the functionality to create and manipulate directed edges between vertices in a graph.
 * An directed edge contains a start vertex, an end vertex but also weights associated with the edges for both car and bike travel
 * Credit: Class is based on material from slides from Algorithms & Data Structures, which is based on algs4 library Sedgewick & Wayne
 */
public class DirectedEdge implements Serializable {
    private final int V,W;
    private final double weightbike,weightcar;
    /**
     * Constructor for DirectedEdges. It initializes a directed edge between two vertices with given weights both car and bike travel
     * @param V - start vertex of the directed edge
     * @param W - end vertex of the directed edge
     * @param weightbike - the weight of the directed edge for bike travel
     * @param weightcar - the weight of the directed edge for car travel
     */
    public DirectedEdge(int V, int W, double weightbike, double weightcar) {
        this.V = V;
        this.W = W;
        this.weightbike = weightbike;
        this.weightcar = weightcar;
    }
    /**
     * Getter method to return the start vertex of the directed edge
     * @return - start vertex of the directed edge
     */
    public int from(){return V;}
    /**
     * Getter method to return the end vertex of the directed edge
     * @return - end vertex of the directed edge
     */
    public int to(){return W;}
    /**
     * Returns the weight of the directed edge on the vehicle type
     * @param vehicle - true = bike, false = car
     * @return - the weight of the directed edge either for bike or car
     */
    public double weight(boolean vehicle){
        if(vehicle) return weightbike;
        return weightcar;
    }

    /**
     * Returns a string that represents the directed edge
     * @return - string representation of the directed edge
     */
    public String toString(){
        return "from: " + from() + ", to: " + to() + ", weightcar: " + weightcar+ ", weightbike: " + weightbike;
    }
}
