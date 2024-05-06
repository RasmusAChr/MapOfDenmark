package com.telos.mapofdenmark;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.FillRule;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a relation composed of nodes and ways.
 */
public class Relation implements Serializable {
    private String landform;
    private List<Member> members;
    private List<Node> orderedNodes;
    private List<List<Node>> listOfOrderedNodes;
    private List<Way> innerWays, singleWays;
    boolean drawable;
    Node lastNode = null;
    ColorScheme cs;

    /**
     * Constructs a Relation object with specified member references, landform, and color scheme.
     *
     * @param memberRefs the list of members in the relation
     * @param landform the landform associated with the relation
     * @param cs the color scheme for drawing
     */
    public Relation(List<Member> memberRefs, String landform, ColorScheme cs) {
        this.landform = landform;
        this.members = memberRefs;
        this.drawable = true;
        this.innerWays = new ArrayList<>();
        this.orderedNodes = new ArrayList<>();
        this.singleWays = new ArrayList<>();
        this.listOfOrderedNodes = new ArrayList<>();
        this.cs = cs;
        orderNodes();
    }

    /**
     * Orders the nodes and ways based on relation membership.
     */
    public void orderNodes(){

        // If a way doesn't exist, then don't manipulate/draw it.
        for (Member m : members) {
            if (m.getWay() == null) {
                drawable = false;
                return;
            }
        }

        while (!addToOrderedNodes().isEmpty()) addToOrderedNodes();
    }

    /**
     * Adds ordered nodes to the list.
     *
     * @return the leftover members after ordering
     */
    public List<Member> addToOrderedNodes (){
        List<Member> leftOverMembers = new ArrayList<>();

        // Use iterator to iterate over members list
        // Using iterator because you cant manipulate a normal list.
        ListIterator<Member> iterator = members.listIterator();
        while (iterator.hasNext()) {
            Member m = iterator.next();

            Node currentFirstNode = m.getWay().getNodes().get(0);
            if(m.getType().equals("inner")){
                innerWays.add(m.getWay());
                iterator.remove();
            }
            // If the first and last node is the same, it's a single way
            else if (currentFirstNode == m.getWay().getNodes().get(m.getWay().getNodes().size()-1)){
                singleWays.add(m.getWay());
                iterator.remove();
            }
            // If the next way is not inner or a single way, then add the first node to the ordered list.
            else if (lastNode == null){
                orderedNodes.addAll(m.getWay().getNodes());
                lastNode = orderedNodes.get(orderedNodes.size()-1);
                iterator.remove();
            }
            // If the last node in list is the same as the first node in current member
            else if (lastNode.equals(currentFirstNode)) {
                orderedNodes.addAll(m.getWay().getNodes());
                lastNode = orderedNodes.get(orderedNodes.size()-1);
                iterator.remove(); // Remove the current member using the iterator
                if (lastNode == orderedNodes.get(0)){
                    listOfOrderedNodes.add(new ArrayList<>(orderedNodes));
                    lastNode = null;
                    orderedNodes.clear();
                }
            }
            // Else if the last node in list is the same as the last node in current member
            else if (lastNode.equals(reverseNodes(m).get(0))) {
                orderedNodes.addAll(reverseNodes(m));
                lastNode = orderedNodes.get(orderedNodes.size()-1);
                iterator.remove(); // Remove the current member using the iterator
                if (lastNode == orderedNodes.get(0)){
                    listOfOrderedNodes.add(new ArrayList<>(orderedNodes));
                    lastNode = null;
                    orderedNodes.clear();
                }
            }
            else {

                leftOverMembers.add(m);
            }
        }
        return leftOverMembers;
    }

    /**
     * Draws the relation on the specified GraphicsContext.
     *
     * @param gc the GraphicsContext to draw on
     * @param zoom the zoom level
     * @param darkMode indicates if dark mode is enabled
     * @param type the type of relation to draw
     */
    public void Draw(GraphicsContext gc, double zoom, boolean darkMode, String type) {
        gc.setFill(cs.getColor(landform,darkMode,type));
        gc.setFillRule(FillRule.EVEN_ODD);
        gc.beginPath();

        if (drawable){
            // FOR OUTER
            for (List<Node> nodeList : listOfOrderedNodes){
                if (!nodeList.isEmpty()) {
                    double[] xPoints = new double[nodeList.size()];
                    double[] yPoints = new double[nodeList.size()];
                    for (int i = 0; i < nodeList.size(); i++) {
                        var node = nodeList.get(i);
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
            }

            // FOR INNER
            if(!innerWays.isEmpty()) {
                for (Way w : innerWays) {
                    double[] coords = w.getCoords();
                    gc.moveTo(coords[0], coords[1]);
                    for (int i = 2; i < coords.length; i += 2) {
                        gc.lineTo(coords[i], coords[i + 1]);
                    }
                }
            }

            if (!singleWays.isEmpty()){
                for (Way w : singleWays) {
                    double[] coords = w.getCoords();
                    gc.moveTo(coords[0], coords[1]);
                    for (int i = 2; i < coords.length; i += 2) {
                        gc.lineTo(coords[i], coords[i + 1]);
                    }
                }
            }
            gc.fill();
            gc.stroke();
        }
    }

    /**
     * Reverses the order of nodes in a member's way.
     *
     * @param mem the member containing the way to reverse
     * @return the list of nodes in reversed order
     */
    public List<Node> reverseNodes(Member mem){
        ArrayList<Node> listOfNodes = mem.getWay().getNodes();
        ArrayList<Node> reversOfNodes = new ArrayList<>();
        for(int i = listOfNodes.size() - 1; i >= 0; i-- ){
            reversOfNodes.add(listOfNodes.get(i));
        }
        return reversOfNodes;
    }
}

