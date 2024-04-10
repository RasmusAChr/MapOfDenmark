package com.telos.mapofdenmark.KDTreeClasses;

import java.util.*;

public class BST<Key extends Comparable<Key>, Value>
{
    private Node root; // root of BST
    private Set<Key> cancellations = new HashSet<>();
    private class Node {
        private Key key;
        private Value val;
        private Node left, right;
        private int size; // # nodes in subtree rooted here
        public Node(Key key, Value val, int size)
        {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }
    public int size() {
        return size(root);
    }
    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }
    public boolean isEmpty() {
        return size() == 0;
    }
    public void put(Key key, Value val) {
        root = put(root, key, val);
    }
    private Node put(Node x, Key key, Value val)
    {
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
    public Value get(Key key)
    {
        Node x = root;
        while (x != null)
        {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else if(!(cancellations.contains(x.key))) return x.val;
        }
        return null;
    }
    public Key min()
    {
        return min(root).key;
    }
    private Node min(Node x)
    {
        if (x.left == null) return x;
        return min(x.left);
    }
    private Node deleteMin(Node x)
    {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        //x.N = size(x.left) + size(x.right) + 1; // Changes the overall size of the BST
        return x;
    }
    public void delete(Key key)
    { root = delete(root, key); }
    private Node delete(Node x, Key key)
    {
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
        if (x == null) return;
        traverse(x.left);
        //StdOut.println(x.key);
        traverse(x.right);
    }
}
