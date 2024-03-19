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
    List<Natural> firstLayer = new ArrayList<>(); // Natural but only islands (background land) https://wiki.openstreetmap.org/wiki/natural
    List<Landuse> secondLayer = new ArrayList<>(); // Landuse https://wiki.openstreetmap.org/wiki/Land_use
    List<Natural> thirdLayer = new ArrayList<>(); // https://wiki.openstreetmap.org/wiki/natural
    List<Building> fourthLayer = new ArrayList<>(); // https://wiki.openstreetmap.org/wiki/Buildings

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
        FileInputStream inputStream = new FileInputStream(filename);
        parseOSM(inputStream);
    }

    private void parseOSM(InputStream inputStream) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        var input = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(inputStream));
        var id2node = new HashMap<Long, Node>();
        var way = new ArrayList<Node>();
        String objectType = "";
        while (input.hasNext()) {
            var tagKind = input.next();
            if (tagKind == XMLStreamConstants.START_ELEMENT) {
                var name = input.getLocalName();
                if (name == "bounds") {
                    parseBounds(input);
                } else if (name == "node") {
                    var id = Long.parseLong(input.getAttributeValue(null, "id"));
                    var lat = Double.parseDouble(input.getAttributeValue(null, "lat"));
                    var lon = Double.parseDouble(input.getAttributeValue(null, "lon"));
                    id2node.put(id, new Node(lat, lon));
                } else if (name == "way") {
                    way.clear();
                    objectType = "";
                } else if (name == "tag") {
                    var v = input.getAttributeValue(null, "v");
                    if (v.equals("coastline")) {
                        objectType = v;
                    }
                } else if (name == "nd") {
                    var ref = Long.parseLong(input.getAttributeValue(null, "ref"));
                    var node = id2node.get(ref);
                    way.add(node);
                }
            } else if (tagKind == XMLStreamConstants.END_ELEMENT) {
                var name = input.getLocalName();
                if (name == "way") {
                    if (objectType.equals("coastline")) {
                        firstLayer.add(new Natural(way, objectType));
                    }

                    //ways.add(new Way(way));
                }
            }
        }
    }

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
