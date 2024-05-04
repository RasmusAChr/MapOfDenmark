package com.telos.mapofdenmark;
import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ColorScheme implements Serializable{
    Map<String, String> placeColors;
    Map<String, String> placeDarkColors;
    Map<String, String> naturalColors;
    Map<String, String> naturalDarkColors;
    Map<String, String> landuseColors;
    Map<String, String> landuseDarkColors;
    Map<String, String> buildingColors;
    Map<String, String> buildingDarkColors;

    Map<String, String> roadColors;
    Map<String, String> roadDarkColors;


    public ColorScheme(){
        placeColors = new HashMap<>();
        placeDarkColors = new HashMap<>();
        naturalColors = new HashMap<>();
        naturalDarkColors = new HashMap<>();
        landuseColors = new HashMap<>();
        landuseDarkColors = new HashMap<>();
        buildingColors = new HashMap<>();
        buildingDarkColors = new HashMap<>();
        roadColors = new HashMap<>();
        roadDarkColors = new HashMap<>();
        placeScheme();
        naturalScheme();
        landuseScheme();
        buildingScheme();
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
                        hex = "#d8d0c9";//buildingColors.get(landform);
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
                        hex = "#9b9087";//buildingDarkColors.get(landform);
                        break;
                    case "road":
                        hex = roadDarkColors.get(landform);
                        break;
                }
            }
            return Color.web(hex);
        } catch (NullPointerException e) {
            if (!landform.isEmpty()) System.out.println("missing color on: " + type + " " + landform);
            return getDefaultColor(dark);
        }

    }

    public Color getRoadColor(String type, boolean dark){
        String hex = null;
        try{
            if(!dark){
                hex = roadColors.get(type);
            }
            else {
                hex = roadDarkColors.get(type);
            }
            return Color.web(hex);
        } catch (NullPointerException e) {
            if (!type.isEmpty()) System.out.println("missing color on: " + type);
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
    }

    public void buildingScheme(){
        // Building
        buildingColors.put("apartments", "#d8d0c9");
        buildingDarkColors.put("apartments", "#9b9087");
        buildingColors.put("house", "#d8d0c9");
        buildingDarkColors.put("house", "#9b9087");
        buildingColors.put("residential", "#d8d0c9");
        buildingDarkColors.put("residential", "#9b9087");
        buildingColors.put("dormitory", "#d8d0c9");
        buildingDarkColors.put("dormitory", "#9b9087");
        buildingColors.put("office", "#d8d0c9");
        buildingDarkColors.put("office", "#9b9087");
        buildingColors.put("school", "#d8d0c9");
        buildingDarkColors.put("school", "#9b9087");
        buildingColors.put("college", "#d8d0c9");
        buildingDarkColors.put("college", "#9b9087");
        buildingColors.put("hospital", "#d8d0c9");
        buildingDarkColors.put("hospital", "#9b9087");
        buildingColors.put("retail", "#d8d0c9");
        buildingDarkColors.put("retail", "#9b9087");
        buildingColors.put("square", "#d8d0c9");
        buildingDarkColors.put("square", "#9b9087");
        buildingColors.put("hotel", "#d8d0c9");
        buildingDarkColors.put("hotel", "#9b9087");
        buildingColors.put("yes", "#d8d0c9");
        buildingDarkColors.put("yes", "#9b9087");
        buildingColors.put("farm", "#d8d0c9");
        buildingDarkColors.put("farm", "#9b9087");
        buildingColors.put("terrace", "#d8d0c9");
        buildingDarkColors.put("terrace", "#9b9087");
        buildingColors.put("semidetached_house", "#d8d0c9");
        buildingDarkColors.put("semidetached_house", "#9b9087");
        buildingColors.put("detached", "#d8d0c9");
        buildingDarkColors.put("detached", "#d8d0c9");
        buildingColors.put("warehouse", "#d8d0c9");
        buildingDarkColors.put("warehouse", "#d8d0c9");
        buildingColors.put("garage", "#d8d0c9");
        buildingDarkColors.put("garage", "#d8d0c9");
        buildingColors.put("cabin", "#d8d0c9");
        buildingDarkColors.put("cabin", "#d8d0c9");
        buildingColors.put("shed", "#d8d0c9");
        buildingDarkColors.put("shed", "#d8d0c9");
        buildingColors.put("bungalow", "#d8d0c9");
        buildingDarkColors.put("bungalow", "#d8d0c9");
        buildingColors.put("roof", "#d8d0c9");
        buildingDarkColors.put("roof", "#d8d0c9");
        buildingColors.put("hut", "#d8d0c9");
        buildingDarkColors.put("hut", "#d8d0c9");
        buildingColors.put("kiosk", "#d8d0c9");
        buildingDarkColors.put("kiosk", "#d8d0c9");
        buildingColors.put("slurry_tank", "#d8d0c9");
        buildingDarkColors.put("slurry_tank", "#d8d0c9");
        buildingColors.put("farm_auxiliary", "#d8d0c9");
        buildingDarkColors.put("farm_auxiliary", "#d8d0c9");
        buildingColors.put("church", "#d8d0c9");
        buildingDarkColors.put("church", "#d8d0c9");
        buildingColors.put("silo", "#d8d0c9");
        buildingDarkColors.put("silo", "#d8d0c9");
        buildingColors.put("supermarket", "#d8d0c9");
        buildingDarkColors.put("supermarket", "#d8d0c9");
        buildingColors.put("storage_tank", "#d8d0c9");
        buildingDarkColors.put("storage_tank", "#d8d0c9");
        buildingColors.put("windmill", "#d8d0c9");
        buildingDarkColors.put("windmill", "#d8d0c9");
        buildingColors.put("carport", "#d8d0c9");
        buildingDarkColors.put("carport", "#d8d0c9");
        buildingColors.put("lighthouse", "#d8d0c9");
        buildingDarkColors.put("lighthouse", "#d8d0c9");
        buildingColors.put("ruins", "#d8d0c9");
        buildingDarkColors.put("ruins", "#d8d0c9");
        buildingColors.put("transformer_tower", "#d8d0c9");
        buildingDarkColors.put("transformer_tower", "#d8d0c9");
        buildingColors.put("toilets", "#d8d0c9");
        buildingDarkColors.put("toilets", "#d8d0c9");
        buildingColors.put("kindergarten", "#d8d0c9");
        buildingDarkColors.put("kindergarten", "#d8d0c9");
        buildingColors.put("sports_centre", "#d8d0c9");
        buildingDarkColors.put("sports_centre", "#d8d0c9");
        buildingColors.put("greenhouse", "#d8d0c9");
        buildingDarkColors.put("greenhouse", "#d8d0c9");
        buildingColors.put("barn", "#d8d0c9");
        buildingDarkColors.put("barn", "#d8d0c9");
        buildingColors.put("barn", "#d8d0c9");
        buildingDarkColors.put("barn", "#d8d0c9");
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
