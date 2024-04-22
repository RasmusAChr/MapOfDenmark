package com.telos.mapofdenmark;

import java.io.Serializable;
import java.util.HashMap;

public class LineThickness implements Serializable {
    HashMap<String, Double> linewidth;

    public LineThickness() {
        linewidth = new HashMap<>();
        defaultScheme();
    }

    public double getDefaultWidth(){
        return linewidth.get("default");
    }// Scheme for all the colors

    public double getWidth(String key) {
        return linewidth.get(key);
    }
    public void defaultScheme() {
        linewidth.clear();
        linewidth.put("default", 0.00001);
        linewidth.put("highway", 0.0001);
        linewidth.put("trunk", 0.0001);
        linewidth.put("primary", 0.0001);
        linewidth.put("secondary", 0.0001);
        linewidth.put("tertiary", 0.0001);
        linewidth.put("unclassified", 0.0001);
        linewidth.put("residential", 0.0001);
        linewidth.put("motorway_link", 0.0001);
        linewidth.put("trunk_link", 0.0001);
        linewidth.put("primary_link", 0.0001);
        linewidth.put("secondary_link", 0.0001);
        linewidth.put("tertiary_link", 0.0001);
        linewidth.put("living_street", 0.0001);
        linewidth.put("track", 0.0001);


        //"motorway", "trunk", "primary", "secondary", "tertiary", "unclassified", "residential", "motorway_link", "trunk_link", "primary_link", "secondary_link", "tertiary_link", "living_street", "track"

    }

    public boolean findKey(String a) {
        return linewidth.containsKey(a);
    }
}
