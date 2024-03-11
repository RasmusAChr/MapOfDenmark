package com.example.mapofdenmark;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Way implements Serializable {
    double[] coords;
    String type;


    public Way(ArrayList<Node> way, String type) {
        this.type = type;
        coords = new double[way.size() * 2];
        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
            coords[2 * i] = 0.56 * node.lon;
            coords[2 * i + 1] = -node.lat;
        }
    }

    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
        }
        gc.stroke();
    }

    public void fillPolygon(GraphicsContext gc){
        if (type.equals("building")) gc.setFill(Color.GREEN);

        // Andre tags her

        double[] xPoints = new double[coords.length / 2];
        double[] yPoints = new double[coords.length / 2];

        for (int i = 0; i < coords.length; i += 2) {
            xPoints[i / 2] = coords[i];
            yPoints[i / 2] = coords[i + 1];
        }

        gc.fillPolygon(xPoints, yPoints, xPoints.length);
    }

}