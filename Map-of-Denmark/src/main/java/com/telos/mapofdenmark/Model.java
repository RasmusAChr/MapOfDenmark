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
import java.util.*;
import java.util.zip.ZipInputStream;

import javax.xml.stream.*;

import com.telos.mapofdenmark.Objects.*;

public class Model implements Serializable {
    List<Line> list = new ArrayList<Line>();
    List<Way> ways = new ArrayList<Way>();
    List<Natural> zeroLayer = new ArrayList<>(); // coastline
    List<Natural> firstLayer = new ArrayList<>(); // Natural but only islands (background land) https://wiki.openstreetmap.org/wiki/natural
    List<Landuse> secondLayer = new ArrayList<>(); // Landuse https://wiki.openstreetmap.org/wiki/Land_use
    List<Leisure> thirdLayer = new ArrayList<>(); // Leisure https://wiki.openstreetmap.org/wiki/Leisure
    List<Building> fourthLayer = new ArrayList<>(); // https://wiki.openstreetmap.org/wiki/Buildings
    List<Highway> bigRoads = new ArrayList<>();
    List<Highway> mediumRoads = new ArrayList<>();
    List<Highway> smallRoads = new ArrayList<>();
    List<Highway> walkingPaths = new ArrayList<>();

    double minlat, maxlat, minlon, maxlon;

    static Model load(String filename) throws FileNotFoundException, IOException, ClassNotFoundException, XMLStreamException, FactoryConfigurationError {
        if (filename.endsWith(".obj")) {
            try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                return (Model) in.readObject();
            }
        }
        return new Model(filename);
    }

    public Model(String filename) throws XMLStreamException, FactoryConfigurationError, IOException {
        if (filename.endsWith(".osm.zip")) {
            parseZIP(filename);
        } else if (filename.endsWith(".osm")) {
            parseOSM(filename);
        } /*else {
            parseTXT(filename);
        }*/
        save(filename+".obj");
    }
    // Method for saving session as binary file (.OBJ)
    void save(String filename) throws FileNotFoundException, IOException {
        try (var out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }
    // Method for parsing a file if it's in the ZIP format
    private void parseZIP(String filename) throws IOException, XMLStreamException, FactoryConfigurationError {
        var input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        parseOSM(input);
    }
    // Method for parsing the file if it's a OSM file to a FileInputStream, to use in the next parseOSM method
    private void parseOSM(String filename) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        FileInputStream inputStream = new FileInputStream(filename);
        parseOSM(inputStream);
    }

    private void parseOSM(InputStream inputStream) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        var input = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(inputStream));
        // Map for storing node id's for quick access when searching for node ref
        var id2node = new HashMap<Long, Node>();
        var way = new ArrayList<Node>();
        var wayTagKey = "";
        var wayTagValue = "";
        // run this loop until there are no more lines in the .OSM file
        while (input.hasNext()) {
            var tagKind = input.next();
            if (tagKind == XMLStreamConstants.START_ELEMENT) {
                var name = input.getLocalName();
                // If the input is bounds, it runs the parseBounds method to define the bounds of the map
                if (name == "bounds") {
                    parseBounds(input);
                } // If the input is node, get the id, latitude and longitude and put it into a HashMap.
                else if (name == "node") {
                    var id = Long.parseLong(input.getAttributeValue(null, "id"));
                    var lat = Double.parseDouble(input.getAttributeValue(null, "lat"));
                    var lon = Double.parseDouble(input.getAttributeValue(null, "lon"));
                    id2node.put(id, new Node(lat, lon));
                    // If the input is a way, clear the list of way for the new way
                } else if (name == "way") {
                    way.clear();
                    wayTagKey = "";
                    wayTagValue = "";
                } else if (name == "tag") {
                    // set the tag value and tag key
                    var v = input.getAttributeValue(null, "v");
                    var k = input.getAttributeValue(null, "k");
                    // If statement is necessary for the layers, otherwise tags like 'source', 'name' etc. will be used.
                    if (k.equals("natural") || k.equals("landuse") || k.equals("leisure") || k.equals("building")) {
                        wayTagKey = k;
                        wayTagValue = v;
                    }
                    //If the input is a node, get the reference of the node and add it to the way list
                } else if (name == "nd") {
                    var ref = Long.parseLong(input.getAttributeValue(null, "ref"));
                    var node = id2node.get(ref);
                    way.add(node);
                }
                // if the tag is the end element, add the way to the corresponding layer to draw the layers correctly
            } else if (tagKind == XMLStreamConstants.END_ELEMENT) {
                var name = input.getLocalName();
                if (name == "way") {
                    if (wayTagValue.equals("coastline")) {
                        zeroLayer.add(new Natural(way, wayTagValue));
                    }
                    else if (wayTagKey.equals("natural")) { // Måske vi ikke skulle lave et objekt for så mange forskellige ting ligesom TA sagde? - Rasmus
                        firstLayer.add(new Natural(way, wayTagValue));
                    }
                    // else if secondlayer (Landuse)
                    else if (wayTagKey.equals("landuse") && !wayTagValue.equals("port")) {
                        secondLayer.add(new Landuse(way, wayTagValue));
                    }
                    // else if thirdlayer (leisure)
                    else if (wayTagKey.equals("leisure") && !wayTagValue.equals("marina") && !wayTagValue.equals("slipway")) {
                        thirdLayer.add(new Leisure(way, wayTagValue));
                        System.out.println(wayTagValue);
                    }
                    // else if fourthlayer (buildings)
                    // else if bigRoad
                    // else if mediumRoad
                    // else if smallRoad
                    // else if walkingPaths

                    //ways.add(new Way(way));
                }
            }
        }
    }
    // A method to get the minimum & maximum latitude (and vice versa for longitude), to define the bounds of the map
    // The method is used in the parseOSM method
    private void parseBounds(XMLStreamReader input){
        minlat = Double.parseDouble(input.getAttributeValue(null, "minlat"));
        maxlat = Double.parseDouble(input.getAttributeValue(null, "maxlat"));
        minlon = Double.parseDouble(input.getAttributeValue(null, "minlon"));
        maxlon = Double.parseDouble(input.getAttributeValue(null, "maxlon"));
    }

    /*private void parseTXT(String filename) throws FileNotFoundException {
        File f = new File(filename);
        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                list.add(new Line(s.nextLine()));
            }
        }
    }*/

    /*public void add(Point2D p1, Point2D p2) {
        list.add(new Line(p1, p2));
    }*/
}
