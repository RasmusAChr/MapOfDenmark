package com.telos.mapofdenmark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Way implements Serializable {
    double[] coords;
    ArrayList<Node> nodesInWay;

    public Way(ArrayList<Node> way) {
        nodesInWay = new ArrayList<>();
        nodesInWay.addAll(way);
        coords = new double[way.size() * 2];
        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
            coords[2 * i] = 0.56 * node.lon;
            coords[2 * i + 1] = -node.lat;
        }
    }
    public double[] getCoords() {
        return coords;
    }

    public void draw(GraphicsContext gc, double zoom, boolean darkMode) {
        gc.beginPath();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
        }
        gc.stroke();
    }
    public ArrayList<Node> getNodes(){
        return nodesInWay;
    }


}