package com.telos.mapofdenmark.Objects;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;

/**
 * Represents a way, or a path, on a map. A way is defined by a sequence of nodes
 * (points) that describe a continuous path. This class provides functionality to
 * create a way from a list of nodes and to draw this way on a JavaFX canvas using
 * a {@link GraphicsContext}.
 */
public class Way implements Serializable {
    /**
     * Array of coordinates representing the path of the way. Each pair of values in
     * this array corresponds to a point on the map, where the first value of the pair
     * is the x-coordinate (longitude), and the second is the y-coordinate (latitude).
     */
    double[] coords;
    String type;

    /**
     * Constructs a Way object from a list of nodes. The way is represented as an
     * array of coordinates, where each node's longitude and latitude are converted
     * and stored.
     *
     * @param way The list of {@link Node} objects that define the path of the way.
     *            Each node contains longitude and latitude information that are
     *            converted and stored in the {@code coords} array. The longitude is
     *            multiplied by 0.56, and the latitude is stored as a negative value.
     */
    public Way(ArrayList<Node> way, String type) {
        this.type = type;
        coords = new double[way.size() * 2];
        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
            coords[2 * i] = 0.56 * node.lon;
            coords[2 * i + 1] = -node.lat;
        }
    }

    /**
     * Draws the way on a JavaFX canvas. The method uses the {@link GraphicsContext}
     * from the canvas to draw lines between the coordinates stored in the {@code coords}
     * array.
     *
     * @param gc The {@link GraphicsContext} of a JavaFX canvas, used to draw the way.
     */
    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
        }
        gc.stroke();
    }

    public String getType(){
        return type;
    }

}