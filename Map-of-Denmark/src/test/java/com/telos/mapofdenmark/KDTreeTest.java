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
        tree.put(point[0], point[1],new Node(1,1,1));
        assertEquals(value, tree.get(0,0), "get should return the correct value for point1");
    }
    @Test
    void testGetNonExistentPoint() {
        double[] point = new double[] {1,0};
        String value = "First Point";
        tree.put(point[0], point[1],new Node(1,1,1));
        assertNull(tree.get(0,0), "get should return null for a non-existent point");
    }

    @Test
    void testPopulationSize(){
        List<Node> listOfNodes = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            listOfNodes.add(new Node(i,i,i));
        }
        tree.populate(listOfNodes);
        assertEquals(10,tree.size());
    }

    @Test
    void testBalancingOnRoot(){
        List<Node> listOfNodes = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            listOfNodes.add(new Node(i,i,i));
        }
        tree.populate(listOfNodes);
        assertEquals(5, tree.getRootX());
    }
    @Test
    void testBalancingOnTree(){
        List<Node> listOfNodes = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            listOfNodes.add(new Node(i, i,i));
        }
        tree.populate(listOfNodes);
        tree.levelOrderTraverse();
    }
    @Test
    void testRangeSearch(){
        List<Node> listOfNodes = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            listOfNodes.add(new Node(i,i,i));
        }
        tree.populate(listOfNodes);
        assertEquals(3, tree.rangeSearch(0,2,0,2).size());
    }

    // Ceiling. Smallest key in tree ≥ query key.
    @Test
    void testCeilingSearch(){
        List<Node> listOfNodes = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            listOfNodes.add(new Node(i,i,i));
        }
        tree.populate(listOfNodes);

        double xCoord = 1.12;
        double yCoord = -2.0;

        double x = tree.ceiling(xCoord,yCoord,false).getLon();
        double y = tree.ceiling(xCoord,yCoord,false).getLat();
        System.out.println("x: "+x+" y:"+y);
        assertEquals(2,x);
        assertEquals(2,y);
    }

    // Floor. Largest node in tree ≤ query key.
    @Test void testFloorSearch(){
        List<Node> listOfNodes = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            listOfNodes.add(new Node(i,i,i));
        }
        tree.populate(listOfNodes);

        double xCoord = 7.0*0.56;
        double yCoord = -7.0;
        Node temp = tree.floor(xCoord, -yCoord,false);
        double x = temp.getLon();
        double y = temp.getLat();
        System.out.println("x: "+x+" y:"+y);
        tree.levelOrderTraverse();
        assertEquals(7.0, x);
        assertEquals(7.0, y);
    }

    @Test
    void testNearestNeighbour(){
        List<Node> listOfNodes = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            listOfNodes.add(new Node(i,i,i));
        }
        listOfNodes.add(new Node(11,5.5,5.5));
        tree.populate(listOfNodes);
        Node neighborReturn = tree.getNearestNeighbor(5.3, 5.3, false);
        assertEquals("11", neighborReturn.getId());
    }
}

