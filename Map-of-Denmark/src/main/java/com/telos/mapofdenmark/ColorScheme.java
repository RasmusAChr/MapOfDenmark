package com.telos.mapofdenmark;
import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ColorScheme implements Serializable{
    Map<String, String> colors;
    Map<String, String> darkColors;
    public ColorScheme(){
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

        // Place
        colors.put("islet", "#f2efe9");
        darkColors.put("islet", "#464646");
        colors.put("island", "#f2efe9");
        darkColors.put("island", "#3d3d3d");
        colors.put("hamlet", "#f2efe9");
        darkColors.put("hamlet", "#323232");
        colors.put("archipelago", "#f2efe9");
        darkColors.put("archipelago", "#292929");

        // Natural
        colors.put("peninsula","#f2efe9");
        darkColors.put("peninsula", "#464646");
        colors.put("wetland", "#8cbf80");
        darkColors.put("wetland", "#5e8e5b");
        colors.put("heath", "#d6d99f");
        darkColors.put("heath", "#b0b283");
        colors.put("scrub", "#c9d8ac");
        darkColors.put("scrub", "#a3b292");
        colors.put("water", "#aad3df");
        darkColors.put("water", "#495f66");
        colors.put("beach", "#fff1bb");
        darkColors.put("beach", "#e0c77a");
        colors.put("bare_rock", "#c3c3c3");
        darkColors.put("bare_rock", "#a6a6a6");
        colors.put("scree", "#e4dcd4");
        darkColors.put("scree", "#c5bdb5");
        colors.put("wood", "#9dca8a");
        darkColors.put("wood", "#748d62");
        colors.put("bay", "#aad3df");
        darkColors.put("bay", "#aad3df");
        colors.put("grassland", "#8cbf80");
        darkColors.put("grassland", "#5e8e5b");

        // Landuse
        colors.put("meadow", "#8cbf80");
        darkColors.put("meadow", "#5e8e5b");
        colors.put("forest", "#7aa66f");
        darkColors.put("forest", "#5c7a58");
        colors.put("grass", "#8cbf80");
        darkColors.put("grass", "#5e8e5b");
        colors.put("farmland", "#8cbf80");
        darkColors.put("farmland", "#5e8e5b");
        colors.put("quarry", "#c4c2c2");
        darkColors.put("quarry", "#9b9999");
        colors.put("farmyard", "#efd5b3");
        darkColors.put("farmyard", "#d4b295");
        colors.put("industrial", "#ebdbe9");
        darkColors.put("industrial", "#b7a9b5");
        colors.put("plant_nursery", "#8cbf80");
        darkColors.put("plant_nursery", "#5e8e5b");
        colors.put("cemetery", "#aacbaf");
        darkColors.put("cemetery", "#7e9d86");
        colors.put("recreation_ground", "#dffce2");
        darkColors.put("recreation_ground", "#aedbbf");


        // Building
        colors.put("apartments", "#d8d0c9");
        darkColors.put("apartments", "#9b9087");
        colors.put("house", "#d8d0c9");
        darkColors.put("house", "#9b9087");
        colors.put("residential", "#d8d0c9");
        darkColors.put("residential", "#9b9087");
        colors.put("dormitory", "#d8d0c9");
        darkColors.put("dormitory", "#9b9087");
        colors.put("office", "#d8d0c9");
        darkColors.put("office", "#9b9087");
        colors.put("school", "#d8d0c9");
        darkColors.put("school", "#9b9087");
        colors.put("college", "#d8d0c9");
        darkColors.put("college", "#9b9087");
        colors.put("hospital", "#d8d0c9");
        darkColors.put("hospital", "#9b9087");
        colors.put("retail", "#d8d0c9");
        darkColors.put("retail", "#9b9087");
        colors.put("square", "#d8d0c9");
        darkColors.put("square", "#9b9087");
        colors.put("hotel", "#d8d0c9");
        darkColors.put("hotel", "#9b9087");
        colors.put("yes", "#d8d0c9");
        darkColors.put("yes", "#9b9087");
        colors.put("farm", "#d8d0c9");
        darkColors.put("farm", "#9b9087");
        colors.put("terrace", "#d8d0c9");
        darkColors.put("terrace", "#9b9087");
        colors.put("semidetached_house", "#d8d0c9");
        darkColors.put("semidetached_house", "#9b9087");
        colors.put("detached", "#d8d0c9");
        darkColors.put("detached", "#d8d0c9");
    }

}
