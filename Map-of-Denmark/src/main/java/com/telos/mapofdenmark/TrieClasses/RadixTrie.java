package com.telos.mapofdenmark.TrieClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RadixTrie {
    RadixNode rootNode;

    /**
     * Constructor for the Trie class.
     */
    public RadixTrie(){
//        rootNode = new TrieNode(); // At initialization the trie will start with an empty root node
        rootNode = new RadixNode(29); // Create a root node with a specified initial capacity
    }

    /**
     * Inserts a word into the Patricia Trie.
     * @param inputWord The word to be inserted into the trie.
     */
    public void insert(String inputWord) {
        if (inputWord == null || inputWord.isEmpty()) return; // Do not process null or empty strings
        String input = inputWord.toLowerCase(); // Convert the word to lowercase to ensure the trie is case-insensitive
        insert(rootNode, input, 0);
    }

    /**
     * Helper method to insert a word into the Patricia Trie recursively.
     * @param node The current node in the trie.
     * @param word The word to be inserted.
     * @param index The index at which the substring starts.
     * @return The updated node after insertion.
     */
    private RadixNode insert(RadixNode node, String word, int index) {
        if (index == word.length()) {
            node.endOfWord = true; // Mark the end of the word
            return node;
        }

        char firstChar = word.charAt(index);
        RadixNode child = node.children.get(firstChar);

        if (child == null) {
            child = new RadixNode(word.substring(index));
            child.parent = node;
            node.children.put(firstChar, child);
            child.endOfWord = true;
            return child;
        } else {
            // Find common prefix length
            String commonPrefix = findCommonPrefix(child.value, substring);
            if (commonPrefix.length() < child.value.length()) {
                // Split the current node
                RadixNode newChild = new RadixNode(child.value.substring(commonPrefix.length()));
                newChild.children.putAll(child.children);
                newChild.endOfWord = child.endOfWord;

                // Reset current child node
                child.value = commonPrefix;
                child.children.clear();
                child.children.put(newChild.value.charAt(0), newChild);
                child.endOfWord = false;
            }

            if (commonPrefix.length() < substring.length()) {
                // Insert remaining part
                return insert(child, substring.substring(commonPrefix.length()), 0);
            } else {
                child.endOfWord = true;
                return child;
            }
        }
    }

    private String findCommonPrefix(String str1, String str2) {
        int minLength = Math.min(str1.length(), str2.length());
        for (int i = 0; i < minLength; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                return str1.substring(0, i);
            }
        }
        return str1.substring(0, minLength);
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
