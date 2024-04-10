package com.telos.mapofdenmark.KDTreeClasses;

import java.util.*;

public class BST<Key extends Comparable<Key>, Value>
{
    private Node root; // root of BST
    private class Node {
        private Key key;
        private Value val;
        private Node left, right; // links to the left and right subtrees
        private int size; // number of nodes in the subtree rooted here
        public Node(Key key, Value val, int size)
        {
            this.key = key;
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
    public void put(Key key, Value val) {
        // Public method to insert a key-value pair; updates if key exists
        root = put(root, key, val);
    }
    private Node put(Node x, Key key, Value val) {
        // Recursively inserts a key-value pair into the BST
        // Change key’s value to val if key in subtree rooted at x.
        // Otherwise, add new node to subtree associating key with val.
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else x.val = val;
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }
    public Value get(Key key) {
        // Retrieves the value associated with a given key
        Node x = root;
        while (x != null)
        {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.val;
        }
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
