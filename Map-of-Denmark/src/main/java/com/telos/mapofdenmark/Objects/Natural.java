package com.telos.mapofdenmark.Objects;

import com.telos.mapofdenmark.ColorScheme;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Natural extends Way {
    String type;
    ColorScheme cs = new ColorScheme();

    public Natural(ArrayList<Node> way, String type){
        super(way);
        this.type = type;
    }

    @Override
    public void draw(GraphicsContext gc){
        gc.setFill(fillColor());
        super.draw(gc);
        gc.fill();
    }

    private Color fillColor(){
        if (type == "coastline"){
            return cs.getColor(type);
        }
        else {
            return Color.PINK;
        }
    }
}
