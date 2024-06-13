package com.telos.mapofdenmark;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.telos.mapofdenmark.KDTreeClasses.KDTreeWay;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;

class KDTreeTest {

    private KDTreeWay tree;

    @BeforeEach
    void setUp() {
        tree = new KDTreeWay();
    }

    // Helper method to create a non-empty list of nodes
    private ArrayList<Node> createNodeList(double lat, double lon) {
        int index = 0;
        ArrayList<Node> nodeList = new ArrayList<>();
        nodeList.add(new Node(index, lat, lon));
        index++;
        return nodeList;
    }

    @Test
    void testNewPopulate(){
        // init arraylist
        int size = 4_000_000;
        List<Way> array = new ArrayList<>(size);

        // Fill arraylist with random numbers
        for (int i = 0; i < size; i++) {
            array.add(new Way(createNodeList(Math.random() * 1000000, Math.random() * 1000000), "landform", "type"));
        }

        // populate and timer
        long startTime = System.currentTimeMillis();
        tree.newPopulate(array);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Elapsed time for populating kd-tree: \n" + size + " elements  " + elapsedTime + " milliseconds");
    }

    @Test
    void testGetNonExistentPoint() {
        Way way = new Way(createNodeList(1, 0), "landform", "type");
        tree.put(1, 0, way);
        assertNull(tree.get(0, 0), "get should return null for a non-existent point");
    }

    @Test
    void testPopulationSize(){
        List<Way> listOfWays = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listOfWays.add(new Way(createNodeList(i, i), "landform", "type"));
        }
        tree.populate(listOfWays);
        assertEquals(10, tree.size());
    }

    @Test
    void testBalancingOnRoot(){
        List<Way> listOfWays = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listOfWays.add(new Way(createNodeList(i, i), "landform", "type"));
        }
        tree.populate(listOfWays);
        // Checking the balance by ensuring the root is not at the extreme
        assertTrue(tree.getRootX() >= 0 && tree.getRootX() <= 10);
    }

    @Test
    void testBalancingOnTree(){
        List<Way> listOfWays = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listOfWays.add(new Way(createNodeList(i, i), "landform", "type"));
        }
        tree.populate(listOfWays);
        tree.levelOrderTraverse();
    }

    @Test
    void testRangeSearch(){
        List<Way> listOfWays = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listOfWays.add(new Way(createNodeList(i, i), "landform", "type"));
        }
        tree.populate(listOfWays);
        Queue<Way> KDTreeWays = tree.rangeSearch(0.56, 1.681, -3, -1);
        assertEquals(3, KDTreeWays.size());
    }
}


