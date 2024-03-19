package com.telos.mapofdenmark;

import javafx.scene.paint.Color;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

public class ColorScheme implements Serializable{
    Map<String, String> colors;
    public ColorScheme(){
        colors = new HashMap<>();
        defaultScheme();
    }

    public Color getColor(String type){
        String hex = colors.get(type);
        return Color.web(hex);
    }

    public void defaultScheme(){
        colors.clear();
        colors.put("default", "#ff00ee");
        colors.put("coastline", "#9f5be3");
    }
}
