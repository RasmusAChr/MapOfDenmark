package com.telos.mapofdenmark.Objects;

import com.telos.mapofdenmark.ColorScheme;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Leisure extends Way{
    ColorScheme cs = new ColorScheme();
    public Leisure(ArrayList<Node> way, String type){
        super(way, type);
    }

    @Override
    public void draw(GraphicsContext gc){
        gc.setFill(fillColor());
        super.draw(gc);
        gc.fill();
    }

    private Color fillColor(){
        if (type.isEmpty()){
            return cs.getDefaultColor();
        }
        else {
            try {
                return cs.getColor(type);
            }
            catch (Exception e) {
                return cs.getDefaultColor();
            }

        }
    }
}
