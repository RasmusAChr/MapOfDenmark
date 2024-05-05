package com.telos.mapofdenmark;

import java.io.Serializable;


public class Node implements Serializable {
    int id;
    public double lat, lon;

    public Node(int id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat(){
        return this.lat;
    }
    public double getLon(){
        return this.lon;
    }
}