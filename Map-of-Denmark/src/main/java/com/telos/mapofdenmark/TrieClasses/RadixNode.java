package com.telos.mapofdenmark.TrieClasses;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The class "RadixNode" defines the structure of the nodes contained inside the RadixTrie.
 * It contains a map to store all child nodes of a character, and also contains a Boolean called endOfWord that indicates if the node is the end of a word or not.
 */
public class RadixNode implements Serializable {
    final static long serialVersionUID = 88214124998L;
    Map<Character, RadixNode> children; // Map to store child nodes, where the key is a sequence of characters
    boolean endOfWord; // boolean to indicate whether this node is at the end of a word or not
    String value; // The sequence of characters stored in this node

    /**
     * Default constructor for non-root nodes.
     */
    public RadixNode(String value, boolean endOfWord) {
        this.children = new HashMap<>();
        this.endOfWord = endOfWord;
        this.value = value;
    }

    /**
     * Constructor with initial capacity, mainly used for the root node.
     * @param initialCapacity The initial capacity for the hashmap, based on the estimated size of unique children.
     */
    public RadixNode(int initialCapacity) {
        this.children = new HashMap<>(initialCapacity);
        this.endOfWord = false;
        this.value = "";
    }
}