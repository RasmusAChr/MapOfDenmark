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
    private List<double[]> orderedMemberCoords;


    public Relation(String type, List<Member> memberRefs) {
        ColorScheme cs = new ColorScheme();
        this.type = type;
        this.members = memberRefs;
       // orderedMembersCoords();
    }
    // lav en tom arraylist af coords
    // tjek arraylist. size for sidste coords
    // tjek med den nye memeber om den matcher de sidste endte normalt eller reversed til føj dernæst alle coords til listen
    // hvis memberen ikke passer i rækkegølgen gå igenem igen senere.

    public void orderMember(){
        orderedMemberCoords = new ArrayList<>();
        // Add first member to list
        Member firstmember = members.get(0);
        orderedMemberCoords.add(firstmember.getWay().getCoords());
        Node last = firstmember.getWay().GetNodes().get(firstmember.getWay().GetNodes().size());
        while (!members.isEmpty()){
            // Check end node in member matches first in a while loop if not check the reversed
            for(Member m : members){
                Node next_First = m.getWay().GetNodes().get(0);

                if (last == next_First) {
                    orderedMemberCoords.add(m.getWay().getCoords());
                    last = m.getWay().GetNodes().get(m.getWay().GetNodes().size());
                }
                else if (last == reverseNodes(m).get(0)) {
                    reverseNodes(m);
                    
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
    }
*/

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
        boolean firstElementParsed = false;
        boolean secondElementParsed = false;
        gc.setFill(Color.PINK);
         List<Double> coordsList = new ArrayList<>();
        for (Member m : members) {
            Way way = m.getWay();
            // Sometimes a way can be null, if it's not in the OSM file, but a
            // part of the relation is and every member reference will be stored.
            if (way == null) break;

            double[] coords = way.getCoords();

          /*  if (m == members.get(0)) { // Check if this is the first member
                if (members.size() > 1) { // Check if there is a next member
                    double[] nextList = members.get(1).getWay().getCoords();
                    if (coords[coords.length - 1] != nextList[0]) {
                        // Reverse the first array
                        coords = reverseCoords(coords);
                    }
                }
            }*/

            for (Double coord : coords) {
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
        }

        // Draw the polygon
        gc.fill();
    }

    public List<Node> reverseNodes(Member mem){
        ArrayList<Node> listOfNodes = mem.getWay().GetNodes();
        ArrayList<Node> reversOfNodes = new ArrayList<>();
        for(int i = listOfNodes.size() - 1; i >= 0; i-- ){
            reversOfNodes.add(listOfNodes.get(i));
        }
        return reversOfNodes;
    }





}

