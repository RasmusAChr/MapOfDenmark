package com.telos.mapofdenmark;

import java.io.Serializable;

public class Node implements Serializable {
    double lat, lon;

    public Node(double lat, double lon) {
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