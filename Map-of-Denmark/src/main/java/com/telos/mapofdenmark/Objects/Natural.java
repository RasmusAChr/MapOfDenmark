package com.telos.mapofdenmark.Objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Natural extends Way {
    String type;

    public Natural(ArrayList<Node> way, String type){
        super(way);
        this.type = type;
    }

    @Override
    public void draw(GraphicsContext gc){
        gc.setFill(Color.web("#bdbdbd"));
    }
}
