package com.telos.mapofdenmark;

import static org.junit.jupiter.api.Assertions.*;

import com.telos.mapofdenmark.TrieClasses.CharTrie;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CharTrieTests {
    private CharTrie charTrie;
    private List<String> suggestionList;
    @BeforeEach
    void setUp(){
        charTrie = new CharTrie();
        suggestionList = new ArrayList<>();
    }
    void loadCityNames(){
        // Loading of desired city names from file
        String path = System.getProperty("user.dir"); // gets which directory the project is placed
        String filename = path+"\\data\\citynames.txt";

        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
            String line;
            while ((line = bReader.readLine()) != null) {
                charTrie.insert(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    void testNordicChars() {
        // Test inserting a single city and retrieving it
        charTrie.insert("Åkirkeby");
        suggestionList = charTrie.getAddressSuggestions("å",1);
        assertEquals("åkirkeby", suggestionList.get(0));
    }

    @Test
    void testCaseInsensitivity() {
        // Test that suggestions work regardless of case
        charTrie.insert("Lemmino");
        suggestionList = charTrie.getAddressSuggestions("lemmino",1);
        assertEquals("lemmino", suggestionList.get(0));
    }
    @Test
    void testSuggestionsForMultipleCities() {
        charTrie.insert("a");
        charTrie.insert("abe");
        charTrie.insert("Aka");
        // Test inserting multiple cities and retrieving suggestions for a prefix
        suggestionList = charTrie.getAddressSuggestions("a",3);
        for(String word : suggestionList){
            System.out.println(word);
        }
        //assertEquals(2, suggestionList.size());
    }
    @Test
    void testEmptyTrie() {
        // Check querying an empty Trie
    }
}
