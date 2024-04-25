package com.telos.mapofdenmark;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;

import javax.security.auth.login.AccountNotFoundException;
import java.io.Serializable;
import java.util.*;

public class Relation implements Serializable {
    private String type;
    private List<Member> members;
    private List<Node> orderedNodes;
    boolean drawable;


    public Relation(String type, List<Member> memberRefs) {
        ColorScheme cs = new ColorScheme();
        this.type = type;
        this.members = memberRefs;
        this.drawable = true;
        orderNodes();
    }
    // lav en tom arraylist af coords
    // tjek arraylist. size for sidste coords
    // tjek med den nye memeber om den matcher de sidste endte normalt eller reversed til føj dernæst alle coords til listen
    // hvis memberen ikke passer i rækkegølgen gå igenem igen senere.

    public void orderNodes(){
        orderedNodes = new ArrayList<>();
        // Add first member to list
        //Member firstmember = members.get(0);
        //orderedNodes.addAll(firstmember.getWay().getNodes());

        // Get the last node in the list
        Node lastNode = null;//orderedNodes.get(orderedNodes.size()-1);

        // Use iterator to iterate over members list
        // Using iterator because you cant manipulate a normal list.
        ListIterator<Member> iterator = members.listIterator();
        while (iterator.hasNext()) {
            Member m = iterator.next();
            if (m.getWay() == null) {
                drawable = false;
                return;
            }

            Node currentFirstNode = m.getWay().getNodes().get(0);

            if (lastNode == null){
                orderedNodes.addAll(m.getWay().getNodes());
                lastNode = orderedNodes.get(orderedNodes.size()-1);
                iterator.remove();
                System.out.println("first line added");
            }
            // If the last node in list is the same as the first node in current member
            else if (lastNode.equals(currentFirstNode)) {
                orderedNodes.addAll(m.getWay().getNodes());
                lastNode = orderedNodes.get(orderedNodes.size()-1);
                iterator.remove(); // Remove the current member using the iterator
                System.out.println("normal added");
            }
            // Else if the last node in list is the same as the last node in current member
            else if (lastNode.equals(reverseNodes(m).get(0))) {
                orderedNodes.addAll(reverseNodes(m));
                lastNode = orderedNodes.get(orderedNodes.size()-1);
                iterator.remove(); // Remove the current member using the iterator
                System.out.println("reversed added");
            }
            else {
                //iterator.add(m);
                System.out.println("else");
            }
        }
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

        if (drawable){

            double[] xPoints = new double[orderedNodes.size()];
            double[] yPoints = new double[orderedNodes.size()];

            for (int i = 0; i < orderedNodes.size(); i++){
                var node = orderedNodes.get(i);
                System.out.println("(" + node.getLat() + "," + node.getLon() + ")");
                xPoints[i] = 0.56 * node.getLon();
                yPoints[i] = -node.getLat();
            }

            // Moves to start coordinates
            gc.moveTo(xPoints[0], yPoints[0]);

            // Creates a not visible line to next coordinate (to create selection).
            for (int i = 0; i < xPoints.length; i++){
                gc.lineTo(xPoints[i],yPoints[i]);
            }

            // Draw the polygon
            gc.fill();
        }
        System.out.println("--------------------------");

    }

    public List<Node> reverseNodes(Member mem){
        ArrayList<Node> listOfNodes = mem.getWay().getNodes();
        ArrayList<Node> reversOfNodes = new ArrayList<>();
        for(int i = listOfNodes.size() - 1; i >= 0; i-- ){
            reversOfNodes.add(listOfNodes.get(i));
        }
        return reversOfNodes;
    }





}

