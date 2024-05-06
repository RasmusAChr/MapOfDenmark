package com.telos.mapofdenmark.KDTreeClasses;

import com.telos.mapofdenmark.Node;
import com.telos.mapofdenmark.Way;

import java.io.Serializable;
import java.util.*;

// Inspiration to KDTree from https://www.geeksforgeeks.org/search-and-insertion-in-k-dimensional-tree/
// We also utilized code from chapter 3 in Algorithms 4 by Wayne and Sedgewick ch 3.2 about BST (Binary Search Trees)
/**
 * The KDTree class is a spatial data structure that supports organizing nodes in a 2D dimensional space.
 * The data structure supports the following methods: Insertion method to insert nodes into the KDTree, deleteMin which deletes the smallest key in the KDTree,
 * rangeSearch to find nodes within a given range, and also nearestNeighbor search which finds the nearest neighbor to a given node, with the option to find the nearest node
 * that is on a road or just the nearest node in general.
 */
public class KDTreeWay implements Serializable
{
    final static long serialVersionUID = 12851802756812749L;
    private KDNode root; // root of BST
    /**
     * The KDNode class is an inner class inside the KDTree class. This class represents a node that
     * is stored inside the KDTree.
     */
    private class KDNode implements Serializable{
        final static long serialVersionUID = 3254120512025412L;
        double x, y; // Coordinates of the point which will be used for the line that will be drawn to create half planes
        private Way val;
        private KDNode left, right; // links to the left and right subtrees
        private int size; // number of nodes in the subtree rooted here
        /**
         * The constructor of the KDNode stores 4 different pieces of information
         * @param x - the X coordinate of the node after transformation
         * @param y - the Y coordinate of the node after transformation
         * @param val - the node's original x & y coordinates before transformation
         * @param size - the size of the subtree rooted at this current node
         */
        public KDNode(double x, double y, Way val, int size)
        {
            // Constructor has been changed from a normal BST to accomodate coordinates
            //this.key = key;
            this.x = x;
            this.y = y;
            this.val = val;
            this.size = size;
        }
    }
    /**
     * getter method to return the number of nodes inside a KDTree
     * @return number of nodes stored inside the KDTree
     */
    public int size() {
        // Returns the number of nodes in the BST
        return size(root);
    }
    /**
     * Helper method that returns the size of the subtree rooted at the given node
     * @param x - Root of the subtree
     * @return - size of subtree rooted at the node
     */
    private int size(KDNode x) {
        // Helper method to calculate size of a subtree rooted at a given node
        if (x == null) return 0;
        else return x.size;
    }
    /**
     * Method to check whether the KDTree is empty or not
     * @return - true if the KDTree is empty, and false if not
     */
    public boolean isEmpty() {
        // Checks if the BST is empty
        return size() == 0;
    }
    /**
     * Inserts a node with transformed coordinates, and original coordinates stored inside value
     * @param x - the transformed x coordinate of the node
     * @param y - the transformed y coordinate of the node
     * @param val - the original x & y coordinate of the node
     */
    public void put(double x, double y, Way val) {
        // Public method to insert a key-value pair; updates if key exists
        // Put method arguments change to acommodate nodes new attributes
        root = put(root, x, y, val, 0);
    }
    /**
     * A helper method that recursively inserts a node with the given transformed coordinates and the
     * original coordinates given in value
     * @param x - root of the subtree
     * @param xCoord - the transformed x-coordinate of the node
     * @param yCoord - the transformed y-coordinate of the node
     * @param val - the original x & y coordinate of the node
     * @param depth - the current depth of the subtree
     * @return - the root of the modified subtree
     */
    private KDNode put(KDNode x, double xCoord, double yCoord, Way val, int depth) {
        if (x == null) return new KDNode(xCoord, yCoord, val, 1);
        // Perform comparison based on the determined axis
        int cmp;
        // Determine the axis of comparison based on current depth (0 for x, 1 for y)
        int axis = depth % 2;
        // The following two conditionals is used to determine whether to divide half-plane vertically or horizontally
        if (axis == 0) cmp = Double.compare(xCoord, x.x); // Compare x-coordinates if axis is 0
        else cmp = Double.compare(yCoord, x.y); // Compare y-coordinates if axis is 1

        // Recursive insert: navigate left or right based on comparison result
        // If the compared value is less, go to the left subtree
        if (cmp < 0) x.left = put(x.left, xCoord, yCoord, val, depth + 1);
            // If the compared value is greater, go to the right subtree
        else if (cmp > 0) x.right = put(x.right, xCoord, yCoord, val, depth + 1);
            // If the point exactly matches (in the compared dimension), update the value.
        else x.val = val;

        // Update the size of the subtree rooted at this node
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }
    /**
     * Uses the given coordinates to return the node with it's original coordinates
     * @param x - the x-coordinate of the node
     * @param y - the y-coordinate of the node
     * @return - the node at the given coordinates, or null if no node was found
     */
    public KDNode get(double x, double y){
        return get(x, y, 0);
    }
    /**
     * Helper method to recursively return the node with it's original coordinates at the given
     * coordinates
     * @param xCoord - the transformed x-coordinate of the node
     * @param yCoord - the transformed y-coordinate of the node
     * @param depth - the current depth of the subtree
     * @return - a KDNode
     */
    private KDNode get(double xCoord, double yCoord, int depth) {
        // Retrieves the value associated with a given key
        KDNode x = root;
        int xDepth = depth;
        while (x != null)
        {
            int axis = depth % 2;
            // Vertical line
            if(axis == 0){
                int cmp = Double.compare(yCoord, x.x);
                if (cmp < 0){
                    x = x.left;
                }
                else if (cmp > 0) {
                    x = x.right;
                }
                else return x;
            } // Horizontal line
            else {
                int cmp = Double.compare(xCoord, x.y);
                if (cmp < 0){
                    x = x.left;
                }
                else if (cmp > 0) {
                    x = x.right;
                }
                else return x;
            }
        }
        // Increment depth at each step to alternate comparison axis
        return null;
    }

    /**
     * Method to traverse the KDTree in-order, printing out the coordinates of each node along
     * the way. Used to test whether the  {@link #populate(List)} method worked as intended
     */
    public void inOrderTraverse(){
        inOrderTraverse(root);
    }

    /**
     * Helper method for in-order traversal of the KDTree, which goes throught the tree
     * recursively
     * @param x - the current node that is traversing
     */
    private void inOrderTraverse(KDNode x) {
        // Traverses the BST in order
        if (x == null) return;
        inOrderTraverse(x.left);
        System.out.println(x.x + " " + x.y);
        inOrderTraverse(x.right);
    }

    /**
     * Tester method that traverses the KDTree in level order, printing out the coordinates
     * of each node along the way
     */
    public void levelOrderTraverse() {
        Queue<KDNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            KDNode x = queue.remove();
            if (x == null) continue;
            System.out.println(x.x + " " + x.y);
            queue.add(x.left);
            queue.add(x.right);
        }
    }

    /**
     * Retrieves the X coordinate of the root node
     * @return - X coordinate of the root node
     */
    public Double getRootX() {
        return root.x;
    }

    /**
     * Method to populate the KDTree with nodes from the given list, whilst also
     * ensuring the tree remains balanced
     * @param nodelist - the list of nodes that will be inserted into the KDTree
     */
    public void populate(List<Way> nodelist){
        populateKDTree(nodelist, 0);
    }

    /**
     * Helper method that recursively populates the KDTree. It populates it by keep finding
     * the median of the nodes. Ensuring the tree keeps being balanced.
     * @param nodelist - the list of the nodes that will be inserted into the KDTree
     * @param depth - the current depth of the tree
     */
    // Helper method which makes recursive calls to itself.
    private void populateKDTree(List<Way> nodelist, int depth) {
        if (nodelist.isEmpty()) return;  // Ensure no operations on an empty list

        int axis = depth % 2;
        // if the axis is 0, compare the x value long else compare lat
        if(axis == 0){
            // Sort based on the x-axis
            nodelist.sort(Comparator.comparingDouble(n -> n.getCenterLon()));

        } else {
            // Sort based on the y-axis
            nodelist.sort(Comparator.comparingDouble(n -> n.getCenterLat()));
        }

        // Find median index
        int medianIndex = nodelist.size() / 2;
        Way medianWay = nodelist.get(medianIndex);

        // Insert median nodes into KDTree
        put(medianWay.getCenterLon()*0.56, -medianWay.getCenterLat(), medianWay);

        // iterate depth to ensure that a new axis is sorted for
        depth++;

        // Recursive population call
        // Left Recursive Call, Handles the elements before the median index by only providing from the start of the list to one less than the median index
        // The subList does not include the end of the range in the list is provides
        if (medianIndex > 0)  populateKDTree(nodelist.subList(0, medianIndex), depth);
        // Right Recursive Call, Handles the elements after the median index by only providing from the median index+1 (avoids duplicates) to the end of the list
        if (medianIndex + 1 < nodelist.size()) populateKDTree(nodelist.subList(medianIndex+1, nodelist.size()), depth);
    }

    /**
     * Method to perform a range search on the KDTree, so that it only returns nodes within this range
     * @param xMin - the minimum X coordinate of the range
     * @param xMax - the maximum X coordinate of the range
     * @param yMin - the minimum Y coordinate of the range
     * @param yMax - the maximum Y coordinate of the range
     * @return - a queue of the nodes within the specified range
     */
    public Queue<Way> rangeSearch(double xMin, double xMax, double yMin, double yMax)
    {
//        System.out.println("Range Searching for these param: xMin:"+xMin+" xMax:"+xMax+" yMin:"+yMin+" yMax:"+yMax);
        Queue<Way> queue = new LinkedList<>();
        rangeSearch(root, queue, xMin, xMax, yMin, yMax, 0);
        return queue;
    }

    /**
     * Helper method that recursively finds node within the given bounds, and adds the nodes found to a queue
     * @param x - the current node which is being examined
     * @param queue - the queue that the nodes found will be added to
     * @param xMin - the minimum X coordinate of the range
     * @param xMax - the maximum X coordinate of the range
     * @param yMin - the minimum Y coordinate of the range
     * @param yMax - the maximum Y coordinate of the range
     * @param depth - the current depth of the recursion step in the KDTree
     */
    private void rangeSearch(KDNode x, Queue<Way> queue, double xMin, double xMax, double yMin, double yMax, int depth)
    {
        if (x == null) return;

        int axis = depth % 2;
        double cmplo; //CompareToLow
        double cmphi; //CompareToHigh
        // if the axis is 0, compare the x value long else comp     are lat
        if(axis == 0){
            // Sort based on the x-axis
            cmplo = Double.compare(xMin,x.x);
            cmphi = Double.compare(xMax,x.x);

        } else {
            // Sort based on the y-axis
            cmplo = Double.compare(yMin,x.y);
            cmphi = Double.compare(yMax,x.y);
        }
        if (cmplo < 0) rangeSearch(x.left, queue, xMin, xMax, yMin, yMax, depth+1);
        if (cmplo <= 0 && cmphi >= 0) queue.add(x.val);
        if (cmphi > 0) rangeSearch(x.right, queue, xMin, xMax, yMin, yMax, depth+1);
    }

    /**
     * Method to find the nearest neighbor for a given node given in x and y coordinates. It uses the {@link #ceiling(Double, Double, boolean)}
     * and the {@link #floor(double, double, boolean)} methods to find their respective best candidates and then compares the two to find the best overall nearest node.
     * Also comes with the option to find the nearest neighbor in general, or the nearest one that is on a road specifically.
     * @param xCoord - the X coordinate of the given node
     * @param yCoord - the Y coordinate of the given node
     * @param shouldFindNearestRoad - true = find nearest node on a road, false = nearest node in general
     * @return - the nearest node either on a road or in general
     */
    public Way getNearestNeighbor(Double xCoord, Double yCoord, boolean shouldFindNearestRoad){
        Way tmpWay1 = floor(xCoord*0.56, -yCoord, shouldFindNearestRoad);
        Way tmpWay2 = ceiling(xCoord*0.56, -yCoord, shouldFindNearestRoad);

        if (tmpWay1 == null && tmpWay2 == null) return null; // Return null if both floor and ceiling are null
        if (tmpWay1 == null) return tmpWay2; // Return ceiling if floor is null
        if (tmpWay2 == null) return tmpWay1; // Return floor if ceiling is null

        double distanceBetweenN1andInput = Math.hypot(tmpWay1.getCenterLon()-xCoord, tmpWay1.getCenterLat()-yCoord);
        double distanceBetweenN2andInput = Math.hypot(tmpWay2.getCenterLon() - xCoord, tmpWay2.getCenterLat() - yCoord);

        if(distanceBetweenN1andInput < distanceBetweenN2andInput){
            return tmpWay1;
        } else {
            return tmpWay2;
        }
    }

    /**
     * Returns the smallest key stored in the KDTree that is greater than or equal to the given coordinates
     * @param xCoord - the X coordinate of the given node
     * @param yCoord - the Y coordinate of the given node
     * @param findNearestRoad -  true = find nearest node greater than or equal to the given coordinates that is on a road, false = nearest node in general
     * @return - the smallest node greater than or equal to the given coordinates, either one that is on a road or just in general
     */
    public Way ceiling(Double xCoord, Double yCoord, boolean findNearestRoad) {
        if (xCoord == null || yCoord == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) return null; // "calls ceiling() with empty symbol table"
        KDNode x = ceiling(root, xCoord, yCoord, 0, findNearestRoad, null);
        if (x == null) return  null; // "argument to ceiling() is too large"
        else return x.val;
    }

    /**
     * Helper method that recursively searches for the smallest node in the KDTree greater than or equal to the given coordinates.
     * It also considers the depth of the recursion to determine which axis it should do the comparsion (0 for x-axis and 1 for y-axis).
     * The boolean shouldFindNearestRoad, determines whether the method should account for nodes only on roads, or just find nodes in general
     * @param x - current node that is examined
     * @param xCoord - the X coordinate of the given node
     * @param yCoord - the Y coordinate of the given node
     * @param depth - the current depth of the recursion step in the KDTree
     * @param shouldFindNearestRoad - boolean indication whether to find nodes on roads or not
     * @param best - best candidate found so far
     * @return - the smallest node greater than or equal to the given coordinates, either on a road or not depending on boolean
     */
    private KDNode ceiling(KDNode x, Double xCoord, Double yCoord, int depth, boolean shouldFindNearestRoad, KDNode best) {
        if (x == null) return null;
        int cmp;
        // Determine the axis of comparison based on current depth (0 for x, 1 for y)
        int axis = depth % 2;
        // The following two conditionals is used to determine whether to divide half-plane vertically or horizontally
        if (axis == 0) cmp = Double.compare(xCoord, x.x); // Compare x-coordinates if axis is 0
        else cmp = Double.compare(yCoord, x.y); // Compare y-coordinates if axis is 1

        // Normal ceiling
        if (!shouldFindNearestRoad){
            if (cmp < 0) return ceiling(x.left, xCoord, yCoord, depth + 1, shouldFindNearestRoad, best);
            else if (cmp > 0) return ceiling(x.right, xCoord, yCoord, depth + 1, shouldFindNearestRoad, x);
            else return x;
        }
        // If we want to find the nearest node that is also a road
        else {
            if (cmp < 0) {
                if (x.val.isRoad()) best = x;
                return ceiling(x.left, xCoord, yCoord, depth + 1, shouldFindNearestRoad, best);
            }
            else if (cmp > 0) {
                if (x.val.isRoad()) best = x;
                return ceiling(x.right, xCoord, yCoord, depth + 1, shouldFindNearestRoad, best);
            }
            else {
                if (x.val.isRoad()) return x;
                else return best;
            }
        }
    }

    /**
     * Returns the largest key stored in the KDTree that is less than or equal to the given coordinates
     * @param xCoord - the X coordinate of the given node
     * @param yCoord - the Y coordinate of the given node
     * @param shouldFindNearestRoad -  true = find nearest node less than or equal to the given coordinates that is on a road, false = nearest node in general
     * @return - the largest node less than or equal to the given coordinates, either one that is on a road or just in general
     */
    public Way floor(double xCoord, double yCoord, boolean shouldFindNearestRoad) {
        KDNode x = floor(root, xCoord, yCoord, 0, shouldFindNearestRoad, null);
        if (x == null) return null; //"argument to floor() is too small"
        else return x.val;

    }

    /**
     *
     * Helper method that recursively searches for the largest node in the KDTree less than or equal to the given coordinates.
     * It also considers the depth of the recursion to determine which axis it should do the comparison (0 for x-axis and 1 for y-axis).
     * The boolean shouldFindNearestRoad, determines whether the method should account for nodes only on roads, or just find nearest nodes in general
     * @param x - current node that is examined
     * @param xCoord - the X coordinate of the given node
     * @param yCoord - the Y coordinate of the given node
     * @param depth - the current depth of the recursion step in the KDTree
     * @param shouldFindNearestRoad - boolean indication whether to find nodes on roads or not
     * @param best - best candidate found so far
     * @return - the largest node less than or equal to the given coordinates, either on a road or not depending on boolean
     */
    private KDNode floor(KDNode x, double xCoord, double yCoord, int depth, boolean shouldFindNearestRoad, KDNode best) {
        if (x == null) return best;
        int cmp;
        // Determine the axis of comparison based on current depth (0 for x, 1 for y)
        int axis = depth % 2;
        // The following two conditionals is used to determine whether to divide half-plane vertically or horizontally
        if (axis == 0) cmp = Double.compare(xCoord, x.x); // Compare x-coordinates if axis is 0
        else cmp = Double.compare(yCoord, x.y); // Compare y-coordinates if axis is 1

        if (!shouldFindNearestRoad) {
            if (cmp  < 0) return floor(x.left, xCoord, yCoord,depth + 1, shouldFindNearestRoad, best);
            else if (cmp  > 0) return floor(x.right, xCoord, yCoord,depth + 1, shouldFindNearestRoad, x);
            else return x;

        }
        // If we want to find the nearest node that is also a road
        else {
            if (cmp < 0) {
                if (x.val.isRoad()) best = x;
                return floor(x.left, xCoord, yCoord, depth + 1, shouldFindNearestRoad, best);
            }
            else if (cmp > 0) {
                if (x.val.isRoad()) best = x;
                return floor(x.right, xCoord, yCoord, depth + 1, shouldFindNearestRoad, best);
            }
            else {
                if (x.val.isRoad()) return x;
                else return best;
            }
        }
    }

    /**
     * Returns all nodes in the KDTree as an ArrayList.
     * @return ArrayList containing all ways in the KDTree
     */
    public List<Way> getAllWays() {
        List<Way> nodeList = new ArrayList<>();
        // Start recursive traversal from the root node
        getAllWays(root, nodeList);
        return nodeList;
    }

    /**
     * Helper method to recursively traverse the KDTree and add nodes to the ArrayList.
     * @param x - current kdnode being traversed
     * @param nodeList - ArrayList to store nodes
     */
    private void getAllWays(KDNode x, List<Way> nodeList) {
        if (x == null) return;
        // Recursively traverse left subtree
        getAllWays(x.left, nodeList);
        // Add current node to the ArrayList
        nodeList.add(x.val);
        // Recursively traverse right subtree
        getAllWays(x.right, nodeList);
    }

}
