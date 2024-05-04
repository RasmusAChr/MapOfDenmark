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
        linewidth.put("highway", 0.0005);
        linewidth.put("trunk", 0.0004);
        linewidth.put("primary", 0.0003);
        linewidth.put("secondary", 0.0002);
        linewidth.put("tertiary", 0.0002);
        linewidth.put("unclassified", 0.0001);
        linewidth.put("residential", 0.0001);
        linewidth.put("motorway_link", 0.0003);
        linewidth.put("trunk_link", 0.0003);
        linewidth.put("primary_link", 0.0003);
        linewidth.put("secondary_link", 0.0002);
        linewidth.put("tertiary_link", 0.0002);
        linewidth.put("living_street", 0.0001);
        linewidth.put("track", 0.00013);
        linewidth.put("cycleway", 0.00008);
        linewidth.put("yes", 0.0001);
        linewidth.put("designated", 0.0001);
        linewidth.put("use_sidepath", 0.0001);
        linewidth.put("optional_sidepath", 0.0001);
        linewidth.put("permissive", 0.0001);
        linewidth.put("destination", 0.0001);
        linewidth.put("service", 0.00008);
        linewidth.put("pedestrian", 0.00007);
        linewidth.put("footway", 0.00007);
        linewidth.put("path", 0.00003);
    }

    public boolean findKey(String a) {
        return linewidth.containsKey(a);
    }
}
