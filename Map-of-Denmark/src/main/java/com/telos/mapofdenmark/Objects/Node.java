package com.telos.mapofdenmark.Objects;
/**
 * Represents a node on a map. A node is a point defined by its latitude and longitude
 * coordinates.
 */
public class Node {
    /**
     * Latitude and longtitude of the node.
     */
    double lat, lon;

    /**
     * Constructs a Node object with specified latitude and longitude.
     *
     * @param lat The latitude of the node.
     * @param lon The longitude of the node.
     */
    public Node(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}