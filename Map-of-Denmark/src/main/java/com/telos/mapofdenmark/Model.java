package com.telos.mapofdenmark;

import java.io.BufferedInputStream;
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
import java.util.TreeMap;

import com.telos.mapofdenmark.KDTreeClasses.KDTreeWay;
import com.telos.mapofdenmark.Shortest_Route.DirectedEdge;
import com.telos.mapofdenmark.Shortest_Route.EdgeWeightedDigraph;
import com.telos.mapofdenmark.Shortest_Route.SP;
import com.telos.mapofdenmark.TrieClasses.Address;
import com.telos.mapofdenmark.TrieClasses.RadixTrie;
import com.telos.mapofdenmark.TrieClasses.Trie;
import javafx.geometry.Point2D;

/**
 * The Model class is responsible for parsing and storing the data from OpenStreetMap files.
 * It provides methods for loading and saving the data, as well as accessing various collections
 * and data structures used for storing and retrieving information about nodes, ways, addresses,
 * relations, and more.
 */
public class Model implements Serializable {
    private static final long serialVersionUID = 9300313068198046L;

    List<Line> list = new ArrayList<>();
    List<Relation> RelationsPlace = new ArrayList<>();
    List<Relation> RelationsNatural = new ArrayList<>();
    List<Relation> RelationsLanduse = new ArrayList<>();
    List<Relation> RelationsBuilding = new ArrayList<>();

    // Collection used for storing points of interest
    List<Point2D> pointsOfInterest = new ArrayList<>();

    // Collection used for storing center points for relations
    List<Way> centerPointRelationsBuilding = new ArrayList<>();
    List<Way> centerPointRelationsNatural = new ArrayList<>();
    List<Way> centerPointRelationsLanduse = new ArrayList<>();

    // Collection used for storing center points for ways
    List<Way> centerPointWaysPlace = new ArrayList<>();
    List<Way> centerPointWaysNatural = new ArrayList<>();
    List<Way> centerPointWaysLanduse = new ArrayList<>();
    List<Way> centerPointWaysBuilding = new ArrayList<>();
    List<Way> centerPointWaysRoad = new ArrayList<>();

    SP Dijkstra = null;
    private Trie trie;
    private RadixTrie radixTrie;
    double minlat, maxlat, minlon, maxlon;
    List<Address> addressList;
    Address address;

    Map<String, Node> addressIdMap;

    // KD-Trees for relations
    KDTreeWay kdTreeBuildings; // KDTree holds relation buildings
    KDTreeWay kdTreeNaturals; // KDTree holds relation naturals
    KDTreeWay kdTreeLanduses; // KDTree holds relation landuses

    // KD-Trees for ways
    KDTreeWay kdTreeWaysPlace; // KDTree holds ways places
    KDTreeWay kdTreeWaysNatural; // KDTree holds ways natural
    KDTreeWay kdTreeWaysLanduse; // KDTree holds ways landuses
    KDTreeWay kdTreeWaysBuilding; // KDTree holds ways buildings
    KDTreeWay kdTreeWaysRoad; // KDTree holds ways roads

    EdgeWeightedDigraph EWD;
    TreeMap<String, Node> id2node;
    List<Member> relationsMembers;
    HashMap<Long, Way> id2way;
    int indexForCenterPoints = 0;
    HashMap<String, Double> roadIdSet;
    HashSet<String> cycleTags;
    ColorScheme cs;
    LineThickness lt;

    // Set with allowed key types when parsing ways and relations.
    Set<String> allowedKeyTypes;
    // Set with banned landforms (values) when parsing ways and relations.
    Set<String> bannedLandforms;

    // Sets corresponding to road layers with values.
    Set<String> xsmallRoads;
    Set<String> smallRoads;
    Set<String> mediumRoads;
    Set<String> bigRoads;

    /**
     * Constructs a new Model instance by parsing the given input stream.
     *
     * @param inputStream The input stream containing the data to be parsed.
     * @param filename The name of the file being parsed.
     * @throws XMLStreamException If an error occurs while parsing the XML data.
     * @throws FactoryConfigurationError If a factory configuration error occurs.
     * @throws IOException If an I/O error occurs while reading the input stream.
     */
    public Model(InputStream inputStream, String filename) throws XMLStreamException, FactoryConfigurationError, IOException {
        cs = new ColorScheme();
        lt = new LineThickness();
        initializeRoadIdSetMap();
        initializeCycleTagsHashSet();
        this.id2way = new HashMap<>();
        this.id2node = new TreeMap<>();
        this.relationsMembers = new ArrayList<>();
        this.addressList = new ArrayList<>();
        this.address = new Address();
        this.addressIdMap = new TreeMap<>(); // Used for ref a node id to an address
        this.allowedKeyTypes = new HashSet<>(Arrays.asList("place", "natural", "landuse", "building")); // Allowed types for relations and ways
        this.bannedLandforms = new HashSet<>(Arrays.asList("coastline", "military", "port", "industrial", "harbour", "strait", "recreation_ground")); // Banned types for relations and ways

        this.xsmallRoads = new HashSet<>(Arrays.asList("path", "footway", "cycleway", "designated", "yes", "permissive", "optional_sidepath", "use_sidepath", "desination", "pedestrian"));
        this.smallRoads = new HashSet<>(Arrays.asList("service", "default", "track", "living_street", "residential", "unclassified"));
        this.mediumRoads = new HashSet<>(Arrays.asList("primary", "tertiary_link", "secondary_link", "primary_link", "trunk_link", "motorway_link", "tertiary", "secondary"));
        this.bigRoads = new HashSet<>(Arrays.asList("trunk", "highway"));

        if (filename.endsWith(".osm.zip")) {
            parseZIP(inputStream);
        } else if (filename.endsWith(".osm")) {
            parseRouteNet(inputStream);
        }

        this.trie = new Trie();
        this.radixTrie = new RadixTrie();
        for(Address address : addressList){
            if(address != null){
                trie.insert(address.getStreet());
                radixTrie.insert(address.getFullAddress());
            } else System.out.println("Address is null");
        }

        // KD-Tree for relations
        this.kdTreeBuildings = new KDTreeWay();
        this.kdTreeNaturals = new KDTreeWay();
        this.kdTreeLanduses = new KDTreeWay();
        this.kdTreeWaysPlace = new KDTreeWay();
        this.kdTreeWaysNatural = new KDTreeWay();
        this.kdTreeWaysLanduse = new KDTreeWay();
        this.kdTreeWaysBuilding = new KDTreeWay();
        this.kdTreeWaysRoad = new KDTreeWay();

        // Populates the KDTree using the centerPointNodes collection such that reference to same way is avoided
        // KD-Trees for Relations
        kdTreeBuildings.populate(centerPointRelationsBuilding);
        System.out.println("Size of KD-Tree Relations Building: " + kdTreeBuildings.size());
        kdTreeNaturals.populate(centerPointRelationsNatural);
        System.out.println("Size of KD-Tree Relations Naturals: " + kdTreeNaturals.size());
        kdTreeLanduses.populate(centerPointRelationsLanduse);
        System.out.println("Size of KD-Tree Relations Landuse: " + kdTreeLanduses.size());

        // KD-Trees for Ways
        kdTreeWaysPlace.populate(centerPointWaysPlace);
        System.out.println("Size of KD-Tree Ways Place: " + kdTreeWaysPlace.size());
        kdTreeWaysNatural.populate(centerPointWaysNatural);
        System.out.println("Size of KD-Tree Ways Natural: " + kdTreeWaysNatural.size());
        kdTreeWaysLanduse.populate(centerPointWaysLanduse);
        System.out.println("Size of KD-Tree Ways Landuse: " + kdTreeWaysLanduse.size());
        kdTreeWaysBuilding.populate(centerPointWaysBuilding);
        System.out.println("Size of KD-Tree Ways Building: " + kdTreeWaysBuilding.size());
        kdTreeWaysRoad.populate(centerPointWaysRoad);
        System.out.println("Size of KD-Tree Ways Road: " + kdTreeWaysRoad.size());

        // Saves objects to binary file
        save(filename+".obj");
    }

    /**
     * Loads a Model instance from a file. If the file extension is ".obj", it attempts to deserialize
     * a previously saved Model object. Otherwise, it creates a new Model instance by parsing the given file.
     *
     * @param inputStream The input stream containing the data to be loaded.
     * @param fileName The name of the file being loaded.
     * @return The loaded Model instance.
     * @throws FileNotFoundException If the file cannot be found.
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     * @throws XMLStreamException If an error occurs while parsing the XML data.
     * @throws FactoryConfigurationError If a factory configuration error occurs.
     */
    static Model load(InputStream inputStream, String fileName) throws FileNotFoundException, IOException, ClassNotFoundException, XMLStreamException, FactoryConfigurationError {
        if(fileName.endsWith(".obj")){
            try (var in = new ObjectInputStream(new BufferedInputStream(inputStream))) {
                return (Model) in.readObject();
            } catch (Exception e) {
                // Close the input stream if an exception occurs
                inputStream.close();
                throw e;
            }
        }
        else{
            return new Model(inputStream, fileName);
        }
    }

    /**
     * Initializes the roadIdSet map with predefined road types and their corresponding speeds.
     * The roadIdSet map is used to store the weight for the different roads.
     */
    private void initializeRoadIdSetMap() {
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
    }

    /**
     * Initializes the HashSet of cycle tags.
     * Cycle tags are used to classify various types of cycling paths and routes.
     */
    private void initializeCycleTagsHashSet() {
        this.cycleTags = new HashSet<>(List.of(
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
                "footway",
                "driveway"));
    }

    /**
     * Starts the parsing with nodes. Later on calls function to parse ways and relations.
     *
     * @param inputStream the input stream containing the network data
     * @throws IOException                if an I/O error occurs while reading the stream
     * @throws FactoryConfigurationError if a configuration error occurs while creating XML input factory
     * @throws XMLStreamException         if an error occurs while processing XML
     */
    private void parseNodeNet(InputStream inputStream) throws IOException, FactoryConfigurationError, XMLStreamException, FactoryConfigurationError {
        var input = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        int NodeCount = 0;
        Node node = null;
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
                        var id = input.getAttributeValue(null, "id");
                        var lat = Double.parseDouble(input.getAttributeValue(null, "lat"));
                        var lon = Double.parseDouble(input.getAttributeValue(null, "lon"));
                        node = new Node(NodeCount, lat, lon);
                        id2node.put(id, node);
                        NodeCount++;
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
                    case "way","relation" -> {
                        EWD = new EdgeWeightedDigraph(NodeCount);
                        parseWaysAndRelations(input, tagKind); // First way element ???? input.getname = way
                        return;
                    }
                }
            } else if (tagKind == XMLStreamConstants.END_ELEMENT) {
                var name = input.getLocalName();
                if(name.equals("node")){
                    if (address != null && !address.getStreet().isBlank()) {
                        addressList.add(address);
                        //System.out.println(addressId);
                        addressIdMap.put(address.getFullAddress().toLowerCase(), node);
                        address = null; // Reset for the next address
                    }
                }
            }
        }
        input.close();
        System.gc();
    }
    /**
     * Parses the route network from the given input stream.
     *
     * This method reads XML data from the provided input stream and processes it to extract
     * information about the route network. It internally calls the {@link #parseNodeNet(InputStream)}
     * method to parse the node network.
     *
     * @param inputStream The input stream containing the XML data of the route network.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws FileNotFoundException Signals that an attempt to open the file denoted by a specified pathname has failed.
     * @throws XMLStreamException Indicates an error in the XML stream being processed.
     * @throws FactoryConfigurationError Signals an error in the configuration of the XML parser factory.
     *
     * @see #parseNodeNet(InputStream)
     */
    private void parseRouteNet(InputStream inputStream) throws IOException, FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        parseNodeNet(inputStream);
    }

    /**
     * Saves the current object to a binary file using serialization.
     *
     * @param filename the name of the file to which the object will be saved
     * @throws FileNotFoundException if the specified file is not found or cannot be opened for writing
     * @throws IOException if an I/O error occurs while writing to the file
     */
    void save(String filename) throws FileNotFoundException, IOException {
        try (var out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    /**
     * Parses the contents of a ZIP file containing XML data.
     *
     * This method extracts the contents of the ZIP file from the provided input stream,
     * reads the XML data, and parses it using the parseNodeNet method.
     *
     * @param inputStream the input stream containing the ZIP file data to parse
     * @throws IOException if an I/O error occurs while reading the ZIP file
     * @throws XMLStreamException if an error occurs while parsing the XML data
     * @throws FactoryConfigurationError if a configuration error is encountered while
     * creating the XML input factory
     */
    private void parseZIP(InputStream inputStream) throws IOException, XMLStreamException, FactoryConfigurationError {
        var input = new ZipInputStream(inputStream);
        input.getNextEntry();
        parseNodeNet(input);
    }

    /**
     * Parses ways and relations from XML input stream.
     *
     * @param input1    XMLStreamReader representing the input stream
     * @param tagKind   Current XML tag kind
     * @throws FileNotFoundException    If the file specified by input1 is not found
     * @throws XMLStreamException        If an error occurs while processing the XML stream
     * @throws FactoryConfigurationError If a configuration error occurs while creating XML input factories
     */
    private void parseWaysAndRelations(XMLStreamReader input1, int tagKind) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        var way = new ArrayList<Node>();
        var building = false;
        var coast = false;
        var relationLandform = "";
        var validRelation = false;
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
        String relationKey = "";
        Node startNode = null;
        String wayKey = "";
        String wayLandform = "";

        //if (!parsedFirstWay){

        if (input1.getLocalName().equals("way")){
            wayid = Long.parseLong(input1.getAttributeValue(null,"id"));
            wayKey = "";
            wayLandform = "";
        }
        //}
        //if (!parsedFirstRelation){
        if (input1.getLocalName().equals("relation")){
            relationsMembers = new ArrayList<>();
            insideRelation = true;
            RelationsType = "";
            relationLandform = "";
            relationKey = "";
        }
        //}

        while (input1.hasNext()) {
            tagKind = input1.next();
            if (tagKind == XMLStreamConstants.START_ELEMENT) {
                var name = input1.getLocalName();
                if (name.equals("way")) {
                    wayid = Long.parseLong(input1.getAttributeValue(null,"id"));
                    way.clear();
                    building = false;
                    coast = false;
                    wayKey = "";
                    wayLandform = "";
                } else if (!insideRelation && name.equals("tag")) {
                    var v = input1.getAttributeValue(null, "v");
                    var k = input1.getAttributeValue(null, "k");
                    switch (k) {
                        case "place", "natural", "landuse", "building":
                            wayKey = k;
                            wayLandform = v;
                            break;
                        case "highway":
                            roadtype = v;
                            if (roadIdSet.containsKey(v)) {
                                drivable = true;
                                shouldAdd = true;
                            }
                            if (v.equals("service") || v.equals("path")) {
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
                            break;
                        case "oneway":
                            oneway = v.equals("yes");
                            break;
                        case "oneway:bicycle":
                            onewayBicycle = v.equals("yes");
                            break;
                        case "bicycle":
                            if (cycleTags.contains(v)) {
                                cycleable = true;
                                shouldAdd = true;
                            } else {
                                cycleable = false;
                            }
                            break;
                        case "access":
                            access = cycleTags.contains(v);
                            break;
                        case "motor_vehicle":
                            if (acccesPostBollean && cycleTags.contains(v)) {
                                shouldAdd = true;
                                cycleable = true;
                                drivable = true;
                            }
                            break;
                        case "service":
                            if (cycleTags.contains(v)) {
                            shouldAdd = true;
                            cycleable = true;
                            drivable = true;
                            }
                    }

                } else if (name.equals("nd")) {
                    var ref = input1.getAttributeValue(null, "ref");
                    var node = id2node.get(ref);
                    way.add(node);
                } else if (name.equals("relation")) {
                    relationsMembers = new ArrayList<>();
                    insideRelation = true;
                    RelationsType = "";
                    relationLandform = "";
                    relationKey = "";
                    validRelation = false;
                } else if (insideRelation && name.equals("member")) {
                    // parse Ref
                    var ref = Long.parseLong(input1.getAttributeValue(null,"ref"));
                    var role = input1.getAttributeValue(null,"role");
                    var Member = new Member(role);
                    Member.setWay(id2way.get(ref));
                    relationsMembers.add(Member);
                } else if (insideRelation && name.equals("tag")) {
                    var k = input1.getAttributeValue(null,"k");
                    var v = input1.getAttributeValue(null,"v");

                    if(k.equals("type")){
                        RelationsType = v;
                    } else if (allowedKeyTypes.contains(k)) {
                        validRelation = true;
                        relationKey = k;
                        relationLandform = v;
                    }
                }

            } else if (tagKind == XMLStreamConstants.END_ELEMENT) {
                var name = input1.getLocalName();
                // If you wish to only draw coastline -- if (name == "way" && coast) {
                if (name.equals("way")) {
                    if (!roadtype.isEmpty()) { // Is a road
                        Road tmpRoad = new Road(way, roadtype, lt);
                        addToCenterWays(tmpRoad, "road");
                        id2way.put(wayid,tmpRoad);
                    } else { // Is not a road
                        //System.out.println("wayKey: " + wayKey);
                        //System.out.println("wayLandform: " + wayLandform);
                        Way tmpWay = new Way(way, wayLandform, wayKey);
                        if (!bannedLandforms.contains(wayLandform)) {
                            // If the last node isn't the same as the first then don't draw it.
                            // This makes sure we don't get any lines that isn't roads.
                            // We want to have closed ways, so we can fill them with color.
                            if (way.get(0) == way.get(way.size()-1)){
                            addToCenterWays(tmpWay, wayKey);
                            }

                        }
                        id2way.put(wayid,tmpWay);
                    }
                    // Ensuring that every node has a ref to the way it is apart of
                    for (Node node : way) {
                        if (shouldAdd && startNode != null) {
                            double dist = distance(startNode.lat, node.lat, startNode.lon, node.lon);
                            double weight_distance_modifier = dist;
                            double weight_car = 1.0;
                            double weight_cycle = dist;
                            // calculate the weight depending on tags
                            if(!cycleable){
                                 weight_cycle = 500000.0;
                            }
                            if (!drivable) {
                                weight_car = 500000.0;
                            } else if(roadIdSet.containsKey(roadtype)){
                                weight_car = weight_distance_modifier * roadIdSet.get(roadtype);
                            }
                            if (oneway) {
                                if (onewayBicycle) {
                                    EWD.addEdge(new DirectedEdge(startNode.id, node.id, weight_cycle, weight_car ));
                                } else {
                                    EWD.addEdge(new DirectedEdge(startNode.id, node.id, weight_cycle, weight_car ));
                                    EWD.addEdge(new DirectedEdge(node.id, startNode.id, weight_cycle, 500000.0 ));
                                }
                            } else {
                                EWD.addEdge(new DirectedEdge(startNode.id, node.id, weight_cycle, weight_car ));
                                EWD.addEdge(new DirectedEdge(node.id, startNode.id, weight_cycle, weight_car ));
                            }

                        }
                        startNode = node;
                    }
                    way.clear();
                    roadtype = "";
                    shouldAdd = false;
                    drivable = false;
                    cycleable = false;
                    oneway = false;
                    onewayBicycle = false;
                    access = false;
                    acccesPostBollean = false;
                    insideRelation = false;
                    RelationsType = "";
                    startNode = null;
                } else if (name.equals("relation") && insideRelation) {
                    insideRelation = false;
                    if (RelationsType.equals("multipolygon")) {
                        if (validRelation && !bannedLandforms.contains(relationLandform)) {
                            if (relationKey.equals("place")) {
                                RelationsPlace.add(new Relation(relationsMembers,relationLandform, cs));
                            } else if (relationKey.equals("building")) {
                                Relation tmpRelation = new Relation(new ArrayList<>(relationsMembers) ,relationLandform,cs);
                                RelationsBuilding.add(tmpRelation);
                                // Temp fix for ensuring that the same relation ref is not added twice
                                if(relationsMembers.get(0).getWay() != null) {
                                    addToCenterRelations(relationsMembers.get(0).getWay(), tmpRelation, "building");
                                }

                                // for loop to find all members in relation and add to collection
//                                for(Member member : relationsMembers){
//                                    if(member.getWay() != null){
//                                        addToCenterRelations(member.getWay(), tmpRelation, "building");
//                                    }
//                                }
                            } else if (relationKey.equals("natural")) {
                                Relation tmpRelation = new Relation(new ArrayList<>(relationsMembers) ,relationLandform,cs);
                                RelationsNatural.add(tmpRelation);
                                // Temp fix for ensuring that the same relation ref is not added twice
                                if(relationsMembers.get(0).getWay() != null) {
                                    addToCenterRelations(relationsMembers.get(0).getWay(), tmpRelation, "natural");
                                }

                                // for loop to find all members in relation and add to collection
//                                for(Member member : relationsMembers){
//                                    if(member.getWay() != null){
//                                        addToCenterRelations(member.getWay(), tmpRelation, "natural");
//                                    }
//                                }
                            } else if (relationKey.equals("landuse")) {
                                Relation tmpRelation = new Relation(new ArrayList<>(relationsMembers) ,relationLandform,cs);
                                RelationsLanduse.add(tmpRelation);
                                // Temp fix for ensuring that the same relation ref is not added twice
                                if(relationsMembers.get(0).getWay() != null) {
                                    addToCenterRelations(relationsMembers.get(0).getWay(), tmpRelation, "landuse");
                                }

                                // for loop to find all members in relation and add to collection
//                                for(Member member : relationsMembers){
//                                    if(member.getWay() != null){
//                                        addToCenterRelations(member.getWay(), tmpRelation, "landuse");
//                                    }
//                                }
                            }
                        }
                    }
                }
            }
        }
        id2node.clear();
    }

    /**
     * Initializes the Dijkstra's shortest path algorithm from the specified start node.
     *
     * @param startaddress The starting node for the Dijkstra's algorithm.
     * @param vehicle A boolean value indicating whether the path calculation is for a cycle (true) or car (false).
     * @throws NullPointerException if startaddress is null.
     */
    public void StartDijkstra(Node startaddress,boolean vehicle){
        double x = startaddress.getLon();
        double y = startaddress.getLat();
        Node tmpNode = kdTreeWaysRoad.getNearestNeighbor(x,y,true).getArbitraryNode();
        list.clear();
        this.Dijkstra = new SP(EWD,tmpNode.id,vehicle);
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
        double x = Endaddress.getLon();
        double y = Endaddress.getLat();
        Node tmpNode = kdTreeWaysRoad.getNearestNeighbor(x,y,true).getArbitraryNode();

         List<Node> path = new ArrayList<Node>(); // this is everything that needs to be drawn for the path
         HashSet<Node> NodeAdded = new HashSet<Node>();
            for(DirectedEdge i: Dijkstra.pathTo(tmpNode.id)) {
                Node node1 = getNodeFromKDTree(i.to());
                if (!NodeAdded.contains(node1)) {
                    NodeAdded.add(node1);
                    path.add(node1);
                    continue;
                }
                Node node2 = getNodeFromKDTree(i.from());
                if (!NodeAdded.contains(node2)) {
                    NodeAdded.add(node2);
                    path.add(node2);
                }
            }
        return path;
    }

    /**
     * Retrieves a Node from the KD-Tree based on its ID.
     *
     * This method iterates through all ways stored in the KD-Tree of roads and searches for the Node with the specified ID.
     *
     * @param id The ID of the Node to retrieve.
     * @return The Node with the specified ID if found, otherwise null.
     */
    private Node getNodeFromKDTree(int id) {
        for (Way way: kdTreeWaysRoad.getAllWays()) {
            for (Node node: way.getNodes()) {
                if (node.id == id) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * Parses address components from OpenStreetMap data and sets them in the Address object if they are not already set.
     * This method is used to extract address information from key-value pairs retrieved from OSM data and populate the Address object accordingly.
     *
     * @param v The value corresponding to the key in the OSM data.
     * @param k The key indicating the type of address component in the OSM data.
     */
    public void parseAddressFromOSM(String v, String k){
        if(address.getStreet().equals(null) || address.getStreet().isEmpty()) {
            if (k.contains("street")) {
                address.setStreet(v);
            } else if (k.contains("housenumber")) {
                address.setHouseNumber(v);
            } else if (k.contains("city")) {
                address.setCity(v);
            } else if (k.contains("municipality")) {
                // To save performance it may be easier to not store the municipality in the trie but instead map city to municipality
                address.setMunicipality(v);
            } else if (k.contains("country")) {
                address.setCountry(v);
            }
        }
    }


    /**
     * Calculates the distance between two points on the Earth's surface using the Haversine formula.
     * The Haversine formula calculates the shortest distance between two points on a sphere given their longitudes and latitudes.
     * Credit James K Polk from StackOverflow
     *
     * @param lat1 Latitude of the first point in degrees.
     * @param lat2 Latitude of the second point in degrees.
     * @param lon1 Longitude of the first point in degrees.
     * @param lon2 Longitude of the second point in degrees.
     * @return The distance between the two points in meters.
     */
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

    /**
     * Retrieves the list of addresses.
     *
     * @return The list of addresses.
     */
    public List<Address> getAddressList() {
        return addressList;
    }

    /**
     * Returns a reference to the map containing address IDs mapped to corresponding nodes.
     *
     * @return The map containing address IDs mapped to corresponding nodes.
     */
    public Map<String, Node> getAddressIdMap() {
        return addressIdMap;
    }

    /**
     * Returns a reference to the map containing address IDs mapped to corresponding nodes.
     *
     * @return The map containing address IDs mapped to corresponding nodes.
     */
    public List<String> getSuggestionList(String input){
        return radixTrie.getAddressSuggestions(input.toLowerCase(), 5);
    }

    /**
     * Retrieves a list of street names based on the input string.
     *
     * @param input the input string to search for street names
     * @return a List containing street names suggested by the Trie data structure
     */
    public List<String> getStreetNamesList(String input){
        return trie.getAddressSuggestions(input.toLowerCase(), 4, true);
    }

    /**
     * Checks if a given word is present in the Trie.
     *
     * @param inputWord The word to search for in the Trie.
     * @return {@code true} if the word is found in the Trie, {@code false} otherwise.
     */
    public boolean isWordInTrie(String inputWord){
        return trie.contains(inputWord);
    }

    /**
     * Adds a point of interest (POI) to the collection.
     *
     * @param POI The Point2D object representing the point of interest to be added.
     */
    public void addPOI(Point2D POI) {
        pointsOfInterest.add(POI);
    }

    /**
     * Retrieves the list of points of interest.
     *
     * @return A List containing Point2D objects representing points of interest.
     */
    public List<Point2D> getPointsOfInterest() {
        return pointsOfInterest;
    }

    /**
     * Retrieves the color scheme associated with this object.
     *
     * @return The ColorScheme object representing the color scheme.
     */
    public ColorScheme getColorScheme() {
        return cs;
    }

    // finds the center lat and lon among a collection of nodes
    public void addToCenterRelations(Way way, Relation refRelation, String type){
        way.setRefRelation(refRelation);
        switch (type) {
            case "building":
                centerPointRelationsBuilding.add(way);
                break;
            case "natural":
                centerPointRelationsNatural.add(way);
                break;
            case "landuse":
                centerPointRelationsLanduse.add(way);
                break;
            default:
                break;
        }
        indexForCenterPoints++;
    }

    /**
     * Adds a Way object to the appropriate center point relations collection based on the given type.
     *
     * @param way         The Way object to be added to the center point relations.
     * @param type        The type of the Way, which determines the collection it will be added to.
     */
    public void addToCenterWays(Way way, String type){
        switch (type) {
            case "place":
                centerPointWaysPlace.add(way);
                break;
            case "natural":
                centerPointWaysNatural.add(way);
                break;
            case "landuse":
                centerPointWaysLanduse.add(way);
                break;
            case "building":
                centerPointWaysBuilding.add(way);
                break;
            case "road":
                centerPointWaysRoad.add(way);
                break;
            default:
                break;
        }
        indexForCenterPoints++;
    }
}


