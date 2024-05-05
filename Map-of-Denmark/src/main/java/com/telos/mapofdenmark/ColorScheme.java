package com.telos.mapofdenmark;
import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ColorScheme implements Serializable{
    Map<String, String> placeColors;
    Map<String, String> placeDarkColors;
    Map<String, String> naturalColors;
    Map<String, String> naturalDarkColors;
    Map<String, String> landuseColors;
    Map<String, String> landuseDarkColors;
    Map<String, String> roadColors;
    Map<String, String> roadDarkColors;


    public ColorScheme(){
        placeColors = new HashMap<>();
        placeDarkColors = new HashMap<>();
        naturalColors = new HashMap<>();
        naturalDarkColors = new HashMap<>();
        landuseColors = new HashMap<>();
        landuseDarkColors = new HashMap<>();
        roadColors = new HashMap<>();
        roadDarkColors = new HashMap<>();
        placeScheme();
        naturalScheme();
        landuseScheme();
        roadScheme();
    }

    public Color getColor(String landform, boolean dark, String type){
        String hex = null;
        try{
            if(!dark){
                switch (type) {
                    case "place":
                        hex = placeColors.get(landform);
                        break;
                    case "natural":
                        hex = naturalColors.get(landform);
                        break;
                    case "landuse":
                        hex = landuseColors.get(landform);
                        break;
                    case "building":
                        hex = "#a39d98";
                        break;
                    case "road":
                        hex = roadColors.get(landform);
                        break;
                }
            }
            else {
                switch (type) {
                    case "place":
                        hex = placeDarkColors.get(landform);
                        break;
                    case "natural":
                        hex = naturalDarkColors.get(landform);
                        break;
                    case "landuse":
                        hex = landuseDarkColors.get(landform);
                        break;
                    case "building":
                        hex = "#706b67";
                        break;
                    case "road":
                        hex = roadDarkColors.get(landform);
                        break;
                }
            }
            return Color.web(hex);
        } catch (NullPointerException e) {
            if (!landform.isEmpty()) {
                System.out.println("missing color on: " + type + " " + landform);
            }
            return getDefaultColor(dark);
        }

    }

    public Color getDefaultColor(boolean dark){
        if(dark) return Color.web("#ff0000");
        return Color.web("#ff0000");
    }

    // Scheme for all the colors
    public void placeScheme(){
        // Place
        placeColors.put("islet", "#f2efe9");
        placeDarkColors.put("islet", "#b5b3ae");
        placeColors.put("island", "#f2efe9");
        placeDarkColors.put("island", "#b5b3ae");
        placeColors.put("hamlet", "#f2efe9");
        placeDarkColors.put("hamlet", "#b5b3ae");
        placeColors.put("archipelago", "#f2efe9");
        placeDarkColors.put("archipelago", "#b5b3ae");
        placeColors.put("square", "#dadada");
        placeDarkColors.put("square", "#a0a0a0");
        placeColors.put("neighbourhood", "#dadada");
        placeDarkColors.put("neighbourhood", "#a0a0a0");
        placeColors.put("quarter", "#dadada");
        placeDarkColors.put("quarter", "#a0a0a0");
        placeColors.put("suburb", "#dadada");
        placeDarkColors.put("suburb", "#a0a0a0");
        placeColors.put("plot", "#dadada");
        placeDarkColors.put("plot", "#a0a0a0");
        placeColors.put("city", "#dadada");
        placeDarkColors.put("city", "#a0a0a0");
        placeColors.put("allotments", "#c5d9c3");
        placeDarkColors.put("allotments", "#9aae98");
        placeColors.put("isolated_dwelling", "#c5d9c3");
        placeDarkColors.put("isolated_dwelling", "#9aae98");
        placeColors.put("locality", "#c5d9c3");
        placeDarkColors.put("locality", "#9aae98");
    }

    public void naturalScheme(){
        // Natural
        naturalColors.put("peninsula","#f2efe9");
        naturalDarkColors.put("peninsula", "#464646");
        naturalColors.put("wetland", "#8cbf80");
        naturalDarkColors.put("wetland", "#5e8e5b");
        naturalColors.put("heath", "#d6d99f");
        naturalDarkColors.put("heath", "#b0b283");
        naturalColors.put("scrub", "#c9d8ac");
        naturalDarkColors.put("scrub", "#a3b292");
        naturalColors.put("scrubby", "#c9d8ac");
        naturalDarkColors.put("scrubby", "#a3b292");
        naturalColors.put("water", "#aad3df");
        naturalDarkColors.put("water", "#495f66");
        naturalColors.put("beach", "#fff1bb");
        naturalDarkColors.put("beach", "#e0c77a");
        naturalColors.put("bare_rock", "#c3c3c3");
        naturalDarkColors.put("bare_rock", "#a6a6a6");
        naturalColors.put("scree", "#e4dcd4");
        naturalDarkColors.put("scree", "#c5bdb5");
        naturalColors.put("wood", "#9dca8a");
        naturalDarkColors.put("wood", "#748d62");
        naturalColors.put("bay", "#aad3df");
        naturalDarkColors.put("bay", "#aad3df");
        naturalColors.put("grassland", "#8cbf80");
        naturalDarkColors.put("grassland", "#5e8e5b");
        naturalColors.put("tree_row", "#7aa66f");
        naturalDarkColors.put("tree_row", "#5c7a58");
        naturalColors.put("sand", "#fff1bb");
        naturalDarkColors.put("sand", "#e0c77a");
        naturalColors.put("rock", "#c3c3c3");
        naturalDarkColors.put("rock", "#a6a6a6");
        naturalColors.put("shoal", "#fff1bb");
        naturalDarkColors.put("shoal", "#e0c77a");
        naturalColors.put("cliff", "#c3c3c3");
        naturalDarkColors.put("cliff", "#a6a6a6");
        naturalColors.put("valley", "#8cbf80");
        naturalDarkColors.put("valley", "#5e8e5b");
        naturalColors.put("stone", "#c3c3c3");
        naturalDarkColors.put("stone", "#a6a6a6");
        naturalColors.put("earth_bank", "#eed7bf");
        naturalDarkColors.put("earth_bank", "#bfaea0");
        naturalColors.put("earth_bank", "#c3c3c3");
        naturalDarkColors.put("earth_bank", "#a6a6a6");
        naturalColors.put("strait", "#aad3df");
        naturalDarkColors.put("strait", "#495f66");
        naturalColors.put("mud", "#d6c29f");
        naturalDarkColors.put("mud", "#a38866");
        naturalColors.put("grass", "#8cbf80");
        naturalDarkColors.put("grass", "#5e8e5b");
        naturalColors.put("shrubbery", "#c9d8ac");
        naturalDarkColors.put("shrubbery", "#a3b292");
        naturalColors.put("hill", "#d6d99f");
        naturalDarkColors.put("hill", "#b0b283");
        naturalColors.put("cape", "#c3c3c3");
        naturalDarkColors.put("cape", "#a6a6a6");
        naturalColors.put("shrub", "#c9d8ac");
        naturalDarkColors.put("shrub", "#a3b292");
        naturalColors.put("forest", "#7aa66f");
        naturalDarkColors.put("forest", "#5c7a58");
        naturalColors.put("tree_group", "#7aa66f");
        naturalDarkColors.put("tree_group", "#5c7a58");
    }

    public void landuseScheme(){
        // Landuse
        landuseColors.put("meadow", "#8cbf80");
        landuseDarkColors.put("meadow", "#5e8e5b");
        landuseColors.put("forest", "#7aa66f");
        landuseDarkColors.put("forest", "#5c7a58");
        landuseColors.put("grass", "#8cbf80");
        landuseDarkColors.put("grass", "#5e8e5b");
        landuseColors.put("farmland", "#8cbf80");
        landuseDarkColors.put("farmland", "#5e8e5b");
        landuseColors.put("quarry", "#c4c2c2");
        landuseDarkColors.put("quarry", "#9b9999");
        landuseColors.put("farmyard", "#efd5b3");
        landuseDarkColors.put("farmyard", "#d4b295");
        landuseColors.put("industrial", "#ebdbe9");
        landuseDarkColors.put("industrial", "#b7a9b5");
        landuseColors.put("plant_nursery", "#8cbf80");
        landuseDarkColors.put("plant_nursery", "#5e8e5b");
        landuseColors.put("cemetery", "#aacbaf");
        landuseDarkColors.put("cemetery", "#7e9d86");
        landuseColors.put("recreation_ground", "#dffce2");
        landuseDarkColors.put("recreation_ground", "#aedbbf");
        landuseColors.put("commercial", "#eecfcf");
        landuseDarkColors.put("commercial", "#b49292");
        landuseColors.put("orchard", "#8cbf80");
        landuseDarkColors.put("orchard", "#5e8e5b");
        landuseColors.put("brownfield", "#c7c7b4");
        landuseDarkColors.put("brownfield", "#9b9b85");
        landuseColors.put("residential", "#dadada");
        landuseDarkColors.put("residential", "#a0a0a0");
        landuseColors.put("greenfield", "#f1eee8");
        landuseDarkColors.put("greenfield", "#b5b2a9");
        landuseColors.put("construction", "#c7c7b4");
        landuseDarkColors.put("construction", "#9b9b85");
        landuseColors.put("construction_site", "#c7c7b4");
        landuseDarkColors.put("construction_site", "#9b9b85");
        landuseColors.put("retail", "#fecac5");
        landuseDarkColors.put("retail", "#d39e99");
        landuseColors.put("pasture", "#8cbf80");
        landuseDarkColors.put("pasture", "#5e8e5b");
        landuseColors.put("water", "#aad3df");
        landuseDarkColors.put("water", "#495f66");
        landuseColors.put("allotments", "#c5d9c3");
        landuseDarkColors.put("allotments", "#9aae98");
        landuseColors.put("scrub", "#c9d8ac");
        landuseDarkColors.put("scrub", "#a3b292");
        landuseColors.put("village_green", "#8cbf80");
        landuseDarkColors.put("village_green", "#5e8e5b");
        landuseColors.put("greenhouse_horticulture", "#eef0d5");
        landuseDarkColors.put("greenhouse_horticulture", "#c3c5a9");
        landuseColors.put("vineyard", "#eef0d5");
        landuseDarkColors.put("vineyard", "#c3c5a9");
        landuseColors.put("basin", "#aad3df");
        landuseDarkColors.put("basin", "#aad3df");
        landuseColors.put("beach", "#fff1bb");
        landuseDarkColors.put("beach", "#e0c77a");
        landuseColors.put("wetland", "#8cbf80");
        landuseDarkColors.put("wetland", "#5e8e5b");
        landuseColors.put("heath", "#d6d99f");
        landuseDarkColors.put("heath", "#b0b283");
        landuseColors.put("wood", "#9dca8a");
        landuseDarkColors.put("wood", "#748d62");
        landuseColors.put("bare_rock", "#c3c3c3");
        landuseDarkColors.put("bare_rock", "#a6a6a6");
        landuseColors.put("scree", "#e4dcd4");
        landuseDarkColors.put("scree", "#c5bdb5");
        landuseColors.put("grassland", "#8cbf80");
        landuseDarkColors.put("grassland", "#5e8e5b");
        landuseColors.put("sand", "#fff1bb");
        landuseDarkColors.put("sand", "#e0c77a");
        landuseColors.put("bay", "#aad3df");
        landuseDarkColors.put("bay", "#aad3df");
        landuseColors.put("railway", "#dadada");
        landuseDarkColors.put("railway", "#a0a0a0");
        landuseColors.put("education", "#ffffe5");
        landuseDarkColors.put("education", "#d9d9ad");
        landuseColors.put("aquaculture", "#aad3df");
        landuseDarkColors.put("aquaculture", "#495f66");
        landuseColors.put("park", "#c3e8b5");
        landuseDarkColors.put("park", "#8dbf84");
        landuseColors.put("logistics", "#ebf0d5");
        landuseDarkColors.put("logistics", "#c3c5a9");
        landuseColors.put("storage", "#ebf0d5");
        landuseDarkColors.put("storage", "#c3c5a9");
        landuseColors.put("religious", "#fceae5");
        landuseDarkColors.put("religious", "#dfb5a3");
        landuseColors.put("artproject", "#f1f1f1");
        landuseDarkColors.put("artproject", "#b7b7b7");
        landuseColors.put("animal_keeping", "#ebf0d5");
        landuseDarkColors.put("animal_keeping", "#c3c5a9");
        landuseColors.put("flowerbed", "#f1f1f1");
        landuseDarkColors.put("flowerbed", "#b7b7b7");
        landuseColors.put("depot", "#ebf0d5");
        landuseDarkColors.put("depot", "#c3c5a9");
        landuseColors.put("terminal", "#ebf0d5");
        landuseDarkColors.put("terminal", "#c3c5a9");
        landuseColors.put("hedge", "#c3e8b5");
        landuseDarkColors.put("hedge", "#8dbf84");
        landuseColors.put("workyard", "#ebf0d5");
        landuseDarkColors.put("workyard", "#c3c5a9");
        landuseColors.put("common", "#c3e8b5");
        landuseDarkColors.put("common", "#8dbf84");
        landuseColors.put("apiary", "#ebf0d5");
        landuseDarkColors.put("apiary", "#c3c5a9");
        landuseColors.put("greenery", "#c3e8b5");
        landuseDarkColors.put("greenery", "#8dbf84");
        landuseColors.put("landfill", "#c3e8b5");
        landuseDarkColors.put("landfill", "#8dbf84");
        landuseColors.put("garages", "#ebf0d5");
        landuseDarkColors.put("garages", "#c3c5a9");
    }

    public void roadScheme(){
        //Roads
        roadColors.put("default", "#000000");
        roadDarkColors.put("default", "#FFFFFF");
        roadColors.put("highway", "#CB4335");
        roadDarkColors.put("highway", "#5B2C6F");
        roadColors.put("residential", "#808B96");
        roadDarkColors.put("residential", "#FFFFFF");
        roadColors.put("trunk", "#CA6F1E");
        roadDarkColors.put("trunk", "#5B2C6F");
        roadColors.put("primary", "#CA6F1E");
        roadDarkColors.put("primary", "#C0392B");
        roadColors.put("secondary", "#F39C12");
        roadDarkColors.put("secondary", "#C0392B");
        roadColors.put("tertiary", "#34495E");
        roadDarkColors.put("tertiary", "#C0392B");
        roadColors.put("unclassified", "#566573");
        roadDarkColors.put("unclassified", "#FFFFFF");
        roadColors.put("motorway_link", "#808B96");
        roadDarkColors.put("motorway_link", "#FFFFFF");
        roadColors.put("trunk_link", "#808B96");
        roadDarkColors.put("trunk_link", "#FFFFFF");
        roadColors.put("primary_link", "#808B96");
        roadDarkColors.put("primary_link", "#FFFFFF");
        roadColors.put("secondary_link", "#808B96");
        roadDarkColors.put("secondary_link", "#FFFFFF");
        roadColors.put("tertiary_link", "#808B96");
        roadDarkColors.put("tertiary_link", "#FFFFFF");
        roadColors.put("living_street", "#808B96");
        roadDarkColors.put("living_street", "#FFFFFF");
        roadColors.put("track", "#566573");
        roadDarkColors.put("track", "#FFFFFF");
        roadColors.put("cycleway", "#808B96");
        roadDarkColors.put("cycleway", "#FFFFFF");
        roadColors.put("designated", "#808B96");
        roadDarkColors.put("designated", "#FFFFFF");
        roadColors.put("use_sidepath", "#808B96");
        roadDarkColors.put("use_sidepath", "#FFFFFF");
        roadColors.put("optional_sidepath", "#808B96");
        roadDarkColors.put("optional_sidepath", "#FFFFFF");
        roadColors.put("permissive", "#808B96");
        roadDarkColors.put("permissive", "#FFFFFF");
        roadColors.put("destination", "#808B96");
        roadDarkColors.put("destination", "#FFFFFF");
        roadColors.put("service", "#808B96");
        roadDarkColors.put("service", "#FFFFFF");
        roadColors.put("pedestrian", "#808B96");
        roadDarkColors.put("pedestrian", "#FFFFFF");
        roadColors.put("footway", "#808B96");
        roadDarkColors.put("footway", "#FFFFFF");
        roadColors.put("path", "#808B96");
        roadDarkColors.put("path", "#FFFFFF");
    }

}
