package com.telos.mapofdenmark.TrieClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The "Trie" data structure is a prefix tree data structure, that is able to find suggestions based on a prefix.
 * It contains methods such as "Insert" which inserts words into the trie, "getAddressSuggestions" which finds the node corresponding to the prefix
 * and lastly "collectAddressSuggestions" which is a recursive method to collect suggestions starting from a given node.
 */
public class Trie implements Serializable {
    TrieNode rootNode;

    public Trie(){
        rootNode = new TrieNode(); // At initialization the trie will start with an empty root node
    }

    // Insert method inserts words into the trie
    public void insert(String inputWord) {
        if (inputWord == null || inputWord.isEmpty()) {
            return; // Early return for null or empty input to maintain data integrity
        }
        String input = inputWord.toLowerCase();
        rootNode.insert(input); // Leverage the insert method in TrieNode
    }


    public List<String> getAddressSuggestions(String prefix, int limit){
        List<String> addressSuggestions = new ArrayList<>(); // A list that stores the suggestions
        TrieNode currentNode = rootNode; // Start the traversal from the root node

        // Traverse through each character from the prefix
        for (char character: prefix.toCharArray()){
            currentNode = currentNode.children.get(character); // We move to the next node

            // Checks whether the prefix exists in the trie or not. If they don't then return an empty list
            if(currentNode == null){
                return  addressSuggestions;
            }
        }

        // Collect suggestions recursively through the collectAddressSuggestions method, which starts from this current node
        collectAddressSuggestions(currentNode, prefix, addressSuggestions, limit);
        return addressSuggestions;
    }

    // A recursive method that collects suggestions from the given node
    private void collectAddressSuggestions(TrieNode node, String prefix, List<String> addressSuggestions, int limit){
        // If the currentnode is the end of the word, that means the prefix formed so far represents a word found in the trie
        // If that is the case, we will add the word to the suggestion list
        if(node.endOfWord){
            addressSuggestions.add(prefix);
            if(addressSuggestions.size() >= limit){
                System.out.println("Limit has been reached");
                return; // If the limit has been reached, we stop collecting anymore suggestions.
            }
        }

        // We recursively travel through each child node to collect suggestions
        for (char character: node.children.keySet()){
            if (addressSuggestions.size() >= limit) {
                return; // Stop collecting if the limit has been reached
            }
            collectAddressSuggestions(node.children.get(character), prefix + character, addressSuggestions, limit);
        }
    }
}


