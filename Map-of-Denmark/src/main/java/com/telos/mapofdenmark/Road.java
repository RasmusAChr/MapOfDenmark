package com.telos.mapofdenmark;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class Road represents a road in a OSM map. The class inherits from the Way class, but also adds attributes
 * such as the kind of road type it is and the line thickness used for it.
 */
public class Road extends Way implements Serializable {
    String roadType;
    LineThickness lt;
    boolean sliderDraged = false;

    /**
     * Constructor used to construct a Road object with the given parameters
     * @param way - The list of nodes that is representing the road
     * @param roadType - The type of road
     * @param lt - The line thickness of the road
     */
    public Road(ArrayList<Node> way, String roadType, LineThickness lt) {
        super(way, "", "road");
        this.roadType = roadType;
        this.lt = lt;

    }

    /**
     * Used to draw the road on the canvas/graphics context
     * @param gc - The graphics context to draw on
     * @param zoom - The zoom level
     * @param darkMode - Indicates whether dark mode is enabled
     * @param cs - The color scheme to use for drawing
     */
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

    /**
     * Retrieves the type of the road
     * @return - The type of road in string format
     */
    public String getRoadType(){
        return roadType;
    }

    /**
     * Indicates whether the object represents a road or not
     * @return - true = the object does represent a road, false = the object does not represent a road
     */
    @Override
    public boolean isRoad(){
        return true;
    }
}
