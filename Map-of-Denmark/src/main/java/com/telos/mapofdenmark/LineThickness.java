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
        linewidth.put("default", 10.0);
        linewidth.put("highway", 0.00001);
    }

    public boolean findKey(String a) {
        return linewidth.containsKey(a);
    }

}
