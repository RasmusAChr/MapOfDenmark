package com.telos.mapofdenmark;

import java.io.Serializable;


public class Node implements Serializable {
    int id;
    public double lat, lon;
    Way way;
    boolean isPartOfRoad;
    Relation refRelation;

    public Node(int id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.way = null;
        this.isPartOfRoad = false;
        this.refRelation = null;
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

    public void setRefRelation(Relation relation){
        refRelation = relation;
    }

    public Relation getRefRelation() {
        return refRelation;
    }
}