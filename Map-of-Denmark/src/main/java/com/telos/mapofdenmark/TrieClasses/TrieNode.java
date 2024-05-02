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
    char c;
    Boolean endOfWord; // Boolean to indicate whether this node is at the end of a word or not
    // Constructor with no char attribute, for the root node
    public TrieNode() {
        children = new HashMap<>();
        endOfWord = false;
    }
    // Constructor for subsequent child elements
    TrieNode(char c){
        this.c = c;
        children = new HashMap<>(); // We initialize the children map but with no entries yet
        endOfWord = false; // At first there will not be any nodes that represent the end of a word
    }

    public void insert(String input) {
        // If the input is empty just return
        if (input == null || input.isEmpty()) return;

        TrieNode currentNode = this;
        // We traverse through each character from the inputWord
        for (char character: input.toCharArray()){
            if(!currentNode.children.containsKey(character)){
                // If the character is not already a child of the current node, create a new TrieNode for it
                currentNode.children.put(character, new TrieNode(character));
            }
            // Move to the child node for the character, whether it was newly created or already existed
            currentNode = currentNode.children.get(character);
        }
        currentNode.endOfWord = true; // The last node of the inserted word will be marked as the character that is at the end of the word
    }
}
