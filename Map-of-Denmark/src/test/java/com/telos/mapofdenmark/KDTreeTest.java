package com.telos.mapofdenmark;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.telos.mapofdenmark.KDTreeClasses.KDTreeWay;
import java.util.List;
import java.util.ArrayList;
class KDTreeTest {

    private KDTreeWay tree;

    @BeforeEach
    void setUp() {
        tree = new KDTreeWay();
    }

    @Test
    void testGetPut() {
        double[] point = new double[] {0,0};
        String value = "First Point";
        Way way = new Way(new ArrayList<>(), "landform", "type");
        tree.put(point[0], point[1], way);
        assertEquals(value, tree.get(0,0), "get should return the correct value for point1");
    }
    @Test
    void testGetNonExistentPoint() {
        double[] point = new double[] {1,0};
        Way way = new Way(new ArrayList<>(), "landform", "type");
        tree.put(point[0], point[1], way);
        assertNull(tree.get(0,0), "get should return null for a non-existent point");
    }

    @Test
    void testPopulationSize(){
        List<Way> listOfWays = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            listOfWays.add(new Way(new ArrayList<>(), "landform", "type"));
        }
        tree.populate(listOfWays);
        assertEquals(10,tree.size());
    }

    @Test
    void testBalancingOnRoot(){
        List<Way> listOfWays = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            listOfWays.add(new Way(new ArrayList<>(), "landform", "type"));
        }
        tree.populate(listOfWays);
        assertEquals(5, tree.getRootX());
    }
    @Test
    void testBalancingOnTree(){
        List<Way> listOfWays = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            listOfWays.add(new Way(new ArrayList<>(), "landform", "type"));
        }
        tree.populate(listOfWays);
        tree.levelOrderTraverse();
    }
    @Test
    void testRangeSearch(){
        List<Way> listOfWays = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            listOfWays.add(new Way(new ArrayList<>(), "landform", "type"));
        }
        tree.populate(listOfWays);
        assertEquals(3, tree.rangeSearch(0,2,0,2).size());
    }
}
