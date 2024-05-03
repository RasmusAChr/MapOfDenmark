package com.telos.mapofdenmark.TrieClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Heavily inspired from https://leetcode.com/problems/implement-trie-prefix-tree/solutions/1996974/radix-tree-with-comments/

public class RadixTrie {
    RadixNode rootNode;

    /**
     * Constructor for the Trie class.
     */
    public RadixTrie(){
        rootNode = new RadixNode(29); // Create a root node with a specified initial capacity
    }

    /**
     * Inserts a word into the Patricia Trie.
     * @param inputWord The word to be inserted into the trie.
     */
    public void insert(String inputWord) {
        if (inputWord == null || inputWord.isEmpty()) return; // Do not process null or empty strings
        String input = inputWord.toLowerCase(); // Convert the word to lowercase to ensure the trie is case-insensitive
        insert(rootNode, input);
    }

    /**
     * Helper method to insert a word into the Patricia Trie recursively.
     * @param node The current node in the trie.
     * @param word The word to be inserted.
     * @return The updated node after insertion.
     */
    private RadixNode insert(RadixNode node, String word) {
        if (node == null) return new RadixNode(word, true);

        if(node.value.equals(word)) {
            node.endOfWord = true;
            return node;
        }

        // Find the point of divergence between the node's string and the input string.
        int i = 0;
        while (i < node.value.length() && i < word.length() && node.value.charAt(i) == word.charAt(i)) {
            i++;
        }

        // If the entire node string is a prefix of the input, continue insertion in the corresponding child.
        if (i == node.value.length()) {
            char nextChar = word.charAt(i);
            node.children.putIfAbsent(nextChar, new RadixNode("", false));
            node.children.put(nextChar, insert(node.children.get(nextChar), word.substring(i)));
            return node;
        }
        
        // Split the node if only part of it is a prefix.
        RadixNode subtree = new RadixNode(node.value.substring(i), node.endOfWord);
        subtree.children.putAll(node.children);
        node.children.clear();
        node.children.put(node.value.charAt(i), subtree);

        // Handle the remaining substring of the input.
        if (i == word.length()) {
            node.endOfWord = true;
        } else {
            node.endOfWord = false;
            node.children.put(word.charAt(i), new RadixNode(word.substring(i), true));
        }

        node.value = node.value.substring(0, i);
        return node;
    }

    /**
     * Retrieves a list of all words in the trie that start with the given prefix.
     * @param prefix The prefix to search for.
     * @param limit The maximum number of suggestions to return.
     * @return A list of suggestions that start with the prefix.
     */
    public List<String> getAddressSuggestions(String prefix, int limit){
        List<String> addressSuggestions = new ArrayList<>(); // A list that stores the suggestions
        RadixNode currentNode = rootNode; // Start the traversal from the root node
        // Traverse through each character from the prefix
        for (char character: prefix.toCharArray()){
            currentNode = currentNode.children.get(character); // We move to the next node
            // Checks whether the prefix exists in the trie or not. If they don't then return an empty list
            if(currentNode == null){
                return addressSuggestions;
            }
        }
        // Collect suggestions recursively through the collectAddressSuggestions method, which starts from this current node
        collectAddressSuggestions(currentNode, prefix, addressSuggestions, limit);

        // instead of just returning the collected strings we format them before returning
        return formatAddressSuggestions(addressSuggestions);
    }

    /**
     * Helper method to collect address suggestions recursively.
     * @param node Current node in the trie.
     * @param prefix Current prefix formed by the path to this node.
     * @param addressSuggestions List to store the collected suggestions.
     * @param limit Maximum number of suggestions.
     */
    // A recursive method that collects suggestions from the given node
    private void collectAddressSuggestions(RadixNode node, String prefix, List<String> addressSuggestions, int limit){
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
        for (char charValue : node.children.keySet()){
            if (addressSuggestions.size() >= limit) {
                return; // Stop collecting if the limit has been reached
            }
            collectAddressSuggestions(node.children.get(charValue), prefix + charValue, addressSuggestions, limit);
        }
    }

    /**
     * Formats a list of address suggestions for better readability.
     * @param suggestions List of unformatted suggestions.
     * @return A list of formatted suggestions.
     */
    //Logic for formatting the output of the TRIE
    private List<String> formatAddressSuggestions(List<String> suggestions) {
        List<String> formattedSuggestions = new ArrayList<>();
        // Compile a regex pattern to extract components of the address. The pattern captures:
        // Group 1: characters for the street
        // Group 2: Digits for the house number
        // Group 3: characters for the city
        // Group 4: characters for the municipality
        // Group 5: characters for the country
        Pattern pattern = Pattern.compile("^(\\D+)(\\d+)\\s+(\\D+)\\s+(\\D+)\\s+(\\D+)$");

        for (String suggestion : suggestions) {
            // Create a matcher for the suggestion based on the compiled pattern
            Matcher matcher = pattern.matcher(suggestion);
            if (matcher.find()) {
                // Format the extracted groups and capitalize appropriate parts
                // "," could be added in front of "%s" but would create exception when running djikstra
                String formatted = String.format("%s %s %s %s %s",
                        capitalizeWord(matcher.group(1).trim()), // Street
                        matcher.group(2).trim(), // House number
                        capitalizeWord(matcher.group(3).trim()), // City
                        capitalizeWord(matcher.group(4).trim()), // Municipality
                        matcher.group(5).trim().toUpperCase()); // Country

                // Add the formatted address to the list of formatted suggestions
                formattedSuggestions.add(formatted);
            }
        }
        return formattedSuggestions;
    }

    /**
     * Helper method to capitalize the first letter of each word in a string.
     * @param input The string to capitalize.
     * @return The capitalized string.
     */
    // Helper method to capitalize the first letter of each word in a string
    // More complex than initially thought as street names can be one or more words
    private String capitalizeWord(String input) {
        // Split the input string into words based on whitespace
        String[] words = input.split("\\s+");

        // StringBuilder to build the capitalized string
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                // Append the word to the result, capitalizing the first letter and making the rest of the letters lowercase
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return result.toString().trim();
    }
}
