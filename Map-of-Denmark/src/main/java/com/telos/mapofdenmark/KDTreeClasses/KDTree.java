package com.telos.mapofdenmark.KDTreeClasses;

import com.telos.mapofdenmark.Node;
import com.telos.mapofdenmark.Way;

import java.util.*;

// Inspiration to KDTree from https://www.geeksforgeeks.org/search-and-insertion-in-k-dimensional-tree/
// We also utilized code from chapter 3 in Algorithms 4 by Wayne and Sedgewick ch 3.2
public class KDTree
{
    private KDNode root; // root of BST
    private class KDNode {
        //        private Key key; // Primitive key is no longer needed in KDTree as coordinates will be used as key instead.
        double x, y; // Coordinates of the point which will be used for the line that will be drawn to create half planes
        private Node val;
        private KDNode left, right; // links to the left and right subtrees
        private int size; // number of nodes in the subtree rooted here
        public KDNode(double x, double y, Node val, int size)
        {
            // Constructor has been changed from a normal BST to accomodate coordinates
            //this.key = key;
            this.x = x;
            this.y = y;
            this.val = val;
            this.size = size;
        }
    }
    public int size() {
        // Returns the number of nodes in the BST
        return size(root);
    }
    private int size(KDNode x) {
        // Helper method to calculate size of a subtree rooted at a given node
        if (x == null) return 0;
        else return x.size;
    }
    public boolean isEmpty() {
        // Checks if the BST is empty
        return size() == 0;
    }
    public void put(double x, double y, Node val) {
        // Public method to insert a key-value pair; updates if key exists
        // Put method arguments change to acommodate nodes new attributes
        root = put(root, x, y, val, 0);
//        System.out.println("lon: " + (x*-0.56) + " lat: " + y);
    }
    private KDNode put(KDNode x, double xCoord, double yCoord, Node val, int depth) {
        // Recursively inserts a key-value pair into the BST
        // Change key’s value to val if key in subtree rooted at x.
        // Otherwise, add new node to subtree associating key with val.
//        if (x == null) return new Node(key, val, 1);
//        int cmp = key.compareTo(x.key);
//        if (cmp < 0) x.left = put(x.left, key, val);
//        else if (cmp > 0) x.right = put(x.right, key, val);
//        else x.val = val;
//        x.size = size(x.left) + size(x.right) + 1;
//        return x;

        //System.out.println("Put this node with a ref to this way: " + val.getWay());

        // New put adapted to KDTree
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
    public Node get(double x, double y){
        return get(x, y, 0);
    }
    private Node get(double xCoord, double yCoord, int depth) {
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
                    //depth++; depth should not be called from conditional as an asymmetric depth could happen
                    x = x.left;
                }
                else if (cmp > 0) {
                    //depth++;
                    x = x.right;
                }
                else return x.val;
            } // Horizontal line
            else {
                int cmp = Double.compare(xCoord, x.y);
                if (cmp < 0){
                    //depth++;
                    x = x.left;
                }
                else if (cmp > 0) {
                    //depth++;
                    x = x.right;
                }
                else return x.val;
            }
        }
        xDepth++; // Increment depth at each step to alternate comparison axis
        return null;
    }

    private KDNode deleteMin(KDNode x) {
        // Removes the node with the smallest key in a subtree
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        //x.N = size(x.left) + size(x.right) + 1; // Changes the overall size of the BST
        return x;
    }

    // Not being used atm. and has to be reworked
    /*public void delete(Double key) {
        // Deletes a node with a given key from the BST
        root = delete(root, key);
    }*/


    /*private KDNode delete(KDNode x, Double key) {
        // Recursive method to delete a node with a given key
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else
        {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            KDNode t = x;
            x = min(t.right); // See page 407.
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.size = size(x.left) + size(x.right) + 1; // Changes the overall size of the BST
        return x;
    }*/

    public void inOrderTraverse(){
        inOrderTraverse(root);
    }
    private void inOrderTraverse(KDNode x) {
        // Traverses the BST in order
        if (x == null) return;
        inOrderTraverse(x.left);
        System.out.println(x.x + " " + x.y);
        //StdOut.println(x.key);
        inOrderTraverse(x.right);
    }

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


    public Double getRootX() {
        return root.x;
        //return root;
    }

    // Method to populate a kdtree and making it balanced with data from an array
    public void populate(List<Node> nodelist){
        populateKDTree(nodelist, 0);
    }

    // Helper method which makes recursive calls to itself.
    private void populateKDTree(List<Node> nodelist, int depth) {
        if (nodelist.isEmpty()) return;  // Ensure no operations on an empty list

        int axis = depth % 2;
        // if the axis is 0, compare the x value long else compare lat
        if(axis == 0){
            // Sort based on the x-axis
            nodelist.sort(Comparator.comparingDouble(n -> n.lon));

        } else {
            // Sort based on the y-axis
            nodelist.sort(Comparator.comparingDouble(n -> n.lat));
        }

        // Find median index
        int medianIndex = nodelist.size() / 2;
        Node medianNode = nodelist.get(medianIndex);

        // Insert median nodes into KDTree
        put(medianNode.getLon()*0.56, -medianNode.getLat(), medianNode);

        // iterate depth to ensure that a new axis is sorted for
        depth++;

        // Recursive population call
        // Left Recursive Call, Handles the elements before the median index by only providing from the start of the list to one less than the median index
        // The subList does not include the end of the range in the list is provides
        if (medianIndex > 0)  populateKDTree(nodelist.subList(0, medianIndex), depth);
        // Right Recursive Call, Handles the elements after the median index by only providing from the median index+1 (avoids duplicates) to the end of the list
        if (medianIndex + 1 < nodelist.size()) populateKDTree(nodelist.subList(medianIndex+1, nodelist.size()), depth);
    }

    public Queue<Node> rangeSearch(double xMin, double xMax, double yMin, double yMax)
    {
//        System.out.println("Range Searching for these param: xMin:"+xMin+" xMax:"+xMax+" yMin:"+yMin+" yMax:"+yMax);
        Queue<Node> queue = new LinkedList<>();
        rangeSearch(root, queue, xMin, xMax, yMin, yMax, 0);
        return queue;
    }

    private void rangeSearch(KDNode x, Queue<Node> queue, double xMin, double xMax, double yMin, double yMax, int depth)
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

    public Node getNearestNeighbor(Double xCoord, Double yCoord, boolean shouldFindNearestRoad){
        Node tmpNode1 = floor(xCoord, yCoord, shouldFindNearestRoad);
        Node tmpNode2 = ceiling(xCoord, yCoord, shouldFindNearestRoad);
        double distanceBetweenN1andInput = Math.hypot(tmpNode1.getLon()-xCoord, tmpNode1.getLat()-yCoord);
        double distanceBetweenN2andInput = Math.hypot(tmpNode2.getLon() - xCoord, tmpNode2.getLat() - yCoord);
        if(distanceBetweenN1andInput < distanceBetweenN2andInput){
            return tmpNode1;
        } else {
            return tmpNode2;
        }
    }

    /**
     * Returns the smallest key in the symbol table greater than or equal to {@code key}.
     *
     * @param  key the key
     * @return the smallest key in the symbol table greater than or equal to {@code key}
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Node ceiling(Double xCoord, Double yCoord, boolean findNearestRoad) {
        if (xCoord == null || yCoord == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");
        KDNode x = ceiling(root, xCoord, yCoord, 0, findNearestRoad, null);
        if (x == null) throw new NoSuchElementException("argument to ceiling() is too large");
        else return x.val;
    }

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
                if (x.val.getWay().isPartOfRoad()) best = x;
                return ceiling(x.left, xCoord, yCoord, depth + 1, shouldFindNearestRoad, best);
            }
            else if (cmp > 0) {
                if (x.val.getWay().isPartOfRoad()) best = x;
                return ceiling(x.right, xCoord, yCoord, depth + 1, shouldFindNearestRoad, best);
            }
            else {
                if (x.val.getWay().isPartOfRoad()) return x;
                else return best;
            }
        }
    }

    public Node floor(double xCoord, double yCoord, boolean shouldFindNearestRoad) {
        KDNode x = floor(root, xCoord, yCoord, 0, shouldFindNearestRoad, null);
        if (x == null) throw new NoSuchElementException("argument to floor() is too small");
        else return x.val;

    }

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
            else if (cmp  > 0) return floor(x.right, xCoord, yCoord,depth + 1, shouldFindNearestRoad, best);
            else return x;
        }
        // If we want to find the nearest node that is also a road
        else {
            if (cmp < 0) {
                if (x.val.getWay().isPartOfRoad()) best = x;
                return floor(x.left, xCoord, yCoord, depth + 1, shouldFindNearestRoad, best);
            }
            else if (cmp > 0) {
                if (x.val.getWay().isPartOfRoad()) best = x;
                return floor(x.right, xCoord, yCoord, depth + 1, shouldFindNearestRoad, best);
            }
            else {
                if (x.val.getWay().isPartOfRoad()) return x;
                else return best;
            }
        }
    }

    // range serach methods that returns a set instead of a queue to ensure that the same way is not drawn multiple times
    public Set<Way> rangeSearchSet(double xMin, double xMax, double yMin, double yMax)
    {
//        System.out.println("Range Searching for these param: xMin:"+xMin+" xMax:"+xMax+" yMin:"+yMin+" yMax:"+yMax);
        Set<Way> waySet = new HashSet<>();
        rangeSearchSet(root, waySet, xMin, xMax, yMin, yMax, 0);
        return waySet;
    }

    private void rangeSearchSet(KDNode x, Set<Way> waySet, double xMin, double xMax, double yMin, double yMax, int depth)
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
        if (cmplo < 0) rangeSearchSet(x.left, waySet, xMin, xMax, yMin, yMax, depth+1);
        if (cmplo <= 0 && cmphi >= 0) waySet.add(x.val.getWay());
        if (cmphi > 0) rangeSearchSet(x.right, waySet, xMin, xMax, yMin, yMax, depth+1);
    }
}
