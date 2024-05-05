package com.telos.mapofdenmark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Way implements Serializable {
    double[] coords;
    double zoom_scale;
    double centerLat;
    double centerLon;
    private Relation refRelation;

    Node refNode;
    private ArrayList<Node> nodesInWay;
    String landform;
    String type;

    public Way(ArrayList<Node> way, double zoom_scale, String landform, String type) {
        this.nodesInWay = new ArrayList<>();
        nodesInWay.addAll(way);
        double sumLat = 0;
        double sumLon = 0;

        coords = new double[way.size() * 2];
        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
            //node.setWay(this);
            coords[2 * i] = 0.56 * node.lon;
            coords[2 * i + 1] = -node.lat;

            sumLat += node.getLat();
            sumLon += node.getLon();
        }

        this.centerLat = sumLat / way.size();
        this.centerLon = sumLon / way.size();

        this.refNode = way.get(0);
        this.zoom_scale = zoom_scale;
        this.landform = landform;
        this.type = type;
    }
    public double[] getCoords() {
        return coords;
    }

    public void draw(GraphicsContext gc, double zoom, boolean darkMode, ColorScheme cs) {
        gc.beginPath();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
        }
        gc.stroke();
    }

    public void fill(GraphicsContext gc, boolean darkMode, ColorScheme cs, String type){
        gc.setFill(cs.getColor(landform,darkMode,type));
        gc.fill();
    }

    public ArrayList<Node> getNodes(){
        return nodesInWay;
    }


    public Node getArbitraryNode(){
        return refNode;
    }
    public double getZoom_scale() {
        return zoom_scale;
    }
    public double getCenterLat() { return centerLat; }
    public double getCenterLon() { return centerLon; }
    public boolean isRoad(){ return false; }

    // Getter and setter for refRelation
    public Relation getRefRelation() {
        return refRelation;
    }

    public void setRefRelation(Relation refRelation) {
        this.refRelation = refRelation;
    }
}