package com.telos.mapofdenmark;

import java.io.BufferedInputStream;
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
import javax.xml.stream.*;

import com.telos.mapofdenmark.KDTreeClasses.KDTree;
import com.telos.mapofdenmark.Shortest_Route.DirectedEdge;
import com.telos.mapofdenmark.Shortest_Route.EdgeWeightedDigraph;
import com.telos.mapofdenmark.Shortest_Route.SP;
import com.telos.mapofdenmark.TrieClasses.Address;
import com.telos.mapofdenmark.TrieClasses.Trie;

public class Model implements Serializable {
    private static final long serialVersionUID = 9300313068198046L;
    List<Line> list = new ArrayList<Line>();
    List<Way> ways = new ArrayList<Way>();
    List<Node> nodeList = new ArrayList<>();
    List<Relation> Relations = new ArrayList<>();
    // Collection used for storing center points such that multiple nodes with same way ref is not used to populate KDTree
    List<Node> centerPointNodes = new ArrayList<>();
    SP Dijkstra = null;
    private Trie trie;
    double minlat, maxlat, minlon, maxlon;
    List<Address> addressList;
    Map<String, Node> addressIdMap;
    KDTree kdTree;
    Address address;
    EdgeWeightedDigraph EWD;
    HashMap<Node, Integer> DigraphNodeToIndex;
    HashMap<Integer, Node> DigraphIndexToNode;
    HashMap<Long, Node> id2node;
    List<Member> relationsMembers;
    HashMap<Long, Way> id2way;
    int roadCount;
    int indexForCenterPoints = 0;
    Map<String, Double> roadIdSet;
    HashSet<String> cycleTags;
    static Model load(String filename) throws FileNotFoundException, IOException, ClassNotFoundException, XMLStreamException, FactoryConfigurationError {
        if (filename.endsWith(".obj")) {
            try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                return (Model) in.readObject();
            }
        }
        return new Model(filename);
    }


    public Model(String filename) throws XMLStreamException, FactoryConfigurationError, IOException {
        this.roadIdSet = new HashMap<String, Double>(Map.of(
                "motorway",0.4545,
                "trunk", 0.625,
                "primary", 0.625,
                "secondary", 0.7142857,
                "tertiary", 0.833,
                "unclassified", 1.0,
                "residential", 1.25,
                "motorway_link", 0.4545,
                "trunk_link", 0.71428574,
                "primary_link", 0.7142857));
        roadIdSet.put("secondary_link", 0.833);
        roadIdSet.put("tertiary_link", 1.0);
        roadIdSet.put("living_street", 2.5);
        roadIdSet.put("track", 1.25);
        roadIdSet.put("service", 1.0);
        roadIdSet.put("pedestrian", 10.0);
        roadIdSet.put("path", 10.0);
        this.cycleTags = new HashSet<String>(List.of(
                "primary",
                "secondary",
                "tertiary",
                "unclassified",
                "residential",
                "primary_link",
                "secondary_link",
                "tertiary_link",
                "living_street",
                "track",
                "cycleway",
                "yes",
                "designated",
                "use_sidepath",
                "optional_sidepath",
                "permissive",
                "destination",
                "private",
                "pedestrian",
                "footway"));
        this.DigraphNodeToIndex = new HashMap<>();
        this.DigraphIndexToNode = new HashMap<>();
        this.id2way = new HashMap<>();
        this.id2node = new HashMap<>();
        this.relationsMembers = new ArrayList<>();
        this.roadCount = 0;
        this.addressList = new ArrayList<>();
        this.address = new Address();
        this.addressIdMap = new HashMap<>(); // Used for ref a node id to an adress
        if (filename.endsWith(".osm.zip")) {
            parseZIP(filename);
        } else if (filename.endsWith(".osm")) {
            parseRouteNet(filename);
        } /*else {
            parseTXT(filename);
        }*/
        save(filename+".obj");
        this.trie = deserializeTrie("data/.obj");
        this.kdTree = new KDTree();
        // Populates the KDTree using all nodes from .osm
//        kdTree.populate(nodeList);
        // Populates the KDTree using the centerPointNodes collection such that reference to same way is avoided
        kdTree.populate(centerPointNodes);
    }
    private void parseNodeNet(InputStream inputStream) throws IOException, FactoryConfigurationError, XMLStreamException, FactoryConfigurationError {
        var input = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        long addressId = 0;
        int roadCountX = 0;


        while (input.hasNext()) {

            var tagKind = input.next();
            if (tagKind == XMLStreamConstants.START_ELEMENT) {
                var name = input.getLocalName();
                switch (name) {
                    case "bounds" -> {
                        minlat = Double.parseDouble(input.getAttributeValue(null, "minlat"));
                        maxlat = Double.parseDouble(input.getAttributeValue(null, "maxlat"));
                        minlon = Double.parseDouble(input.getAttributeValue(null, "minlon"));
                        maxlon = Double.parseDouble(input.getAttributeValue(null, "maxlon"));
                    }
                    case "node" -> {
                        var id = Long.parseLong(input.getAttributeValue(null, "id"));
                        var lat = Double.parseDouble(input.getAttributeValue(null, "lat"));
                        var lon = Double.parseDouble(input.getAttributeValue(null, "lon"));
                        Node node = new Node(id, lat, lon);
                        id2node.put(id, node);
                        nodeList.add(node);
                        addressId = id;
                        DigraphNodeToIndex.put(node, roadCountX);
                        DigraphIndexToNode.put(roadCountX, node);
                        roadCountX++;
//                    address = new Address(); // Reset for new node
                    }
                    case "tag" -> {
                        var v = input.getAttributeValue(null, "v");
                        var k = input.getAttributeValue(null, "k");
                        if (k.startsWith("addr:")) {
                            // Lazy initialization of address
                            if (address == null) {
                                address = new Address();
                            }
                            parseAddressFromOSM(v, k);
                        }
                    }
                    case "way" -> {
                        EWD = new EdgeWeightedDigraph(roadCountX);
                        parseWaysAndRelations(input, tagKind);
                        return;
                    }
                    case "relation" -> {
                        parseWaysAndRelations(input,tagKind);
                        return;
                    }
                }
            } else if (tagKind == XMLStreamConstants.END_ELEMENT) {
                var name = input.getLocalName();
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

    private void parseRouteNet(String filename) throws IOException, FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        parseNodeNet(new FileInputStream(filename));
    }

    void save(String filename) throws FileNotFoundException, IOException {
        try (var out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    private void parseZIP(String filename) throws IOException, XMLStreamException, FactoryConfigurationError {
        var input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        parseNodeNet(input);
    }

    private void parseWaysAndRelations(XMLStreamReader input1, int tagKind) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        var way = new ArrayList<Node>();
        var coast = false;
        String roadtype = "";
        String RelationsType = "";
        boolean shouldAdd = false;
        boolean drivable = false;
        boolean cycleable = false;
        boolean oneway = false;
        boolean onewayBicycle = false;
        boolean insideRelation = false;
        boolean access = false;
        boolean acccesPostBollean = false;
        long wayid = 0;
        int vertexIndex = -1;

        while (input1.hasNext()) {
            tagKind = input1.next();
            if (tagKind == XMLStreamConstants.START_ELEMENT) {
                var name = input1.getLocalName();
                if (name.equals("way")) {
                    wayid = Long.parseLong(input1.getAttributeValue(null,"id"));
                    way.clear();
                } else if (!insideRelation && name.equals("tag")) {
                    var v = input1.getAttributeValue(null, "v");
                    var k = input1.getAttributeValue(null, "k");
                    if (k.equals("highway")) {
                        roadtype = v;
                        if (roadIdSet.containsKey(v)) {
                            drivable = true;
                            shouldAdd = true;
                        } if (v.equals("service") || v.equals("path")) {
                            if (access) {
                                shouldAdd = true;
                                cycleable = true;
                                drivable = true;
                            } else {
                                acccesPostBollean = true;
                            }
                        }
                        if (cycleTags.contains(v)) {
                            cycleable = true;
                            shouldAdd = true;
                        }
                    } else if (k.equals("oneway")) {
                        oneway = v.equals("yes");
                    } else if (k.equals("oneway:bicycle")) {
                        onewayBicycle = v.equals("yes");
                    } else if (k.equals("bicycle")) {
                        if (cycleTags.contains(v)) {
                            cycleable = true;
                            shouldAdd = true;
                        } else {
                            cycleable = false;
                        }
                    } else if (k.equals("access")) {
                        access = cycleTags.contains(v);
                    } else if (k.equals("motor_vehicle") && acccesPostBollean && cycleTags.contains(v))  {
                        shouldAdd = true;
                        cycleable = true;
                        drivable = true;
                    }

                } else if (name.equals("nd")) {
                    var ref = Long.parseLong(input1.getAttributeValue(null, "ref"));
                    var node = id2node.get(ref);
                    way.add(node);
                } else if (name.equals("relation")) {
                    
                    insideRelation = true;
                    relationsMembers.clear();
                    RelationsType = "";
                } else if (insideRelation && name.equals("member")) {
                    // parse Ref
                    var ref = Long.parseLong(input1.getAttributeValue(null,"ref"));
                    var role = input1.getAttributeValue(null,"role");
                    var Member = new Member(role,ref);
                    Member.setWay(id2way.get(ref));
                    relationsMembers.add(Member);
                } else if (insideRelation && name.equals("tag")) {
                    var k = input1.getAttributeValue(null,"k");
                    var v = input1.getAttributeValue(null,"v");

                    if(k.equals("type")){
                        RelationsType = v;
                    }
                }

            } else if (tagKind == XMLStreamConstants.END_ELEMENT) {
                var name = input1.getLocalName();
                // If you wish to only draw coastline -- if (name == "way" && coast) {
                if (name.equals("way")) {
                    if (!roadtype.isEmpty()) {
                        Road tmpRoad = new Road(way,roadtype);
                        ways.add(tmpRoad);
                        addToCenterPointNodes(way, tmpRoad, true);
                    } else {
                        Way tmpWay = new Way(way);
                        ways.add(tmpWay);
                        addToCenterPointNodes(way, tmpWay, false);
                    }
                    // Ensuring that every node has a ref to the way it is apart of
                    for (Node node : way) {
                        if (shouldAdd && vertexIndex != -1) {
                            double weight_without_modifier = 1.0;
                            double weight_car = 1.0;
                            double weight_cycle = 0.5;
                            // calculate the weight depending on tags
                            if(!cycleable){
                                 weight_cycle = 500000.0;
                            }
                            if (!drivable) {
                                weight_car = 500000.0;
                            } else if(roadIdSet.containsKey(roadtype)){
                                weight_car = weight_without_modifier * roadIdSet.get(roadtype);
                            }
                            if (oneway) {
                                if (onewayBicycle) {
                                    EWD.addEdge(new DirectedEdge(vertexIndex, DigraphNodeToIndex.get(node), weight_cycle, weight_car ));
                                } else {
                                    EWD.addEdge(new DirectedEdge(vertexIndex, DigraphNodeToIndex.get(node), weight_cycle, weight_car ));
                                    EWD.addEdge(new DirectedEdge(DigraphNodeToIndex.get(node), vertexIndex, weight_cycle, 500000.0 ));
                                }
                            } else {
                                EWD.addEdge(new DirectedEdge(vertexIndex, DigraphNodeToIndex.get(node), weight_cycle, weight_car ));
                                EWD.addEdge(new DirectedEdge(DigraphNodeToIndex.get(node), vertexIndex, weight_cycle, weight_car ));
                            }

                        } else {
                            vertexIndex = DigraphNodeToIndex.get(node);
                        }
                    }
                    Way newWay = new Way(way);
                    id2way.put(wayid,newWay);
                    way.clear();
                    roadtype = "";
                    shouldAdd = false;
                    drivable = false;
                    cycleable = false;
                    oneway = false;
                    onewayBicycle = false;
                    vertexIndex = -1;
                    access = false;
                    acccesPostBollean = false;
                } else if (name.equals("relation") && insideRelation) {
                    insideRelation = false;
                    Relations.add(new Relation(RelationsType,relationsMembers));
//                    System.out.println("Relation added");
                }
            }
        }
    }

    //Dijkstra implementation
    public void StartDijkstra(Node startaddress,boolean vehicle){
        list.clear();
        this.Dijkstra = new SP(EWD,DigraphNodeToIndex.get(findNodeByID(nodeList, "697814")),vehicle); // this starts the dijkstra search from the index that refferes to a node
    }



    /**
     * Since it iterates backwards from the end goal to the front that won't matter since the distance/ drawn area will be the same
     * This will then create a Hashset to check if the node has already been added since it is constant time O(1)
     * where looking it up in the list would have been linear time O(n) which in turn would have taken longer depending on the size of the path.
     * Therefore, we chose to use a Hashset for more optimal timing.
     * This will then return the paths ass nodes in an arraylist goning for index 0 being the final node up to the first node at n place in the array.
     * This will make it easier to draw since we know have a new Way of those nodes to be drawn.
     *
     * @param /Endaddress The end addres for Dijkstras algorithm
     * @return Returns a list of nodes in order from start to finish
     *
     * */
    public List<Node> getDijkstraPath(Node Endaddress) {
         List<Node> path = new ArrayList<Node>(); // this is everything that needs to be drawn for the path
         HashSet<Node> NodeAdded = new HashSet<Node>();
         for(DirectedEdge i: Dijkstra.pathTo(DigraphNodeToIndex.get(findNodeByID(nodeList, "92994313")))) {
             if(!NodeAdded.contains(DigraphIndexToNode.get(i.to()))){
                 NodeAdded.add(DigraphIndexToNode.get(i.to()));
                 path.add(DigraphIndexToNode.get(i.to()));
             } else if (!NodeAdded.contains(DigraphIndexToNode.get(i.from()))){
                 NodeAdded.add(DigraphIndexToNode.get(i.from()));
                 path.add(DigraphIndexToNode.get(i.from()));
             }

         }
         System.out.println(path);
         return path;
    }
    //





    // used for test
    public Node findNodeByID(List<Node> nodeList, String id) {

        for (Node node : nodeList) {
            if(node.getId().equals(id)){
                return node;
            }
        }
        System.out.println("yes");
        return null;
    }


    /*private void parseTXT(String filename) throws FileNotFoundException {
        File f = new File(filename);
        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                list.add(new Line(s.nextLine()));
            }
        }
    }*/
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
    // credit James K polk from StackOwerflow
    private static double distance(double lat1, double lat2, double lon1,
                                  double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        double height = 0.0;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
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

    // finds the center lat and lon among a collection of nodes
    public void addToCenterPointNodes(List<Node> nodes, Way refWay, Boolean isPartOfRoad){
        double sumLat = 0;
        double sumLon = 0;
        for(Node node : nodes){
            sumLat += node.getLat();
            sumLon += node.getLon();
        }
        double centerLat = sumLat / nodes.size();
        double centerLon = sumLon / nodes.size();
        Node centeredNode = new Node(indexForCenterPoints, centerLat, centerLon);
        centeredNode.setWay(nodes.get(0).getWay());
        centeredNode.setWay(refWay);
        centeredNode.setPartOfRoad(isPartOfRoad);
        centerPointNodes.add(centeredNode);
        indexForCenterPoints++;
    }



}


