package com.telos.mapofdenmark;

import java.io.Serializable;

/**
 * Represents a geographical node with an id, latitude, and longitude.
 * This class provides methods to retrieve information about the node, such as its
 * latitude and longitude.
 */
public class Node implements Serializable {
    // identifier of the node
    int id;
    // latitude and longitude of the node's location
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