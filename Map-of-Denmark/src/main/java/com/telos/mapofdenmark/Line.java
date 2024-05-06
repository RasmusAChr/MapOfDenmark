package com.telos.mapofdenmark;

import java.io.Serializable;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/**
 * Represents a line segment connecting a series of nodes.
 * This class provides methods to draw the line segment on a graphics context.
 */
public class Line implements Serializable {
    // List of nodes forming the line
    List<Node> way;
    // Array of coordinates representing the line
    double[] coords;

    /**
     * Constructs a Line object with the specified list of nodes.
     * @param way The list of nodes forming the line
     */
    public Line(List<Node> way) {
        this.way = way;
        coords = new double[way.size() * 2];
        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
            coords[2 * i] = 0.56 * node.lon;
            coords[2 * i + 1] = -node.lat;
        }
    }

    /**
     * Draws the line segment on the specified graphics context.
     * @param gc    The graphics context to draw on
     * @param dark  Whether dark mode is enabled
     * @param zoom  The zoom level used for line width
     */
    public void draw(GraphicsContext gc, boolean dark, double zoom) {
        double zoomValue;
        gc.beginPath();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
        }
        if (zoom < 10.0) {
            zoomValue = 0.002;
        } else {
            zoomValue = 0.0001 / (zoom/120);
        }
        gc.setLineWidth(zoomValue);
        gc.setStroke(Color.web("#4834d4"));
        if (dark) gc.setStroke(Color.web("#686de0"));
        gc.stroke();
    }

}