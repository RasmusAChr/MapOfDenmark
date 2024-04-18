package com.telos.mapofdenmark;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipInputStream;
import java.util.Comparator;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import com.telos.mapofdenmark.KDTreeClasses.KDTree;
import com.telos.mapofdenmark.TrieClasses.Address;
import com.telos.mapofdenmark.TrieClasses.Trie;
import javafx.geometry.Point2D;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class Model implements Serializable {
    private static final long serialVersionUID = 9300313068198046L;

    List<Line> list = new ArrayList<Line>();
    List<Way> ways = new ArrayList<Way>();
    List<Node> nodeList = new ArrayList<>();
    private Trie trie;
    double minlat, maxlat, minlon, maxlon;
    List<Address> addressList;
    Map<String, Node> addressIdMap;
    KDTree kdTree;
    Address address;

    static Model load(String filename) throws FileNotFoundException, IOException, ClassNotFoundException, XMLStreamException, FactoryConfigurationError {
        if (filename.endsWith(".obj")) {
            try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                return (Model) in.readObject();
            }
        }
        return new Model(filename);
    }


    public Model(String filename) throws XMLStreamException, FactoryConfigurationError, IOException {
        this.addressList = new ArrayList<>();
        this.address = new Address();
        this.addressIdMap = new HashMap<>(); // Used for ref a node id to an adress
        if (filename.endsWith(".osm.zip")) {
            parseZIP(filename);
        } else if (filename.endsWith(".osm")) {
            parseOSM(filename);
        } else {
            parseTXT(filename);
        }
        save(filename+".obj");
        this.trie = deserializeTrie("data/.obj");
        this.kdTree = new KDTree();
        kdTree.populate(nodeList);
    }



    void save(String filename) throws FileNotFoundException, IOException {
        try (var out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    private void parseZIP(String filename) throws IOException, XMLStreamException, FactoryConfigurationError {
        var input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        parseOSM(input);
    }

    private void parseOSM(String filename) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        parseOSM(new FileInputStream(filename));
    }

    private void parseOSM(InputStream inputStream) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        var input = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        var id2node = new HashMap<Long, Node>();
        var way = new ArrayList<Node>();
        var coast = false;
        long addressId = 0;
        while (input.hasNext()) {
            var tagKind = input.next();
            if (tagKind == XMLStreamConstants.START_ELEMENT) {
                var name = input.getLocalName();
                if (name == "bounds") {
                    minlat = Double.parseDouble(input.getAttributeValue(null, "minlat"));
                    maxlat = Double.parseDouble(input.getAttributeValue(null, "maxlat"));
                    minlon = Double.parseDouble(input.getAttributeValue(null, "minlon"));
                    maxlon = Double.parseDouble(input.getAttributeValue(null, "maxlon"));
                } else if (name == "node") {
                    var id = Long.parseLong(input.getAttributeValue(null, "id"));
                    var lat = Double.parseDouble(input.getAttributeValue(null, "lat"));
                    var lon = Double.parseDouble(input.getAttributeValue(null, "lon"));
                    Node tempNode = new Node(lat, lon);
                    id2node.put(id, tempNode);
                    nodeList.add(tempNode);
                    addressId = id;
//                    address = new Address(); // Reset for new node
                } else if (name == "way") {
                    way.clear();
                    coast = false;
                } else if (name == "tag") {
                    var v = input.getAttributeValue(null, "v");
                    if (v.equals("coastline")) {
                        coast = true;
                    }
                    var k = input.getAttributeValue(null, "k");
                    if (k.startsWith("addr:")){
                        // Lazy initialization of address
                        if (address == null) {
                            address = new Address();
                        }
                        parseAddressFromOSM(v, k);
                    }
                } else if (name == "nd") {
                    var ref = Long.parseLong(input.getAttributeValue(null, "ref"));
                    var node = id2node.get(ref);
                    way.add(node);
                }
            } else if (tagKind == XMLStreamConstants.END_ELEMENT) {
                var name = input.getLocalName();
                // If you wish to only draw coastline -- if (name == "way" && coast) {
                if (name == "way") {
//                    ways.add(new Way(way));
                    Way newWay = new Way(way);
                    ways.add(newWay);
                    // Ensuring that every node has a ref to the way it is apart of
                    for (Node node : way) {
                        node.setWay(newWay); // Set the way reference in each node
                    }
                    way.clear();
                }
                if(name.equals("node")){
                    if (address != null && !address.getStreet().isBlank()) {
                        addressList.add(address);
                        //System.out.println(addressId);
                        addressIdMap.put(address.getFullAdress().toLowerCase(), id2node.get(addressId));
                        addressId = 0;
                        address = null; // Reset for the next address
                    }
                }
            }
        }
    }

    private void parseTXT(String filename) throws FileNotFoundException {
        File f = new File(filename);
        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                list.add(new Line(s.nextLine()));
            }
        }
    }
    public void parseAddressFromOSM(String v, String k){
        // Assuming you have a Trie instance called 'trie'
//        trie.insert(fullAddress);
//        adressList.add(fullAdress);
        if(address.getStreet().equals(null) || address.getStreet().isEmpty()) {
            if (k.contains("street")) {
                address.setStreet(v);
            } else if (k.contains("housenumber")) {
                address.setHouseNumber(v);
            } else if (k.contains("city")) {
                address.setCity(v);
            } else if (k.contains("municipality")) {
                address.setMunicipality(v);
            } else if (k.contains("country")) {
                address.setCountry(v);
            }
        }
    }

    public void add(Point2D p1, Point2D p2) {
        list.add(new Line(p1, p2));
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public Map<String, Node> getAddressIdMap() {
        return addressIdMap;
    }
    private Trie loadCityNames() {
        Trie trie = new Trie();
//        String path = System.getProperty("user.dir"); // gets which directory the project is placed
//        String filename = path+"\\data\\citynames.txt";
//
//        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
//            String line;
//            while ((line = bReader.readLine()) != null) {
//                trie.insert(line.trim().toLowerCase());
//            }
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
        for(Address address : addressList){
            trie.insert(address.getFullAdress());
        }
        serializeTrie(trie, "data/trie.obj");
        return trie;
    }

    private void serializeTrie(Trie trie, String filepath) {
        try (
                FileOutputStream fileOut = new FileOutputStream(filepath); // Open a file output stream to the specified file.
                ObjectOutputStream out = new ObjectOutputStream(fileOut) // Wrap the file output stream in an ObjectOutputStream.
        ) {
            out.writeObject(trie); // Serialize the Trie object and write it to the file.
        } catch (IOException i) {
            i.printStackTrace(); // Handle potential IO exceptions.
        }
        System.out.println("Created serializable file");
    }
    private Trie deserializeTrie(String filepath) {
        Trie trie = null;
        try (
                FileInputStream fileIn = new FileInputStream(filepath); // Open a file input stream to the specified file.
                ObjectInputStream in = new ObjectInputStream(fileIn) // Wrap the file input stream in an ObjectInputStream.
        ) {
            trie = (Trie) in.readObject(); // Deserialize the object read from the file and cast it to a Trie.
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        // If a serializable file does not exist we will populate the trie ourselves and create a serializable file
        if(!(trie == null))  {

            System.out.println("Serializable file was found");
            return trie; // Return the deserialized Trie object.
        }
        // If a serializable file does not exist we will populate the trie ourselves and create a serializable file
        else return loadCityNames();
    }
    public List<String> getSuggestionList(String input){
        return trie.getAddressSuggestions(input.toLowerCase(), 4);

    }

    // Method to populate a kdtree and making it balanced with data from an array
//    public void populateKDTree(){
////        populateKDTree(kdtree, nodelist, 0);
//        kdTree.populate(kdTree, nodeList);
//    }

    // Helper method which makes recursive calls to itself.
    /*private static void populateKDTree(KDTree kdTree, List<Node> nodeList, int depth) {
        if (nodeList.isEmpty()) return;  // Ensure no operations on an empty list

        List<Node> nodes = nodeList;
        int axis = depth % 2;
        // if the axis is 0, compare the x value long else compare lat
        if(axis == 0){
            // Sort based on the x-axis
            nodes.sort(Comparator.comparingDouble(n -> n.lon));

        } else {
            // Sort based on the y-axis
            nodes.sort(Comparator.comparingDouble(n -> n.lat));
        }

        // Find median index
        int medianIndex = nodes.size() / 2;
        Node medianNode = nodes.get(medianIndex);

        // Insert median nodes into KDTree
        kdTree.put(medianNode.getLon(), medianNode.getLat(), medianNode);

        // iterate depth to ensure that a new axis is sorted for
        depth++;

        // Recursive population call
        // Left Recursive Call, Handles the elements before the median index by only providing from the start of the list to one less than the median index
        // The subList does not include the end of the range in the list is provides
        if (medianIndex > 0)  populateKDTree(kdTree, nodes.subList(0, medianIndex), depth);
        // Right Recursive Call, Handles the elements after the median index by only providing from the median index+1 (avoids duplicates) to the end of the list
        if (medianIndex + 1 < nodeList.size()) populateKDTree(kdTree, nodes.subList(medianIndex+1, nodes.size()), depth);
    }*/

    // Converts canvas coordinates to geo coordinates or vice versa, depending on true/false
    public Point2D convertToCoordinates(Point2D pointFromCanvas, Boolean toGeoCoord, Affine trans){
        try{
            // Transforms coordinates from canvas to geo coordinates using inbuilt functionality
            if(toGeoCoord){
                Point2D geoCoordPoint = trans.inverseTransform(pointFromCanvas);

                // Calculations to make sure the coordinates are within the bounds
                //double geoLon = Math.max(minlon, Math.min(maxlon, geoCoordPoint.getX()));
                //double geoLat = Math.max(minlat, Math.min(maxlat, geoCoordPoint.getY()));

                //return new Point2D(geoLon, geoLat);
                return new Point2D(geoCoordPoint.getX(), geoCoordPoint.getY());
            }
            // Vice versa
            else{
                //double reverseLon = Math.max(minlon, Math.min(maxlon, pointFromCanvas.getX()));
                //double reverseLat = Math.max(minlat, Math.min(maxlat, pointFromCanvas.getY()));

                //Point2D canvasPoint = new Point2D(reverseLon, reverseLat);
                Point2D canvasPoint = new Point2D(pointFromCanvas.getX(), pointFromCanvas.getY());
                return trans.transform(canvasPoint);
            }

//            if (toGeoCoord) {
//                Point2D geoCoordPoint = trans.inverseTransform(pointFromCanvas);
//                System.out.println("Converting canvas to geo: " + geoCoordPoint.toString());
//                return geoCoordPoint;
//            } else {
//                Point2D canvasPoint = trans.transform(pointFromCanvas);
//                System.out.println("Converting geo to canvas: " + canvasPoint.toString());
//                return canvasPoint;
//            }
        }catch(NonInvertibleTransformException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public double getMinlon(){
        return minlon;
    }
    public double getMaxlon(){
        return maxlon;
    }
    public double getMinlat(){
        return minlat;
    }
    public double getMaxlat(){
        return maxlat;
    }


}


