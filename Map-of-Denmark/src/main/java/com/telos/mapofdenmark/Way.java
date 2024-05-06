package com.telos.mapofdenmark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents a geographic way consisting of a collection of nodes
 * This class provides methods to retrieve information about the way, such as its coordinates,
 * center point, and associated nodes. It also provides functionality to draw and fill the way
 * on a graphics context.
 */
public class Way implements Serializable {
    // Array of coordinates representing the way
    double[] coords;
    // Latitude of the center point of the way
    double centerLat;
    // Longitude of the center point of the way
    double centerLon;
    // Reference to the parent relation of the way
    private Relation refRelation;
    // Reference to one of the nodes in the way
    Node refNode;
    // List of nodes forming the way
    private ArrayList<Node> nodesInWay;
    String landform;
    String type;


    /**
     * Constructs a Way object with the specified list of nodes, landform, and type.
     * @param way      The list of nodes forming the way
     * @param landform The landform type of the way
     * @param type     The type of the way
     */
    public Way(ArrayList<Node> way, String landform, String type) {
        this.nodesInWay = new ArrayList<>();
        nodesInWay.addAll(way);
        double sumLat = 0;
        double sumLon = 0;

        coords = new double[way.size() * 2];
        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
            coords[2 * i] = 0.56 * node.lon;
            coords[2 * i + 1] = -node.lat;

            sumLat += node.getLat();
            sumLon += node.getLon();
        }

        this.centerLat = sumLat / way.size();
        this.centerLon = sumLon / way.size();

        this.refNode = way.get(0);
        this.landform = landform;
        this.type = type;
    }

    /**
     * Retrieves the coordinates of the way.
     * @return The coordinates of the way
     */
    public double[] getCoords() {
        return coords;
    }


    /**
     * Draws the way on the specified graphics context.
     * @param gc       The graphics context to draw on
     * @param zoom     The zoom level
     * @param darkMode Indicates whether dark mode is enabled
     * @param cs       The color scheme to use for drawing
     */
    public void draw(GraphicsContext gc, double zoom, boolean darkMode, ColorScheme cs) {
        gc.beginPath();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
        }
        gc.stroke();
    }

    /**
     * Fills the way with color on the specified graphics context.
     * @param gc       The graphics context to fill on
     * @param darkMode Indicates whether dark mode is enabled
     * @param cs       The color scheme to use for filling
     * @param type     The type of the way
     */
    public void fill(GraphicsContext gc, boolean darkMode, ColorScheme cs, String type){
        gc.setFill(cs.getColor(landform,darkMode,type));
        gc.fill();
    }

    /**
     * Retrieves the list of nodes forming the way.
     * @return The list of nodes forming the way
     */
    public ArrayList<Node> getNodes(){
        return nodesInWay;
    }

    /**
     * Retrieves an arbitrary node from the way.
     * @return An arbitrary node from the way
     */
    public Node getArbitraryNode(){
        return refNode;
    }

    /**
     * Retrieves the latitude of the center point of the way.
     * @return The latitude of the center point of the way
     */
    public double getCenterLat() { return centerLat; }

    /**
     * Retrieves the longitude of the center point of the way.
     * @return The longitude of the center point of the way
     */
    public double getCenterLon() { return centerLon; }

    /**
     * Indicates whether the way represents a road.
     * @return true if the way represents a road, otherwise false
     */
    public boolean isRoad(){ return false; }

    // Getter and setter for refRelation
    /**
     * Retrieves the reference to the relation the way is a part of.
     * @return The reference to the parent relation of the way
     */
    public Relation getRefRelation() {
        return refRelation;
    }

    /**
     * Sets the reference to the parent relation of the way.
     * @param refRelation The reference to the parent relation of the way
     */
    public void setRefRelation(Relation refRelation) {
        this.refRelation = refRelation;
    }
}