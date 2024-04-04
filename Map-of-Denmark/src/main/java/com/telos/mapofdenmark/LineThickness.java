package com.telos.mapofdenmark;

import java.io.Serializable;
import java.util.HashMap;

public class LineThickness implements Serializable {
    HashMap<String, Integer> linewidth;

    public LineThickness() {
        linewidth = new HashMap<>();
        defaultScheme();
    }

    public int getDefaultWidth(){
        return linewidth.get("default");
    }// Scheme for all the colors

    public int getWidth(String key) {
        return linewidth.get(key);
    }
    public void defaultScheme() {
        linewidth.clear();
        linewidth.put("default", 10);
        linewidth.put("highway", 20);
    }

    public boolean findKey(String a) {
        return linewidth.containsKey(a);
    }

}
