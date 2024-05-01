package com.telos.mapofdenmark;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.FillRule;

import java.io.Serializable;
import java.util.*;

public class RelationTwo implements Serializable {
    private String type;
    private String landform;

    private List<Member> members;
    private List<Node> orderedNodes;
    private List<Way> innerWays;
    private List<Member> nonRelatedMembers;
    boolean drawable;
    Node lastNode = null;
    ColorScheme cs = new ColorScheme();
    long tempM = 0;

    public RelationTwo(String type, List<Member> memberRefs, String landform) {
        ColorScheme cs = new ColorScheme();
        this.type = type;
        this.landform = landform;
        this.members = memberRefs;
        this.drawable = true;
        this.innerWays = new ArrayList<>();
        this.nonRelatedMembers = new ArrayList<>();
        this.orderedNodes = new ArrayList<>();
        orderNodes();
    }

    public void orderNodes(){



        // Check if a node appears 2 times. If not place the way with that node in another list.
        //System.out.println("non related members:");
        for (Member m : members) {
            if (m.getWay() == null) {
                drawable = false;
                return;
            }
            if (hasDuplicateCoordinates(m)) {
                nonRelatedMembers.add(m);
                //System.out.println(m.getRef());
                //System.out.println("lastNode: " + lastNode.id + "    node: " + m.getWay().getNodes().get(m.getWay().getNodes().size()-1).id);
            }
        }

        // Remove the collected members after the iteration
        members.removeAll(nonRelatedMembers);

        if (addToOrderedNodes() == null) {
            //System.out.println("EMPTY");
            drawable = false;
            return;
        }


        while (!addToOrderedNodes().isEmpty()) addToOrderedNodes();
        //System.out.println("------------------------------");

    }

    public boolean hasDuplicateCoordinates(Member member) {
        //Set<Double> encounteredCoordinates = new HashSet<>();
        Set<Point2D> encounteredCoordinates = new HashSet<>();
        for (int i = 0; i < member.getWay().getCoords().length; i += 2) {
            if (!encounteredCoordinates.add(new Point2D(member.getWay().getCoords()[i], member.getWay().getCoords()[i+1]))) {
                return true; // Coordinate encountered more than once
            }
        }
        return false; // No coordinate encountered more than once
    }

    public List<Member> addToOrderedNodes (){
        List<Member> leftOverMembers = new ArrayList<>();

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
                System.out.println("First node " + m.getRef());
                tempM = m.getRef();
                orderedNodes.addAll(m.getWay().getNodes());
                lastNode = orderedNodes.get(orderedNodes.size()-1);
                iterator.remove();
            }
            // If the last node in list is the same as the first node in current member
            else if (lastNode.equals(currentFirstNode)) {
                orderedNodes.addAll(m.getWay().getNodes());
                lastNode = orderedNodes.get(orderedNodes.size()-1);
                iterator.remove(); // Remove the current member using the iterator
            }
            // Else if the last node in list is the same as the last node in current member
            else if (lastNode.equals(reverseNodes(m).get(0))) {
                orderedNodes.addAll(reverseNodes(m));
                lastNode = orderedNodes.get(orderedNodes.size()-1);
                iterator.remove(); // Remove the current member using the iterator
            }
            else {
                System.out.println("First way " + tempM);
                System.out.println("https://www.openstreetmap.org/way/" + m.getRef() + "#map=11/55.1424/14.9641");
                leftOverMembers.add(m);
            }
        }
        return leftOverMembers;
    }

    public void Draw(GraphicsContext gc, double zoom, boolean darkMode) {
        gc.setFill(cs.getColor(landform,darkMode));
        gc.setFillRule(FillRule.EVEN_ODD);
        gc.beginPath();

        if (drawable){
            // FOR OUTER
            if (!orderedNodes.isEmpty()){
                double[] xPoints = new double[orderedNodes.size()];
                double[] yPoints = new double[orderedNodes.size()];
                for (int i = 0; i < orderedNodes.size(); i++) {
                    var node = orderedNodes.get(i);
                    xPoints[i] = 0.56 * node.getLon();
                    yPoints[i] = -node.getLat();
                }
                // Moves to start coordinates
                gc.moveTo(xPoints[0], yPoints[0]);
                // Creates a not visible line to next coordinate (to create selection).
                for (int i = 0; i < xPoints.length; i++) {
                    gc.lineTo(xPoints[i], yPoints[i]);
                }
            }

            // FOR INNER
            if(!innerWays.isEmpty())
                for(Way w : innerWays){
                    double[] coords = w.getCoords();
                    gc.moveTo(coords[0], coords[1]);
                    for (int i = 2 ; i < coords.length ; i += 2) {
                        gc.lineTo(coords[i], coords[i+1]);
                    }
                }

            // FOR NON RELATED MEMBERS
            if (!nonRelatedMembers.isEmpty()){
                for (Member m : nonRelatedMembers){
                    Way w = m.getWay();
                    double[] coords = w.getCoords();
                    gc.moveTo(coords[0], coords[1]);
                    for (int i = 2 ; i < coords.length ; i += 2) {
                        gc.lineTo(coords[i], coords[i+1]);
                    }
                }
            }

            gc.fill();
            gc.stroke();
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

