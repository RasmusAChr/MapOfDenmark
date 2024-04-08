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
    public void draw(GraphicsContext gc, double zoom) {
        if(lt.findKey(roadType)) {
            gc.setStroke(cs.getColor(roadType));
            double zoomValue = lt.getWidth(roadType) + zoom;
            System.out.println(zoomValue);
            super.draw(gc, zoomValue);
        } else {
            super.draw(gc, zoom);
        }
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
    }
}
