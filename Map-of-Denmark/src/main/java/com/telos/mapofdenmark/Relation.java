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
    private List<Way> innerWays;
    boolean drawable;
    Node lastNode = null;


    public Relation(String type, List<Member> memberRefs) {
        ColorScheme cs = new ColorScheme();
        this.type = type;
        this.members = memberRefs;
        this.drawable = true;
        this.innerWays = new ArrayList<>();
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

        if (addToOrderedNodes() == null) return;

        while (!addToOrderedNodes().isEmpty()) addToOrderedNodes();

    }

    public List<Member> addToOrderedNodes (){
        List<Member> leftOverMembers = new ArrayList<>();

        // Get the last node in the list

        // Use iterator to iterate over members list
        // Using iterator because you cant manipulate a normal list.
        ListIterator<Member> iterator = members.listIterator();
        while (iterator.hasNext()) {
            Member m = iterator.next();
            if (m.getWay() == null) {
                drawable = false;
                return null;
            }

            Node currentFirstNode = m.getWay().getNodes().get(0);
            if(m.getType().equals("inner")){
                innerWays.add(m.getWay());
                iterator.remove();
            }
            else if (lastNode == null){
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
                leftOverMembers.add(m);
                System.out.println("else");
                System.out.println(m.getRef());
            }
        }
        return leftOverMembers;
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
        gc.setFillRule(FillRule.EVEN_ODD);

        if (drawable){
            // FOR OUTER
            double[] xPoints = new double[orderedNodes.size()];
            double[] yPoints = new double[orderedNodes.size()];
            for (int i = 0; i < orderedNodes.size(); i++){
                var node = orderedNodes.get(i);
                xPoints[i] = 0.56 * node.getLon();
                yPoints[i] = -node.getLat();
            }
            // Moves to start coordinates
            gc.moveTo(xPoints[0], yPoints[0]);
            // Creates a not visible line to next coordinate (to create selection).
            for (int i = 0; i < xPoints.length; i++){
                gc.lineTo(xPoints[i],yPoints[i]);
            }
            //gc.fill();

            // For inner
            if(!innerWays.isEmpty())
                for(Way W : innerWays){
                    double[] coords = W.getCoords();
                    gc.moveTo(coords[0], coords[1]);
                    for (int i = 2 ; i < coords.length ; i += 2) {
                        gc.lineTo(coords[i], coords[i+1]);
                    }

                    /*List<Node> nodes = W.getNodes();
                    double[] xPointsInner = new double[nodes.size()];
                    double[] yPointsInner = new double[nodes.size()];*/
                    System.out.println("inner");
                    /*for (int i = 0; i < nodes.size(); i++){
                        var node = nodes.get(i);
                        xPointsInner[i] = 0.56 * node.getLon();
                        yPointsInner[i] = -node.getLat();
                    }
                    gc.moveTo(xPointsInner[0],yPointsInner[0]);


                    for (int i = 0; i < xPointsInner.length; i++){
                        gc.lineTo(xPointsInner[i],yPointsInner[i]);
                    }*/

                    // Draw the seperated ways for the inners.
                /*gc.setFill(Color.GREEN);
                W.draw(gc, zoom, darkMode);
                gc.fill();*/
                }

            gc.fill();
            gc.stroke();
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

