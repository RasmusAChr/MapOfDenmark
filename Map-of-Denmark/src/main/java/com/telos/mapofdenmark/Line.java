package com.telos.mapofdenmark;

import java.io.Serializable;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line implements Serializable {
    List<Node> way;
    double[] coords;

    public Line(List<Node> way) {
        this.way = way;
        coords = new double[way.size() * 2];
        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
            coords[2 * i] = 0.56 * node.lon;
            coords[2 * i + 1] = -node.lat;
        }
    }

    public void draw(GraphicsContext gc, boolean dark, double zoom) {
        double zoomValue;
        if (zoom < 0.1) {
            zoomValue = 0.00003 / 10.666;
        } else {
            zoomValue = 0.03 / (zoom/0.45);
        }
        gc.setLineWidth(zoomValue);
        gc.beginPath();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
        }
        gc.setStroke(Color.BLUE);
        if (dark) gc.setStroke(Color.YELLOW);
        //gc.setLineWidth(0.0001);
        gc.stroke();
    }
}