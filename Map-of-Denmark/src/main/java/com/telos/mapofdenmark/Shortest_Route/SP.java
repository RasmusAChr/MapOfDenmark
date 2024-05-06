package com.telos.mapofdenmark.Shortest_Route;
import com.telos.mapofdenmark.Model;
import com.telos.mapofdenmark.Node;

import java.util.ArrayList;
import java.util.Stack;

public class SP
{
    private DirectedEdge[] edgeTo;
    private double[] distTo;
    private IndexMinPQ<Double> pq;
    private boolean vehicle;
    private int Goal;
    private ArrayList<Node> nodes;
    public SP(EdgeWeightedDigraph G, int s, int goal, boolean vehicle, ArrayList<Node> Nodes)
    {
        this.nodes = Nodes;
        this.vehicle = vehicle;
        this.Goal = goal;
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];
        pq = new IndexMinPQ<Double>(G.V());
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;
        pq.insert(s, 0.0);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdge e : G.adj(v))
                relax(e, goal);
        }
    }
    private void relax(DirectedEdge e, int goal)
    {
        int v = e.from(), w = e.to();
        double newDistToW = distTo[v] + e.weight(vehicle);
        if (distTo[w] > newDistToW) {
            distTo[w] = newDistToW;
            double priority = newDistToW + heuristic(w,goal);
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, priority);
            else pq.insert (w, priority);
        }
    }

    public double distTo(int v)
    { return distTo[v]; }

    public Iterable<DirectedEdge> pathTo(int v)
    {
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
            path.push(e);
        return path;
    }

    private double heuristic(int v, int goal){
       Node currentNode = nodes.get(v);
       Node goalNode = nodes.get(goal);

       double lat1 = currentNode.getLat();
       double lon1 = currentNode.getLon();
       double lat2 = goalNode.getLat();
       double lon2 = goalNode.getLon();

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        double height = 0.0;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return  distance;
    }
}
