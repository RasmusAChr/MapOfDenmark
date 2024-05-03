package com.telos.mapofdenmark.TrieClasses;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RadixNode implements Serializable {
    final static long serialVersionUID = 88214124998L;
    Map<String, RadixNode> children; // Map to store child nodes, where the key is a sequence of characters
    boolean endOfWord; // boolean to indicate whether this node is at the end of a word or not
    String value; // The sequence of characters stored in this node
    RadixNode parent; // Parent node

    /**
     * Default constructor for non-root nodes.
     */
    public RadixNode(String value) {
        this.children = new HashMap<>();
        this.endOfWord = false;
        this.value = value;
        this.parent = null;
    }

    /**
     * Constructor with initial capacity, mainly used for the root node.
     * @param initialCapacity The initial capacity for the hashmap, based on the estimated size of unique children.
     */
    public RadixNode(int initialCapacity) {
        this.children = new HashMap<>(initialCapacity);
        this.endOfWord = false;
        this.value = "";
        this.parent = null;
    }
}