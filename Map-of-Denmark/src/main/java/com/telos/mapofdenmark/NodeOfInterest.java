package com.telos.mapofdenmark;

import java.io.Serializable;


public class NodeOfInterest implements Serializable {
    long id;
    public double lat, lon;
    Way way;
    boolean isPartOfRoad;
    boolean isPointOfInterest;

    public NodeOfInterest(long id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.way = null;
        this.isPartOfRoad = false;
        this.isPointOfInterest = false;
    }

    public double getLat(){
        return this.lat;
    }
    public double getLon(){
        return this.lon;
    }

    public void setWay(Way way) {
        this.way = way;
    }

    public String getId(){
        return this.id + "";
    }

    @Override
    public String toString() {
        return "Node" + this.id;
    }

    public Way getWay() {
        return way;
    }

    public boolean isPartOfRoad() {
        return isPartOfRoad;
    }

    public void setPartOfRoad(boolean partOfRoad) {
        isPartOfRoad = partOfRoad;
    }

    public boolean isPointOfInterest() {
        return isPointOfInterest;
    }
    public void setPointOfInterest(boolean pointOfInterest) {
        isPointOfInterest = pointOfInterest;
    }
}