package com.telos.mapofdenmark;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Relation implements Serializable {
    private String type;
    private List<Member> memberRefs;


    public Relation(String type, List<Member> memberRefs) {
        ColorScheme cs = new ColorScheme();
        this.type = type;
        this.memberRefs = memberRefs;
        for (Member m : memberRefs) {
            System.out.println(m.way + " " + m.getType() + " " + m.ref);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Member> getMemberRefs() {
        return memberRefs;
    }

    public void setMemberRefs(List<Member> memberRefs) {
        this.memberRefs = memberRefs;
    }

    public void Draw(GraphicsContext gc, double zoom, boolean darkMode) {
        System.out.println("Trying to draw Relation");

        for (Member m : memberRefs) {
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
                    gc.fill();
                    gc.setStroke(Color.BLACK);
                    gc.stroke();
                    System.out.println("relation drawn");
                }
            }
        }
    }
}

