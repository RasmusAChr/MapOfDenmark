package com.telos.mapofdenmark;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;

public class Road extends Way implements Serializable {
    String roadType;
    LineThickness lt;
    boolean sliderDraged = false;
    public Road(ArrayList<Node> way, String roadType, double zoom_scale, LineThickness lt) {
        super(way, zoom_scale, "", "road");
        this.roadType = roadType;
        this.lt = lt;

    }

    @Override
    public void draw(GraphicsContext gc, double zoom, boolean darkMode,ColorScheme cs) {
        double zoomValue;

        if(lt.findKey(roadType)) {
            gc.setStroke(cs.getColor(roadType, darkMode, "road"));
            if (!sliderDraged) {
                if (zoom != 0) sliderDraged = true;
                zoomValue = lt.getWidth(roadType);
                gc.setLineWidth(zoomValue);
            } else {
                if (zoom < 5.0) {
                    zoomValue = lt.getWidth(roadType) / 0.1;
                } else {
                    zoomValue = lt.getWidth(roadType) / (zoom / 45);
                }
                gc.setLineWidth(zoomValue);
            }
        }
        super.draw(gc, zoom, darkMode, cs);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.00001);
    }

    public String getRoadType(){
        return roadType;
    }

    /*@Override
    public void fill(GraphicsContext gc, boolean darkMode, ColorScheme cs, String type){
        return;
    }*/

    @Override
    public boolean isRoad(){
        return true;
    }
}
