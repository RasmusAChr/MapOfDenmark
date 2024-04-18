package com.telos.mapofdenmark;

import java.io.Serializable;


public class Node implements Serializable {
    public double lat, lon;
    Way way;

    public Node(double lat, double lon) {
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

    public Way getWay() {
        return this.way;
    }
}