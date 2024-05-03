package com.telos.mapofdenmark;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;

public class Road extends Way implements Serializable {
    String roadType;
    LineThickness lt;
    public Road(ArrayList<Node> way, String roadType, double zoom_scale, LineThickness lt) {
        super(way, zoom_scale);
        this.roadType = roadType;
        this.lt = lt;

    }

    @Override
    public void draw(GraphicsContext gc, double zoom, boolean darkMode,ColorScheme cs) {
        double zoomValue = 0.0;
        if(lt.findKey(roadType)) {
            gc.setStroke(cs.getColor(roadType, darkMode));
            if (zoom < 5.0) {
                zoomValue = lt.getWidth(roadType) / 0.666;
            } else {
                zoomValue = lt.getWidth(roadType) / (zoom/10);
            }
            gc.setLineWidth(zoomValue);
        }
        super.draw(gc, zoom, darkMode, cs);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.00001);
    }
}
