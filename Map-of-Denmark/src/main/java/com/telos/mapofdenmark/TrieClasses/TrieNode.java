package com.telos.mapofdenmark.TrieClasses;

import java.util.HashMap;
import java.util.Map;

// This is the node structure for the trie data structure

/**
 * The class "TrieNode" defines the structure of the nodes contained inside the Trie.
 * It contains a map to store all child nodes of a character, and also contains a Boolean called endOfWord that indicates if the node is the end of a word or not.
 */
public class TrieNode {
    Map<Character, TrieNode> children; // This map stores all the child nodes. We map characters to corresponding child nodes
    Boolean endOfWord; // Boolean to indicate whether this node is at the end of a word or not

    TrieNode(){
        children = new HashMap<>(); // We initialize the children map but with no entries yet
        endOfWord = false; // At first there will not be any nodes that represent the end of a word
    }
}
