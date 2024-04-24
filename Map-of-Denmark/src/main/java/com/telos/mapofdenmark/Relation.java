package com.telos.mapofdenmark;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;

import java.io.Serializable;
import java.util.*;

public class Relation implements Serializable {
    private String type;
    private List<Member> members;


    public Relation(String type, List<Member> memberRefs) {
        ColorScheme cs = new ColorScheme();
        this.type = type;
        this.members = memberRefs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Member> getMember() {
        return members;
    }

    public void setMember(List<Member> member) {
        this.members = member;
    }

    public void Draw(GraphicsContext gc, double zoom, boolean darkMode) {
        gc.setFill(Color.PINK);
        List<Double> coordsList = new ArrayList<>();
        for (Member m : members) {
            Way way = m.getWay();
            // Sometimes a way can be null, if it's not in the OSM file, but a
            // part of the relation is and every member reference will be stored.
            if (way == null) break;

            for (Double coord : way.getCoords()) {
                coordsList.add(coord);
            }
        }
        // Create separate arrays for x and y coordinates
        double[] xPoints = new double[coordsList.size() / 2];
        double[] yPoints = new double[coordsList.size() / 2];
        for (int i = 0; i < coordsList.size(); i += 2) {
            xPoints[i / 2] = coordsList.get(i);
            yPoints[i / 2] = coordsList.get(i + 1);
        }
        // Moves to start coordinates
        gc.moveTo(xPoints[0], yPoints[0]);

        // Creates a not visible line to next coordinate (to create selection).
        for (int i = 0; i < xPoints.length; i++){
            gc.lineTo(xPoints[i],yPoints[i]);
            System.out.println(xPoints[i] + ", " + yPoints[i]);
        }

        // Draw the polygon
        gc.fill();
        System.out.println("------------------------------------------------");
    }




}

