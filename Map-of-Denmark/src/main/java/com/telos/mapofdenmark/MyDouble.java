package com.telos.mapofdenmark;

import java.io.Serializable;

public class MyDouble implements Comparable<MyDouble>, Serializable {
    private double dist;

    MyDouble(double dist) {
        this.dist = dist;
    }
    MyDouble() {
        this.dist = Double.POSITIVE_INFINITY;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public int compareTo(MyDouble o) {
        return Double.compare(this.dist, o.dist);
    }

    public boolean compare(MyDouble o) {
        return dist < o.dist;
    }

    public MyDouble add(double d) {
        return new MyDouble(dist + d);
    }

}
