package com.telos.mapofdenmark;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.telos.mapofdenmark.KDTreeClasses.BST;

class KDTreeTest {

    private BST tree;

    @BeforeEach
    void setUp() {
        tree = new BST();
    }

    @Test
    void testGet() {
        double[] point = new double[] {0,0};
        String value = "First Point";
        tree.put(point[0], point[1],value);
        assertEquals(value, tree.get(0,0), "get should return the correct value for point1");
    }
    @Test
    void testPut(){

    }
    @Test
    void testGetNonExistentPoint() {
        double[] point = new double[] {1,0};
        String value = "First Point";
        tree.put(point[0], point[1],value);
        assertNull(tree.get(0,0), "get should return null for a non-existent point");
    }
}

