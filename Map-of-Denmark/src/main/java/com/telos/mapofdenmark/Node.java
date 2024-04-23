package com.telos.mapofdenmark;

import java.io.Serializable;


public class Node implements Serializable {
    public double lat, lon;
    Way way;
    private boolean isPartOfRoad;
    public Node(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        this.way = null;
        this.isPartOfRoad = false;
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
    public void setPartOfRoad(boolean partOfRoad) {
        isPartOfRoad = partOfRoad;
    }
    public boolean getIsPartOfRoad(){
        return isPartOfRoad;
    }
    public Way getWay() {
        return this.way;
    }

}