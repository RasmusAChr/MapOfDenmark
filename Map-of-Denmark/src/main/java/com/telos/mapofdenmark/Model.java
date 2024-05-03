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
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import com.telos.mapofdenmark.KDTreeClasses.KDTree;
import com.telos.mapofdenmark.Shortest_Route.DirectedEdge;
import com.telos.mapofdenmark.Shortest_Route.EdgeWeightedDigraph;
import com.telos.mapofdenmark.Shortest_Route.SP;
import com.telos.mapofdenmark.TrieClasses.Address;
import com.telos.mapofdenmark.TrieClasses.Trie;
import javafx.geometry.Point2D;

public class Model implements Serializable {
    private static final long serialVersionUID = 9300313068198046L;

    List<Line> list = new ArrayList<Line>();
    List<Way> ways = new ArrayList<Way>();
    List<Node> nodeList = new ArrayList<>();
    List<Relation> RelationsPlace = new ArrayList<>();
    List<Relation> RelationsNatural = new ArrayList<>();
    List<Relation> RelationsLanduse = new ArrayList<>();
    List<Relation> RelationsBuilding = new ArrayList<>();

    // Collection used for storing center points such that multiple nodes with same way ref is not used to populate KDTree
    List<Node> centerPointNodes = new ArrayList<>();
    // Collection used for storing points of interest
    List<Point2D> pointsOfInterest = new ArrayList<>();
    // Collection used for storing center points for building relations
    List<Node> centerPointNodesBuilding = new ArrayList<>();
    // Collection used for storing cennter points for natural relations
    List<Node> centerPointNodesNatural = new ArrayList<>();
    List<Node> centerPointNodesLanduse = new ArrayList<>();
    SP Dijkstra = null;
    private Trie trie;
    double minlat, maxlat, minlon, maxlon;
    List<Address> addressList;
    Map<String, Node> addressIdMap;
    // This KDTree holds all ways
    KDTree kdTree;
    // This KDTree holds all building relations
    KDTree kdTreeBuildings;
    // This KDTree holds all building relations
    KDTree kdTreeNaturals;
    KDTree kdTreeLanduses;
    Address address;
    EdgeWeightedDigraph EWD;
   // HashMap<Node, Integer> DigraphNodeToIndex;
  //  HashMap<Integer, Node> DigraphIndexToNode;
    BiMap<Node,Integer> IndexBINode;
    TreeMap<Long, Node> id2node;
    HashMap<String, Double> tagToScaleValue;
    List<Member> relationsMembers;
    HashMap<Long, Way> id2way;
    int roadCount;
    int indexForCenterPoints = 0;
    long wayid = 0;
    Set<String> uniqueWayTypes = new HashSet<>();
    HashMap<String, Double> roadIdSet;
    HashSet<String> cycleTags;
    ColorScheme cs;
    LineThickness lt;
    Set<String> allowedRelationTypes;

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


    public Model(InputStream inputStream, String filename) throws XMLStreamException, FactoryConfigurationError, IOException {
        cs = new ColorScheme();
        lt = new LineThickness();
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
     //   this.DigraphNodeToIndex = new HashMap<>();
      //  this.DigraphIndexToNode = new HashMap<>();
        this.IndexBINode = HashBiMap.create();
        this.tagToScaleValue = new HashMap<>();
        /*
        tagToScaleValue.put("building", 70.0);
        tagToScaleValue.put("shop", 70.0);
        tagToScaleValue.put("barrier", 70.0);
        tagToScaleValue.put("tourism", 70.0);
        tagToScaleValue.put("public_transport", 100.0);
        tagToScaleValue.put("power", 100.0);
        tagToScaleValue.put("tunnel", 100.0);
        tagToScaleValue.put("route", 100.0);
        tagToScaleValue.put("bridge", 50.0);
        tagToScaleValue.put("motorway", 0.1);
        tagToScaleValue.put("primary", 20.0);
        tagToScaleValue.put("secondary", 30.0);
        tagToScaleValue.put("tertiary", 40.0);
        tagToScaleValue.put("railway", 40.0);
        tagToScaleValue.put("living_street", 60.0);
        tagToScaleValue.put("residential", 60.0);
                        case "building":
                            zoom_scale = 80.0;
                        case "building", "barrier", "tourism", "tunnel", "water", "waterway", "area", "route", "shop",
                             "bridge", "power", "railway", "public_transport", "office", "natural", "leisure",
                             "landuse", "cycleway", "footway":
                            //zoom_scale = 0.1;break;*/
        this.id2way = new HashMap<>();
        this.id2node = new TreeMap<>();
        this.relationsMembers = new ArrayList<>();
        this.roadCount = 0;
        this.addressList = new ArrayList<>();
        this.address = new Address();
        this.addressIdMap = new TreeMap<>(); // Used for ref a node id to an adress
        this.allowedRelationTypes = new HashSet<>(Arrays.asList("place", "natural", "landuse", "building"));
        if (filename.endsWith(".osm.zip")) {
            parseZIP(inputStream);
        } else if (filename.endsWith(".osm")) {
            parseRouteNet(inputStream);
        }
        this.trie = new Trie();
        for(Address address : addressList){
            trie.insert(address.getFullAddress());
        }
        this.kdTree = new KDTree();
        this.kdTreeBuildings = new KDTree();
        this.kdTreeNaturals = new KDTree();
        this.kdTreeLanduses = new KDTree();
        for (String s : uniqueWayTypes) System.out.println(s);

        // Populates the KDTree using the centerPointNodes collection such that reference to same way is avoided
        kdTree.populate(centerPointNodes); // tilføjer ikke
        System.out.println("size of building collection: "+centerPointNodesBuilding.size());
        kdTreeBuildings.populate(centerPointNodesBuilding);
       System.out.println("Size of building KDTree: " + kdTreeBuildings.size());
       kdTreeNaturals.populate(centerPointNodesNatural);
       System.out.println("Size of building Naturals: " + kdTreeNaturals.size());
       kdTreeLanduses.populate(centerPointNodesLanduse);
       System.out.println("Size of building Landuse: " + kdTreeLanduses.size());
       save(filename+".obj");
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
                        IndexBINode.put(node, roadCountX);
                        //DigraphNodeToIndex.put(node, roadCountX);
                        //DigraphIndexToNode.put(roadCountX, node);
                        roadCountX++;

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
                        EWD = new EdgeWeightedDigraph(roadCountX);
                        wayid = Long.parseLong(input.getAttributeValue(null,"id"));
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
                        addressIdMap.put(address.getFullAddress().toLowerCase(), id2node.get(addressId));
                        addressId = 0;
                        address = null; // Reset for the next address
                    }

                }
            }
        }
        input.close();
    }

    private void parseRouteNet(InputStream inputStream) throws IOException, FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        parseNodeNet(inputStream);
    }

    void save(String filename) throws FileNotFoundException, IOException {
        try (var out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    private void parseZIP(InputStream inputStream) throws IOException, XMLStreamException, FactoryConfigurationError {
        var input = new ZipInputStream(inputStream);
        input.getNextEntry();
        parseNodeNet(input);
    }

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
        int vertexIndex = -2;
        double zoom_scale = -1.0;
        double max_speed = -1.0;
        String relationKey = "";

        //if (!parsedFirstWay){

        if (input1.getLocalName().equals("way")){
            wayid = Long.parseLong(input1.getAttributeValue(null,"id"));
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
                } else if (!insideRelation && name.equals("tag")) {
                    var v = input1.getAttributeValue(null, "v");
                    var k = input1.getAttributeValue(null, "k");
                    if (tagToScaleValue.containsKey(k)) zoom_scale = tagToScaleValue.get(k);
                    switch (k) {
                        case "highway":
                            if (tagToScaleValue.containsKey(v)) zoom_scale = tagToScaleValue.get(v);
                            roadtype = v;
                            //zoom_scale = 0.0;
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
                            break;
                        case "maxspeed":
                            //max_speed = Double.parseDouble(v); Dum Bornholm way
                    }

                } else if (name.equals("nd")) {
                    var ref = Long.parseLong(input1.getAttributeValue(null, "ref"));
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
                    var Member = new Member(role,ref);
                    Member.setWay(id2way.get(ref));
                    relationsMembers.add(Member);
                } else if (insideRelation && name.equals("tag")) {
                    var k = input1.getAttributeValue(null,"k");
                    var v = input1.getAttributeValue(null,"v");

                    if(k.equals("type")){
                        RelationsType = v;
                    } else if (allowedRelationTypes.contains(k)) {
                        validRelation = true;
                        relationKey = k;
                        relationLandform = v;
                    }
                    /*else if (k.equals("place") || k.equals("building") || k.equals("natural") || k.equals("leisure") ||
                               k.equals("amenity") || k.equals("surface")){
                        relationLandform = v;
                    }*/
                }

            } else if (tagKind == XMLStreamConstants.END_ELEMENT) {
                var name = input1.getLocalName();
                // If you wish to only draw coastline -- if (name == "way" && coast) {
                if (name.equals("way")) {
                    if (!roadtype.isEmpty()) {
                        Road tmpRoad = new Road(way, roadtype, zoom_scale, lt);
                        ways.add(tmpRoad);
                        addToCenterPointNodes(way, tmpRoad, true);
                        id2way.put(wayid,tmpRoad);
                    } else {
                        Way tmpWay = new Way(way, zoom_scale);
                        ways.add(tmpWay);
                        addToCenterPointNodes(way, tmpWay, false);
                        id2way.put(wayid,tmpWay);
                    }
                    // Ensuring that every node has a ref to the way it is apart of
                    for (Node node : way) {
                        if (shouldAdd && vertexIndex > -1) {
                            //Node fromNode = DigraphIndexToNode.get(vertexIndex);
                           // int toIndex = DigraphNodeToIndex.get(node);
                            Node fromNode = IndexBINode.inverse().get(vertexIndex);
                            int toIndex = IndexBINode.get(node);
                            double dist = distance(fromNode.lat, node.lat, fromNode.lon, node.lon);
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
                                if (max_speed > 0) {
                                    weight_car = weight_distance_modifier * (max_speed/50.0);
                                } else {
                                    weight_car = weight_distance_modifier * roadIdSet.get(roadtype);
                                }
                            }
                            if (oneway) {
                                if (onewayBicycle) {
                                    EWD.addEdge(new DirectedEdge(vertexIndex, toIndex, weight_cycle, weight_car ));
                                } else {
                                    EWD.addEdge(new DirectedEdge(vertexIndex, toIndex, weight_cycle, weight_car ));
                                    EWD.addEdge(new DirectedEdge(toIndex, vertexIndex, weight_cycle, 500000.0 ));
                                }
                            } else {
                                EWD.addEdge(new DirectedEdge(vertexIndex, toIndex, weight_cycle, weight_car ));
                                EWD.addEdge(new DirectedEdge(toIndex, vertexIndex, weight_cycle, weight_car ));
                            }

                        }
                      //  vertexIndex = DigraphNodeToIndex.get(node);
                        vertexIndex = IndexBINode.get(node);
                    }
                    way.clear();
                    roadtype = "";
                    shouldAdd = false;
                    drivable = false;
                    cycleable = false;
                    oneway = false;
                    onewayBicycle = false;
                    vertexIndex = -2;
                    access = false;
                    acccesPostBollean = false;
                    insideRelation = false;
                    RelationsType = "";
                    zoom_scale = -1.0;
                    max_speed = -1.0;
                } else if (name.equals("relation") && insideRelation) {
                    vertexIndex = -1;
                    insideRelation = false;
                    if (RelationsType.equals("multipolygon")) {
                        if (validRelation) {
                            if (relationKey.equals("place")) {
                                RelationsPlace.add(new Relation(RelationsType,relationsMembers,relationLandform, cs));
                            } else if (relationKey.equals("building")) {
                                Relation tmpRelation = new Relation(RelationsType, new ArrayList<>(relationsMembers) ,relationLandform,cs);
                                RelationsBuilding.add(tmpRelation);
                                // for loop to find center point from member
                                List<Node> nListe = new ArrayList<>();
                                for(Member member : relationsMembers){
                                    if(member.getWay() != null){
                                        nListe.addAll(member.getWay().getNodes());
                                    }
                                }
                                addToCenterPointNodesRelation(nListe, tmpRelation, "building");
                            } else if (relationKey.equals("natural")) {
                                Relation tmpRelation = new Relation(RelationsType, new ArrayList<>(relationsMembers), relationLandform,cs);
                                RelationsNatural.add(tmpRelation);
                                // for loop to find center point from member
                                List<Node> nListe = new ArrayList<>();
                                for(Member member : relationsMembers){
                                    if(member.getWay() != null){
                                        nListe.addAll(member.getWay().getNodes());
                                    }
                                }
                                addToCenterPointNodesRelation(nListe, tmpRelation, "natural");
                            } else if (relationKey.equals("landuse")) {
                                Relation tmpRelation = new Relation(RelationsType, new ArrayList<>(relationsMembers), relationLandform,cs);
                                RelationsLanduse.add(tmpRelation);
                                // for loop to find center point from member
                                List<Node> nListe = new ArrayList<>();
                                for(Member member : relationsMembers){
                                    if(member.getWay() != null){
                                        nListe.addAll(member.getWay().getNodes());
                                    }
                                }
                                addToCenterPointNodesRelation(nListe, tmpRelation, "landuse");
                            }
                        }
                    }
                }
            }
        }
    }

    private int weightCalculate() {
        //calculate the weight of the edge, based on different factors.

        return 20;
    }

    //Dijkstra implementation
    public void StartDijkstra(Node startaddress,boolean vehicle){
        double x = startaddress.getLon();
        double y = startaddress.getLat();
        Node tmpNode = kdTree.getNearestNeighbor(x,y,true).getWay().getArbitraryNode();
        list.clear();
      //  this.Dijkstra = new SP(EWD,DigraphNodeToIndex.get(tmpNode),vehicle); // this starts the dijkstra search from the index that refferes to a node
        this.Dijkstra = new SP(EWD,IndexBINode.get(tmpNode),vehicle);
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
        Node tmpNode = kdTree.getNearestNeighbor(x,y,true).getWay().getArbitraryNode();

         List<Node> path = new ArrayList<Node>(); // this is everything that needs to be drawn for the path
         HashSet<Node> NodeAdded = new HashSet<Node>();
            for(DirectedEdge i: Dijkstra.pathTo(IndexBINode.get(tmpNode))) {
             if(!NodeAdded.contains(IndexBINode.inverse().get(i.to()))){
                 NodeAdded.add(IndexBINode.inverse().get(i.to()));
                 path.add(IndexBINode.inverse().get(i.to()));
             } else if (!NodeAdded.contains(IndexBINode.inverse().get(i.from()))){
                 NodeAdded.add(IndexBINode.inverse().get(i.from()));
                 path.add(IndexBINode.inverse().get(i.from()));
             }

         }
         return path;
    }

    public void parseAddressFromOSM(String v, String k){
        // Assuming you have a Trie instance called 'trie'
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
    public void addPOI(Point2D POI) {
        pointsOfInterest.add(POI);
//        System.out.println("lon: " + lon + ", lat: " + lat);
//        Node nearestNode = kdTree.getNearestNeighbor(lon, lat, false);
//        if (nearestNode != null) {
////            pointOfInterestList.add(nearestNode);
//            nearestNode.setPointOfInterest(true);
//            kdTree.put(lon, lat, nearestNode);
//        }
    }

    public List<Point2D> getPointsOfInterest() {
        return pointsOfInterest;
    }
    public ColorScheme getColorScheme() {
        return cs;
    }

    // finds the center lat and lon among a collection of nodes
    public void addToCenterPointNodesRelation(List<Node> nodes, Relation refRelation, String type){
        double sumLat = 0;
        double sumLon = 0;
        for(Node node : nodes){
            sumLat += node.getLat();
            sumLon += node.getLon();
        }
        double centerLat = sumLat / nodes.size();
        double centerLon = sumLon / nodes.size();
        Node centeredNode = new Node(indexForCenterPoints, centerLat, centerLon);
        centeredNode.setRefRelation(refRelation);
        switch (type) {
            case "building":
                centerPointNodesBuilding.add(centeredNode);
                break;
            case "natural":
                centerPointNodesNatural.add(centeredNode);
            case "landuse":
                centerPointNodesLanduse.add(centeredNode);
            default:
                break;
        }
        indexForCenterPoints++;
    }
}


