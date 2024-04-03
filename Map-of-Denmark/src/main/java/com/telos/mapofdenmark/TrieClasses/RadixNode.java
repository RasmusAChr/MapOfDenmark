package com.telos.mapofdenmark.TrieClasses;
import java.util.HashMap;
import java.util.Map;

import java.io.Serializable;

// This is the node structure for the trie data structure

/**
 * The class "TrieNode" defines the structure of the nodes contained inside the Trie.
 * It contains a map to store all child nodes of a character, and also contains a Boolean called endOfWord that indicates if the node is the end of a word or not.
 */
public class RadixNode implements Serializable {
    Map<String, RadixNode> children; // This map stores all the child nodes. We map Strings or words to corresponding child nodes
    Boolean endOfWord; // Boolean to indicate whether this node is at the end of a word or not
    String word; // Optional: Store the word at this node. Useful for debugging or extensions.

    // Constructor with no char attribute, for the root node
    public RadixNode() {
        children = new HashMap<>();
        endOfWord = false;
    }
    // Constructor for subsequent child elements

    /*
    Not inherently necessary to have alternative constructor but good for debugging and eventual clarity
    This may be removed later for efficiency purposes. But it is good for clarity as i modeled this based on the CharTrie and inspiration from
    https://www.geeksforgeeks.org/implementing-patricia-trie-in-java/
    https://en.wikipedia.org/wiki/Radix_tree
     */
    RadixNode(String word){
        this.word = word;
        children = new HashMap<>(); // We initialize the children map but with no entries yet
        endOfWord = false; // At first there will not be any nodes that represent the end of a word
    }

    public void insert(String[] words, int index) {
        // If the input is empty or null just return
        if (words[index] == null || words[index].isEmpty()) return;

        // Get the current word from the array
        String currentWord = words[index];
        // Add a new node for the current word if it's not already present
        children.putIfAbsent(currentWord, new RadixNode(currentWord));
        // Recursively insert the next word in the sequence into the trie
        children.get(currentWord).insert(words, index + 1);

        if (index == words.length-1) {
            this.endOfWord = true; // Mark the end of a word sequence
        }
    }
}