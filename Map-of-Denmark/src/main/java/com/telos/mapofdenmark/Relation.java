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
        Member firstmember = members.get(0);
        orderedNodes.addAll(firstmember.getWay().getNodes());

        System.out.println(firstmember.getWay().getNodes());

        // Get the last node in the list
        Node lastNode = orderedNodes.get(orderedNodes.size()-1);

        // Runs as long as there are members which nodes haven't been added to the list
        while (!members.isEmpty()){
            // Check if there is a member whose first Node matches the last node in list
            for(Member m : members){
                if (m.getWay() == null) {
                    drawable = false;
                    return;
                }
                Node currentFirstNode = m.getWay().getNodes().get(0);

                // If the last node in list is the same as the first node in current member
                if (lastNode == currentFirstNode) {
                    orderedNodes.addAll(m.getWay().getNodes());
                    lastNode = orderedNodes.get(orderedNodes.size()-1);
                    members.remove(m);
                }
                // Else if the last node in list is the same as the last node in current member
                else if (lastNode == reverseNodes(m).get(0)) {
                    orderedNodes.addAll(reverseNodes(m));
                    lastNode = orderedNodes.get(orderedNodes.size()-1);
                    members.remove(m);
                }
            }
        }
    }





  /*  public void orderedMembersCoords(){
        orderedMemberCoords = new ArrayList<>();
        //Get first member coordinates
        Member firstmember = members.get(0);
        double[] coordsoffirst = firstmember.getWay().getCoords();
        if (coordsoffirst == null) return;
        // Add first member coordinates to list
        for (double val : coordsoffirst){
            orderedMemberCoords.add(val);
            System.out.println(val);
        }

        // For every member (except first), check for last coordinate to see if there is a match in x,y value
        for (int i = 1; i < members.size()-1; i++){
            Member nextmem = members.get(i);
            double[] coordsOfNextMem = nextmem.getWay().coords;
            if (coordsOfNextMem == null) return;
            // Check for x value -2 and for y -1
            System.out.println("Ordered corrds " + orderedMemberCoords.get(orderedMemberCoords.size()-2) + " " + orderedMemberCoords.get(orderedMemberCoords.size()-1));
            System.out.println("Needs to match " + coordsOfNextMem[0] + " " + coordsOfNextMem[1] + " or " + reverseCoords(coordsOfNextMem)[0] + " " +reverseCoords(coordsOfNextMem)[1] );
            if (orderedMemberCoords.get(orderedMemberCoords.size()-2) == coordsOfNextMem[0] &&
                orderedMemberCoords.get(orderedMemberCoords.size()-1) == coordsOfNextMem[1]) {
                System.out.println("coords added for later than second");
                // If next members first coords is equal to previous members last coords, add to coords list
                for (double val : nextmem.getWay().coords){
                    orderedMemberCoords.add(val);

                }
                // if the next members reversed coors is equal to the previous members last coords
            } else if (orderedMemberCoords.get(orderedMemberCoords.size()-2) == reverseCoords(coordsOfNextMem)[0] &&
                    orderedMemberCoords.get(orderedMemberCoords.size()-1) == reverseCoords(coordsOfNextMem)[1]) {
                System.out.println("coords added for later than second 2");
                    // adds the reversed coords to the list
                for (double val : reverseCoords(coordsOfNextMem)){
                    orderedMemberCoords.add(val);
                }
            }

        }
    }*/

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
                xPoints[i] = orderedNodes.get(i).getLat();
                yPoints[i] = orderedNodes.get(i).getLon();
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

