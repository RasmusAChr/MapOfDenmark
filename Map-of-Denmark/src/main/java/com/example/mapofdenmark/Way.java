package com.example.mapofdenmark;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Way implements Serializable {
    double[] coords;
    String type;
    boolean objectType;



    public Way(ArrayList<Node> way, String wayType, boolean objectType) {
        this.type = wayType;
        this.objectType = objectType;
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
        if (type.equals("asphalt")) gc.setStroke(Color.web("#ffffff"));
        else{gc.setStroke(Color.BLACK);}
        gc.stroke();

    }

    public void fillPolygon(GraphicsContext gc) {
        if (type.equals("building")) gc.setFill(Color.web("#fcffa8"));
        if (type.equals("water")) gc.setFill(Color.web("#addeff"));
        if (type.equals("park")) gc.setFill(Color.web("#cbe4c4"));


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