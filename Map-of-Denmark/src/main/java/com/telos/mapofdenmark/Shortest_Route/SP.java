package com.telos.mapofdenmark.Shortest_Route;
import com.telos.mapofdenmark.Node;

import java.util.ArrayList;
import java.util.Stack;

/**
 * The SP class represents a shortest path algorithm implementation for finding the shortest path
 * in a weighted directed graph from a start node as an index to a goal node. It employs Dijkstra's algorithm
 * with the addition of a heuristic function to optimize the search.
 */
public class SP
{
    private DirectedEdge[] edgeTo;
    private double[] distTo;
    private IndexMinPQ<Double> pq;
    private boolean vehicle;
    private ArrayList<Node> nodes;



    /**
     * Initializes the shortest path algorithm from the start node as index 's' to the goal node 'goal'
     * in the given weighted directed graph 'G'. The 'vehicle' parameter indicates whether the
     * path calculation is for a bike or car The 'nodes' parameter provides the list of nodes
     * necessary for heuristic calculation.
     *
     * @param G       the weighted directed graph
     * @param s       the start/source node index
     * @param goal    the goal node index
     * @param vehicle indicates if the path calculation is for bike or a car
     * @param Nodes   list of nodes for heuristic calculation
     */
    public SP(EdgeWeightedDigraph G, int s, int goal, boolean vehicle, ArrayList<Node> Nodes)
    {
        this.nodes = Nodes;
        this.vehicle = vehicle;
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];
        pq = new IndexMinPQ<>(G.V());
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

    /**
     * A function for Relaxing and edge when Dijkstra has passed it.
     * @param e is the Directed edge from the graphs adjacent edges.
     * @param goal Is the end point( end nodes index value/id)
     */
    private void relax(DirectedEdge e, int goal){
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

    /**
     * Gets the path from start to finish and makes an iterable of Directededges that summarise the path.
     * @param v End node as index
     * @return And itereable list of Directed edges from the start point to end
     */
    public Iterable<DirectedEdge> pathTo(int v)
    {
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
            path.push(e);
        return path;
    }

    /**
     *  A Helper function for the A* heuristic. This helper function calculateds the distance from the node that is currently being explored to the end node in a
     *  birds as distance as crows fly. This helps to get the shortest path quicker.
     * @param v the current node that is being explored
     * @param goal The destination for the pathfinding algorith the place we want to end up.
     * @return this returns the distance that has been calculated.
     */
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
