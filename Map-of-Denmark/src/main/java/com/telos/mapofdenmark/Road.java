package com.telos.mapofdenmark;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Road extends Way{

    ColorScheme cs = new ColorScheme();
    LineThickness lt = new LineThickness();
    String roadType;
    public Road(ArrayList<Node> way, String roadType) {
        super(way);
        this.roadType = roadType;
    }

    @Override
    public void draw(GraphicsContext gc, double zoom, boolean darkMode) {
        double zoomValue = 0.0;
        if(lt.findKey(roadType)) {
            gc.setStroke(cs.getColor(roadType, darkMode));
            if (zoom < 0.1) {
                zoomValue = lt.getWidth(roadType) / 5;
            } else {
                zoomValue = lt.getWidth(roadType) / (zoom/10);
            }
            gc.setLineWidth(zoomValue);
            super.draw(gc, zoom, darkMode);
        } else {
            super.draw(gc, zoom, darkMode);
        }
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.00001);
    }
}
