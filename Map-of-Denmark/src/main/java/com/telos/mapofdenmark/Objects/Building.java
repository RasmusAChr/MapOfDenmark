package com.telos.mapofdenmark.Objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Building extends Way{
    //String buildingType;
    public Building(ArrayList<Node> way, String type){
        super(way, type);
        //this.buildingType = type;
    }

    // Way.draw() get the outline and strokes it.
    // This draw uses the outline and fills it.
    @Override
    public void draw(GraphicsContext gc){
        gc.setFill(Color.web("#ffe08c"));
        super.draw(gc);
        gc.fill();
    }
}
