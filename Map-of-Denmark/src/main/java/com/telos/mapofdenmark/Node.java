package com.telos.mapofdenmark;

import java.io.Serializable;


public class Node implements Serializable {
    int id;
    public double lat, lon;

    /**
     * Constructs a new Node with the specified identifier, latitude, and longitude.
     * @param id  The identifier of the node.
     * @param lat The latitude of the node's location.
     * @param lon The longitude of the node's location.
     */
    public Node(int id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Returns the latitude of the node
     * @return double lat
     */
    public double getLat(){
        return this.lat;
    }

    /**
     * Returns the longitude of the node
     * @return double lat
     */
    public double getLon(){
        return this.lon;
    }
}