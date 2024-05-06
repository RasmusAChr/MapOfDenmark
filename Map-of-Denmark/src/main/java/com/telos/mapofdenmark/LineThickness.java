package com.telos.mapofdenmark;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Represents line thickness for different types of map features.
 * Line widths are stored in a HashMap with keys representing feature types and
 * values representing the corresponding line thickness.
 */
public class LineThickness implements Serializable {
    // HashMap to store line widths for different feature types
    HashMap<String, Double> linewidth;

    public LineThickness() {
        linewidth = new HashMap<>();
        defaultScheme();
    }

    /**
     * Retrieves the line width for the specified feature type.
     * @param key The key representing the feature type
     * @return The line width for the specified feature type
     */
    public double getWidth(String key) {
        return linewidth.get(key);
    }

    // Initializes the HashMap with default line width values for various feature types.
    public void defaultScheme() {
        linewidth.clear();
        linewidth.put("default", 0.00001);
        linewidth.put("motorway", 0.0005);
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

    /**
     * Checks if the specified key exists in the HashMap.
     * @param a The key to search for
     * @return true if the key exists, otherwise false
     */
    public boolean findKey(String a) {
        return linewidth.containsKey(a);
    }
}
