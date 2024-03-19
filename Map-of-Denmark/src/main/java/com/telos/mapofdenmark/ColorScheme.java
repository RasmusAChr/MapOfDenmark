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

    public Color getDefaultColor(){
        return Color.web(colors.get("default"));
    }
    // Scheme for all the colors
    public void defaultScheme(){
        colors.clear();

        colors.put("fell","#48a160");
        colors.put("grassland","#68bf60");
        colors.put("heath","#45ed7d");
        colors.put("moor","#45ed7d");
        colors.put("scrub","#93ad6a");
        colors.put("scrubbery","#93ad6a");
        colors.put("tree","#188f40");
        colors.put("tree_row","#188f40");
        colors.put("tree_stump","#5c2606");
        colors.put("tundra","#48a160");
        colors.put("wood","#188f40");
        colors.put("bay","#a8d5ed");
        colors.put("beach","#f2db68");
        colors.put("blowhole","#f2db68");
        colors.put("cape","#f2db68");
        colors.put("coastline", "#ecf0ec"); //original:#ecf0ec pink:#ff73e8
        colors.put("crevasse","#68e2f2");
        colors.put("geyser","#acb4b5");
        colors.put("glacier","#68e2f2");
        colors.put("hot_spring","#a8d5ed");
        colors.put("isthmus","#45ed7d");
        colors.put("mud","#332702");
        colors.put("peninsula","#45ed7d");
        colors.put("shoal","#f2db68");
        colors.put("spring","#7daaff");
        colors.put("strait","#7daaff");
        colors.put("water","#7a9ad6");
        colors.put("wetland","#637796");
        colors.put("bare_rock","#6f7070");
        colors.put("arete","#6f7070");
        colors.put("blockfield","#6f7070");
        colors.put("cave_entrance","#6f7070");
        colors.put("cliff","#989a9c");
        colors.put("dune","#f2bf33");
        colors.put("earth_bank","#5c361d");
        colors.put("fumarole","#5c361d");
        colors.put("gully","#5c361d");
        colors.put("hill","#6d8f29");
        colors.put("peak","#d9fcfb");
        colors.put("ridge","#d9fcfb");
        colors.put("saddle","#d9fcfb");
        colors.put("sand","#d6c64f");
        colors.put("scree","#6f7070");
        colors.put("sinkhole","#6f7070");
        colors.put("stone","#6f707");
        colors.put("volcano","#453724");
        colors.put("default", "#ff00ae");
    }
}
