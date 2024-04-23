package com.telos.mapofdenmark;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;

import java.io.Serializable;
import java.util.List;

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
        gc.setFillRule(FillRule.EVEN_ODD); // Set fill rule to even-odd
        gc.setFill(Color.PINK);

        if (type.equals("multipolygon")) {
            for (Member m : members) {
                Way way = m.getWay();

                if (way == null) break;

                double[] coords = way.getCoords();



                // Create separate arrays for x and y coordinates
                double[] xPoints = new double[coords.length / 2];
                double[] yPoints = new double[coords.length / 2];
                for (int i = 0; i < coords.length; i += 2) {
                    xPoints[i / 2] = coords[i];
                    yPoints[i / 2] = coords[i + 1];
                }

                gc.moveTo(xPoints[0], yPoints[0]);
                for (int i = 0; i < xPoints.length; i++){
                    gc.lineTo(xPoints[i],yPoints[i]);
                }


                //gc.fillPolygon(xPoints, yPoints, xPoints.length);
            }
            // Draw the polygon
            gc.fill();
        }

        /*if(type.equals("multipolygon")) {
            List<Double> xPoints = new ArrayList<>(), yPoints = new ArrayList<>();
            Polygon p = new Polygon();
            for (Member m : memberRefs) {

                    double[] coords = m.way.getCoords();
                    p.getPoints().add(coords[0]);
                    p.getPoints().add(coords[1]);
            }

            p.setFill(Color.PINK);
            p.setStroke(Color.BLACK);
            gc.beginPath();
            double x = p.getPoints().get(0);
            double y = p.getPoints().get(1);
            xPoints.add(x);
            yPoints.add(y);
            gc.moveTo(x, y);
            for(int i = 0; i < p.getPoints().size(); i++) {

            }

        }*/
      /*  for (Member m : memberRefs) {
            if (m.getType().equals("outer")) {
                gc.beginPath();
                Way way = m.way;
                if (!(way == null)) {
                    double[] coords = way.getCoords();
                    gc.moveTo(coords[0], coords[1]);
                    for (int i = 2; i < coords.length; i += 2) {
                        gc.lineTo(coords[i], coords[i + 1]);
                    }
                    gc.closePath();
                    gc.setFill(Color.PINK);
                    gc.setFillRule(FillRule.EVEN_ODD);
                    gc.fill();
                    gc.setStroke(Color.PINK);
                    gc.stroke();
                    System.out.println("relation drawn");
                }
            }
            if (m.getType().equals("inner")) {
                gc.beginPath();
                Way way = m.way;
                if (!(way == null)) {
                    double[] coords = way.getCoords();
                    gc.moveTo(coords[0], coords[1]);
                    for (int i = 2; i < coords.length; i += 2) {
                        gc.lineTo(coords[i], coords[i + 1]);
                    }
                    gc.closePath();
                    gc.setFill(Color.GREEN);
                    gc.setFillRule(FillRule.EVEN_ODD);
                    gc.fill();
                    gc.setStroke(Color.BLACK);
                    gc.stroke();
                    System.out.println("relation drawn");
                }
            }
        }*/
    }
}

