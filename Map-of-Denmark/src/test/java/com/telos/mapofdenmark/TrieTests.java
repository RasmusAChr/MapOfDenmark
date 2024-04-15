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
    }
    void loadCityNames(){
        // Loading of desired city names from file
        String path = System.getProperty("user.dir"); // gets which directory the project is placed
        String filename = path+"\\data\\citynames.txt";

        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
            String line;
            while ((line = bReader.readLine()) != null) {
                trie.insert(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    void testNordicChars() {
        // Test inserting a single city and retrieving it
        trie.insert("Åkirkeby");
        suggestionList = trie.getAddressSuggestions("å",1);
        assertEquals("åkirkeby", suggestionList.get(0));
    }

    @Test
    void testCaseInsensitivity() {
        // Test that suggestions work regardless of case
        trie.insert("Lemmino");
        suggestionList = trie.getAddressSuggestions("lemmino",1);
        assertEquals("lemmino", suggestionList.get(0));
    }
    @Test
    void testSuggestionsForMultipleCities() {
        trie.insert("a");
        trie.insert("abe");
        trie.insert("Aka");
        // Test inserting multiple cities and retrieving suggestions for a prefix
        suggestionList = trie.getAddressSuggestions("a",3);
        for(String word : suggestionList){
            System.out.println(word);
        }
        //assertEquals(2, suggestionList.size());
    }
    @Test
    void testEmptyTrie() {
        // Check querying an empty Trie
    }

    @Test
    void testCorrectId(){

    }
}
