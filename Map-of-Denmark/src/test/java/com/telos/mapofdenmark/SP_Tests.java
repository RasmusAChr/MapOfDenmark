package com.telos.mapofdenmark;

import com.telos.mapofdenmark.Shortest_Route.*;
import java.util.ArrayList;

public class SP_Tests {
    /**
     * Basic test of Shortest path, and showcasing A-star parameters and usages.
     * Nodes have all node from our map. Index to different nodes is node.id.
     * EdgeWeightedDigraph contains our directedEdges with parameters from- and to-id
     * , and different edge-weights for bike- and car-travel.
     */
    public static void main(String[] args) {
        ArrayList<Node> Nodes = new ArrayList<>();
        Nodes.add(new Node(0, 100.0, 100.0));
        Nodes.add(new Node(1, 100.0, 200.0));
        Nodes.add(new Node(2, 100.0, 300.0));
        Nodes.add(new Node(3, 200.0, 100.0));
        Nodes.add(new Node(4, 200.0, 200.0));
        Nodes.add(new Node(5, 200.0, 300.0));

        EdgeWeightedDigraph Digraph = new EdgeWeightedDigraph(10);

        Digraph.addEdge(new DirectedEdge(0, 1, 0.5,0.7));
        Digraph.addEdge(new DirectedEdge(1, 2, 0.5,0.7));
        Digraph.addEdge(new DirectedEdge(2, 3, 0.5,0.7));

        Digraph.addEdge(new DirectedEdge(1, 4, 0.5,0.7));
        Digraph.addEdge(new DirectedEdge(4, 5, 0.5,0.7));
        Digraph.addEdge(new DirectedEdge(5, 3, 0.5,0.9));//Higher weight, so less favorable path.

        SP Dijkstra = new SP(Digraph,0,3,false, Nodes);

        boolean first = false;
        for (DirectedEdge i : Dijkstra.pathTo(3)) {
            if (!first)  {
                System.out.println("end " + Nodes.get(i.to()).id);
                first = true;
            }
            System.out.println(Nodes.get(i.from()).id);
        }
    }
}
