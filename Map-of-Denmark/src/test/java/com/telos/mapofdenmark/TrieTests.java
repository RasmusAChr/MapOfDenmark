package com.telos.mapofdenmark;

import static org.junit.jupiter.api.Assertions.*;

import com.telos.mapofdenmark.TrieClasses.Trie;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TrieTests {
    private Trie trie;
    private List<String> suggestionList;
    @BeforeEach
    void setUp(){
        trie = new Trie();
        suggestionList = new ArrayList<>();
        // Loading of desired city names
        try{
            String path = System.getProperty("user.dir"); // gets which directory the project is placed
            String filename = path+"\\data\\citynames.txt";
            File cityNames = new File(filename);
            Scanner scanner = new Scanner(cityNames);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    void testInsertAndSuggestSingleCity() {
        // Test inserting a single city and retrieving it
        trie.insert("Åkirkeby");
        suggestionList = trie.getAddressSuggestions("å",1);
        assertEquals(suggestionList.get(0), "åkirkeby");
    }

    @Test
    void testCaseInsensitivity() {
        // Test that suggestions work regardless of case
        trie.insert("Lemmino");
        suggestionList = trie.getAddressSuggestions("l",1);
        assertEquals(suggestionList.get(0), "lemmino");
    }
    @Test
    void testSuggestionsForMultipleCities() {
        // Test inserting multiple cities and retrieving suggestions for a prefix
    }
    @Test
    void testEmptyTrie() {
        // Check querying an empty Trie
    }
}
