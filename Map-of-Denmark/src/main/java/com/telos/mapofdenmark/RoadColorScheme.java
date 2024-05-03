package com.telos.mapofdenmark;
import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RoadColorScheme implements Serializable{
    Map<String, String> colors;
    Map<String, String> darkColors;
    public RoadColorScheme(){
        colors = new HashMap<>();
        darkColors = new HashMap<>();
        defaultScheme();

    }

    public Color getColor(String type, boolean dark){
        String hex = null;
        try{
            if(!dark){
                hex = colors.get(type);
            }
            else {
                hex = darkColors.get(type);
            }
            return Color.web(hex);
        } catch (NullPointerException e) {
            if (!type.isEmpty()) System.out.println("missing color on: " + type);
            return getDefaultColor(dark);
        }

    }

    public boolean findKeyC(String b) {
        return colors.containsKey(b);
    }

    public Color getDefaultColor(boolean dark){
        if(dark) return Color.web("#ff0000");
        return Color.web("#ff0000");
    }
    // Scheme for all the colors
    public void defaultScheme(){
        colors.clear();

        //Roads
        colors.put("default", "#000000");
        darkColors.put("default", "#FFFFFF");
        colors.put("highway", "#CB4335");
        darkColors.put("highway", "#5B2C6F");
        colors.put("trunk", "#CA6F1E");
        darkColors.put("trunk", "#5B2C6F");
        colors.put("primary", "#CA6F1E");
        darkColors.put("primary", "#C0392B");
        colors.put("secondary", "#F39C12");
        darkColors.put("secondary", "#C0392B");
        colors.put("tertiary", "#34495E");
        darkColors.put("tertiary", "#C0392B");
        colors.put("residential", "#566573");
        darkColors.put("residential", "#FFFFFF");
        colors.put("unclassified", "#566573");
        darkColors.put("unclassified", "#FFFFFF");
        colors.put("motorway_link", "#808B96");
        darkColors.put("motorway_link", "#FFFFFF");
        colors.put("trunk_link", "#808B96");
        darkColors.put("trunk_link", "#FFFFFF");
        colors.put("primary_link", "#808B96");
        darkColors.put("primary_link", "#FFFFFF");
        colors.put("secondary_link", "#808B96");
        darkColors.put("secondary_link", "#FFFFFF");
        colors.put("tertiary_link", "#808B96");
        darkColors.put("tertiary_link", "#FFFFFF");
        colors.put("living_street", "#808B96");
        darkColors.put("living_street", "#FFFFFF");
        colors.put("track", "#566573");
        darkColors.put("track", "#FFFFFF");
        colors.put("cycleway", "#808B96");
        darkColors.put("cycleway", "#FFFFFF");
        colors.put("designated", "#808B96");
        darkColors.put("designated", "#FFFFFF");
        colors.put("use_sidepath", "#808B96");
        darkColors.put("use_sidepath", "#FFFFFF");
        colors.put("optional_sidepath", "#808B96");
        darkColors.put("optional_sidepath", "#FFFFFF");
        colors.put("permissive", "#808B96");
        darkColors.put("permissive", "#FFFFFF");
        colors.put("destination", "#808B96");
        darkColors.put("destination", "#FFFFFF");
        colors.put("service", "#808B96");
        darkColors.put("service", "#FFFFFF");
        colors.put("pedestrian", "#808B96");
        darkColors.put("pedestrian", "#FFFFFF");
        colors.put("footway", "#808B96");
        darkColors.put("footway", "#FFFFFF");
        colors.put("path", "#808B96");
        darkColors.put("path", "#FFFFFF");

    }

}
