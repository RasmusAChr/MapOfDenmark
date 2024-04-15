package com.telos.mapofdenmark;

public class MapBounds {
    private double minLon;
    private double maxLon;
    private double minLat;
    private double maxLat;


    public MapBounds(double minLon, double maxLon, double minLat, double maxLat){
        this.minLon = minLon;
        this.maxLon = maxLon;
        this.minLat = minLat;
        this.maxLat = maxLat;
    }

    public double getMinLon(){
        return minLon;
    }
    public double getMaxLon(){
        return maxLon;
    }
    public double getMinLat(){
        return minLat;
    }
    public double getMaxLat(){
        return maxLat;
    }
}
