package com.telos.mapofdenmark.KDTreeClasses;

// Inspiration to KDTree from https://www.geeksforgeeks.org/search-and-insertion-in-k-dimensional-tree/
// We also utilized code from chapter 3 in Algorithms 4 by Wayne and Sedgewick ch 3.2
public class KDTree<Key extends Comparable<Key>, Value>
{
    private Node root; // root of BST
    private class Node {
        private Key key; // Primitive key is no longer needed in KDTree as coordinates will be used as key instead.
        double x, y; // Coordinates of the point which will be used for the line that will be drawn to create half planes
        private Value val;
        private Node left, right; // links to the left and right subtrees
        private int size; // number of nodes in the subtree rooted here
        public Node(double x, double y, Value val, int size)
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
    private int size(Node x) {
        // Helper method to calculate size of a subtree rooted at a given node
        if (x == null) return 0;
        else return x.size;
    }
    public boolean isEmpty() {
        // Checks if the BST is empty
        return size() == 0;
    }
    public void put(double x, double y, Value val) {
        // Public method to insert a key-value pair; updates if key exists
        // Put method arguments change to acommodate nodes new attributes
        root = put(root, x, y, val, 0);
    }
    private Node put(Node x, double xCoord, double yCoord, Value val, int depth) {
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

        // New put adapted to KDTree
        if (x == null) return new Node(xCoord, yCoord, val, 1);
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
    public Value get(double x, double y){
        return get(x, y, 0);
    }
    private Value get(double xCoord, double yCoord, int depth) {
        // Retrieves the value associated with a given key
        Node x = root;
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
    public Key min() {
        // Finds the smallest key in the BST
        return min(root).key;
    }
    private Node min(Node x) {
        // Recursive method to find the node with the smallest key
        if (x.left == null) return x;
        return min(x.left);
    }
    private Node deleteMin(Node x) {
        // Removes the node with the smallest key in a subtree
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        //x.N = size(x.left) + size(x.right) + 1; // Changes the overall size of the BST
        return x;
    }
    public Key max() {
        // Finds the greatest key in the BST
        return max(root).key;
    }
    public Node max(Node x) {
        // Recursive method to find the node with the greatest key
        if (x.right == null) return x;
        return max(x.right);
    }
    public void delete(Key key) {
        // Deletes a node with a given key from the BST
        root = delete(root, key);
    }
    private Node delete(Node x, Key key) {
        // Recursive method to delete a node with a given key
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else
        {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            Node t = x;
            x = min(t.right); // See page 407.
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.size = size(x.left) + size(x.right) + 1; // Changes the overall size of the BST
        return x;
    }

    private void traverse(Node x) {
        // Traverses the BST in order
        if (x == null) return;
        traverse(x.left);
        //StdOut.println(x.key);
        traverse(x.right);
    }
}
