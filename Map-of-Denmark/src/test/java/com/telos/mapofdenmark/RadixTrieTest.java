package com.telos.mapofdenmark;

import static org.junit.jupiter.api.Assertions.*;

import com.telos.mapofdenmark.TrieClasses.RadixTrie;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;


public class RadixTrieTest {
    private RadixTrie trie;

    @BeforeEach
    public void setUp() {
        trie = new RadixTrie();
    }

    @Test
    public void testInsert() {
        trie.insert("Strandvejen");
        trie.insert("Strandvejen");
        trie.insert("Strandvejen");
        trie.insert("Østergade");
        trie.insert("Vestergade");
    }

    @Test
    public void testGetAddressSuggestions_ExactMatch() {
        testInsert();
        String input = "Strandvejen 20 Svaneke Bornholm Denmark";
        List<String> suggestions = trie.getAddressSuggestions(input.toLowerCase(), 10);
        assertEquals(1, suggestions.size());
        assertEquals("Strandvejen 20 Svaneke Bornholm Denmark", suggestions.get(0));
    }

    @Test
    public void testGetAddressSuggestions_Prefix() {
        testInsert();
        List<String> suggestions = trie.getAddressSuggestions("Strandvejen", 10);
        assertEquals(3, suggestions.size());
        assertTrue(suggestions.containsAll(Arrays.asList(
                "Strandvejen 20 Svaneke Bornholm DENMARK",
                "Strandvejen 21 Svaneke Bornholm DENMARK",
                "Strandvejen 22 Svaneke Bornholm DENMARK"
        )));
    }

    @Test
    public void testGetAddressSuggestions_LimitSuggestions() {
        testInsert();
        List<String> suggestions = trie.getAddressSuggestions("Strand", 2);
        assertEquals(2, suggestions.size());
        assertTrue(suggestions.containsAll(Arrays.asList(
                "Strandvejen 20 Svaneke Bornholm DENMARK",
                "Strandvejen 21 Svaneke Bornholm DENMARK"
        )));
    }

    @Test
    public void testGetAddressSuggestions_NoMatch() {
        testInsert();
        List<String> suggestions = trie.getAddressSuggestions("NonExistentAddress", 10);
        assertTrue(suggestions.isEmpty());
    }

    @Test
    public void testPrintAllAddresses() {
        RadixTrie trie = new RadixTrie();

        // Insert addresses into the trie
        trie.insert("Strandvejen 20 Svaneke Bornholm Denmark");
        trie.insert("Strandvejen 21 Svaneke Bornholm Denmark");
        trie.insert("Strandvejen 22 Svaneke Bornholm Denmark");
        trie.insert("Østergade 10 København København Denmark");
        trie.insert("Vestergade 5 Aarhus Aarhus Denmark");

        // Retrieve all addresses
        List<String> allAddresses = trie.getAllAddresses();

        // Print out all addresses
        System.out.println("All Addresses:");
        for (String address : allAddresses) {
            System.out.println(address);
        }

        // Ensure at least one address is printed
        assertTrue(allAddresses.size() > 0, "At least one address should be printed");
    }
}
