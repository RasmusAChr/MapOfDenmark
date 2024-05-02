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

        /*// NATURALS
        colors.put("fell","#48a160");
        colors.put("grassland","#f2efe9");
        colors.put("heath","#eaf0d6");
        colors.put("moor","#45ed7d");
        colors.put("scrub","#93ad6a");
        colors.put("scrubbery","#93ad6a");
        colors.put("tree","#188f40");
        colors.put("tree_row","#188f40");
        colors.put("tree_stump","#5c2606");
        colors.put("tundra","#48a160");
        colors.put("wood","#188f40");
        colors.put("bay","#90c5ee");
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
        colors.put("shoal","#f2db68");
        colors.put("spring","#7daaff");
        colors.put("strait","#7daaff");
        colors.put("water","#90c5ee");
        colors.put("wetland","#637796");
        colors.put("bare_rock","#adadad");
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

        // Landuse
        colors.put("forest","#9dca8a");
        colors.put("meadow", "#ceecb1");
        colors.put("cemetery","#abccb0");
        colors.put("grass","#68bf60");

        // Leisure
        colors.put("garden","#d3efb6");
        colors.put("playground","#dffce2");*/

    }

}
