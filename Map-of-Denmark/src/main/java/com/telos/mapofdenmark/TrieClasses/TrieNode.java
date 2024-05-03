package com.telos.mapofdenmark.TrieClasses;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// This is the node structure for the trie data structure

/**
 * The class "TrieNode" defines the structure of the nodes contained inside the Trie.
 * It contains a map to store all child nodes of a character, and also contains a Boolean called endOfWord that indicates if the node is the end of a word or not.
 */
public class TrieNode implements Serializable {
    final static long serialVersionUID = 88214124998L;
    Map<Character, TrieNode> children; // This map stores all the child nodes. We map characters to corresponding child nodes
    boolean endOfWord; // boolean to indicate whether this node is at the end of a word or not

    /**
     * Default constructor for non-root nodes.
     */
    public TrieNode() {
        children = new HashMap<>();
        endOfWord = false;
    }

    /**
     * Constructor with initial capacity, mainly used for the root node.
     * @param initialCapacity The initial capacity for the hashmap, based on the estimated size of unique children.
     */
    public TrieNode(int initialCapacity) {
        this.children = new HashMap<>(initialCapacity);
        this.endOfWord = false;
    }
}
