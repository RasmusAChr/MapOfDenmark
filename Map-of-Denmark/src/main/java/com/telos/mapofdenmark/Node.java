package com.telos.mapofdenmark;

import java.io.Serializable;


public class Node implements Serializable {
    long id;
    double lat, lon;
    Way way;

    public Node(long id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.way = null;
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
}