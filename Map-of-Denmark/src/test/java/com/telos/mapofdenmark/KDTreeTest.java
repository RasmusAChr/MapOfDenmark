package com.telos.mapofdenmark;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.telos.mapofdenmark.KDTreeClasses.KDTree;
import java.util.List;
import java.util.ArrayList;
class KDTreeTest {

    private KDTree tree;

    @BeforeEach
    void setUp() {
        tree = new KDTree();
    }

    @Test
    void testGetPut() {
        double[] point = new double[] {0,0};
        String value = "First Point";
        tree.put(point[0], point[1],new Node(1,1));
        assertEquals(value, tree.get(0,0), "get should return the correct value for point1");
    }
    @Test
    void testGetNonExistentPoint() {
        double[] point = new double[] {1,0};
        String value = "First Point";
        tree.put(point[0], point[1],new Node(1,1));
        assertNull(tree.get(0,0), "get should return null for a non-existent point");
    }

    @Test
    void testPopulationSize(){
        List<Node> listOfNodes = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            listOfNodes.add(new Node(i,i));
        }
        tree.populate(listOfNodes);
        assertEquals(10,tree.size());
    }

    @Test
    void testBalancingOnRoot(){
        List<Node> listOfNodes = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            listOfNodes.add(new Node(i,i));
        }
        tree.populate(listOfNodes);
        assertEquals(5, tree.getRootX());
    }
    @Test
    void testBalancingOnTree(){
        List<Node> listOfNodes = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            listOfNodes.add(new Node(i,i));
        }
        tree.populate(listOfNodes);
        tree.levelOrderTraverse();
    }
    @Test
    void testRangeSearch(){
        List<Node> listOfNodes = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            listOfNodes.add(new Node(i,i));
        }
        tree.populate(listOfNodes);
        assertEquals(3, tree.rangeSearch(0,2,0,2).size());
    }
}

