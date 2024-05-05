package com.telos.mapofdenmark;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Queue;

public class View {
    Canvas canvas = new Canvas(1120.0, 638.0);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    double x1 = 100;
    double y1 = 100;
    double x2 = 200;
    double y2 = 800;

    double slider_value;

    Affine trans = new Affine();
    Model model;


    boolean dark;

    @FXML
    private Pane mapPane; //This is a reference to the pane over in the FXML file aka the GUI
    @FXML
    private Pane backgroundPane;

    Point2D tempAddressPoint;

    public View(Model model, Stage primaryStage) throws IOException {
        this.model = model;

        primaryStage.setTitle("Map of Denmark");
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/telos/mapofdenmark/235861.png")));
        primaryStage.getIcons().add(image);
        //Loads the GUI from the FXML file
        // creates a new FXML LOADER for the GUI file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI.fxml"));
        // creates a root of the type parent to set scene
        Parent root = loader.load();
        // Creates the controller object from the GUI controller
        Controller controller = loader.getController();
        // intizalise the rest of the controller with the model and view to run commands on
        controller.init(model,this);

        Scene scene = new Scene(root);
        //Looks for the node in the scene graph hiearchy by the ID #mapPane.
        //It then returns and assigns the Pane from the GUI, and keeps a reference to it
        mapPane = (Pane) scene.lookup("#mapPane");
        mapPane.getChildren().add(canvas); //Adds the canvas to the mapPane

        this.slider_value = 50.0;

        //Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
        redraw();
        panToMap(model.minlat, model.minlon, model.maxlat, model.maxlon);
        zoom(0, 0, 10000); // THIS IS SETTING THE ZOOM DYNAMICALLY: zoom(0, 0, canvas.getHeight() / (model.maxlat - model.minlat));
        //Listens for changes done to the width then changes the canvas acordingly
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> resizePanes(primaryStage.getWidth(), primaryStage.getHeight()));
        //Listens for changes done to the Height then changes the canvas acordingly
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> resizePanes(primaryStage.getWidth(), primaryStage.getHeight()));


    }

    void redraw() {
        gc.setTransform(new Affine());
//        if(dark) {
//            gc.setStroke(Color.WHITE);
//            gc.setFill(Color.web("#212F3D"));
//        } else{
//            gc.setFill(Color.WHITE);
//            gc.setStroke(Color.BLACK);
//        }
        gc.setFill(model.cs.getColor("water", dark, "natural"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(0.000005);
        // Logic for drawing ways from KDTree instead of all ways
        Point2D canvasTopLeft =  mousetoModel(-200,-200);
        Point2D canvasBottomRight = mousetoModel(canvas.getWidth() + 200,canvas.getHeight() + 200);
        //Queue<Node> nodesFromKD = model.kdTree.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());


        // Drawing place
        drawPlace(canvasTopLeft, canvasBottomRight);

        // Drawing landuse
        if (slider_value >= 20){
            drawLanduse(canvasTopLeft, canvasBottomRight);
        }

        // Drawing natural
        if (slider_value >= 20){
            drawNatural(canvasTopLeft, canvasBottomRight);
        }

        // Drawing building
        if (slider_value >= 50){
            drawBuilding(canvasTopLeft, canvasBottomRight);
        }


        // Drawing road
        // Slider values are inside the function
        drawRoad(canvasTopLeft, canvasBottomRight);

        for (var line : model.list) {
            line.draw(gc, dark);
        }
        drawPOI();

        if(tempAddressPoint != null){
            drawCircle(tempAddressPoint.getX(), tempAddressPoint.getY());
        }

    }
    private void drawPOI() {
        if (!model.getPointsOfInterest().isEmpty()) {
            for (Point2D pointOfInterest : model.getPointsOfInterest()) {
                System.out.println("Attempted to draw POI at: " + pointOfInterest.getX() + ", " + pointOfInterest.getY());

                // Calculate the visible size of the POI on the canvas
                double x = pointOfInterest.getX();
                double y = pointOfInterest.getY();

                // Draw a filled circle centered on the point
                drawCircle(x,y);
            }
        }
    }

    void drawCircle(double x, double y){
        double radius = 0.0001; // Radius of the circle in pixels
        gc.setFill(Color.BLACK);  // Set the fill color for the circle
        gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        gc.setFill(Color.RED);  // Set the fill color for the circle
        gc.fillOval(x - radius+0.000025, y - radius+0.000025, 1.5 * radius, 1.5 * radius);
    }

    void panToMap(double minlat, double minlon, double maxlat, double maxlon) {
        double midpointLat = (minlat + maxlat) / 2.0;
        double midpointLon = (minlon + maxlon) / 2.0;
        trans.prependTranslation(-0.56 * midpointLon, midpointLat);
        redraw();
    }

    void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();
    }

    private void resizePanes(double resizedWidth, double resizedHeight){
        canvas.setWidth(resizedWidth);
        canvas.setHeight(resizedHeight);
        redraw();
    }

    void togglecolor(boolean a){
        dark = a;
    }

    void zoom(double dx, double dy, double factor) {
        pan(-dx, -dy);
        trans.prependScale(factor, factor);
        pan(dx, dy);
        redraw();
    }

    public Point2D mousetoModel(double lastX, double lastY) {
        try {
            return trans.inverseTransform(lastX, lastY);
        } catch (NonInvertibleTransformException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }

    public Point2D convertGeoCoordsToPixels(double lon, double lat){
        Point2D transformedGeoPoint = trans.transform(lon, lat);

        return transformedGeoPoint;
    }

    public void Current_Slider_value(double value){
        slider_value = value;
    }

    public Point2D getCanvasCenterPoint(){
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;

        Point2D canvasCoordsTransformed = mousetoModel(centerX, centerY);

        return new Point2D(centerX,centerY);
    }

    public void setTempAddressPoint(Double x, Double y) {
        if (x == null || y == null) {
            tempAddressPoint = null;
        } else {
            tempAddressPoint = new Point2D(x, y);
        }
    }

    private void drawPlace(Point2D canvasTopLeft, Point2D canvasBottomRight){
        Queue<Way> waysPlaceNodesFromKD = model.kdTreeWaysPlace.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());

        for (var relation : model.RelationsPlace) {
            relation.Draw(gc,slider_value,dark,"place");
        }

        for (Way placeWay : waysPlaceNodesFromKD) {
            if (placeWay != null && placeWay.getZoom_scale() < slider_value) {
                gc.setStroke(Color.BLACK);
                placeWay.draw(gc, slider_value, dark, model.getColorScheme());
                placeWay.fill(gc, dark, model.getColorScheme(), "place");
            }
        }
    }

    private void drawNatural(Point2D canvasTopLeft, Point2D canvasBottomRight){
        Queue<Way> naturalsNodesFromKD = model.kdTreeNaturals.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());
        Queue<Way> waysNaturalNodesFromKD = model.kdTreeWaysNatural.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());

        for(Way naturalsWay : naturalsNodesFromKD){
            Relation relation = naturalsWay.getRefRelation();
            relation.Draw(gc,slider_value,dark,"natural");
        }

        // Drawing natural ways
        for (Way naturalsWay : waysNaturalNodesFromKD) {
            if (naturalsWay != null && naturalsWay.getZoom_scale() < slider_value) {
                gc.setStroke(Color.BLACK);
                naturalsWay.draw(gc, slider_value, dark, model.getColorScheme());
                naturalsWay.fill(gc, dark, model.getColorScheme(), "natural");
            }
        }
    }

    private void drawLanduse(Point2D canvasTopLeft, Point2D canvasBottomRight){
        Queue<Way> landuseNodesFromKD = model.kdTreeLanduses.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());
        Queue<Way> waysLanduseNodesFromKD = model.kdTreeWaysLanduse.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());

        for(Way landuseWay : landuseNodesFromKD){
            Relation relation = landuseWay.getRefRelation();
            relation.Draw(gc,slider_value,dark,"landuse");
        }

        // Drawing landuse ways
        for (Way landuseWay : waysLanduseNodesFromKD) {
            if (landuseWay != null && landuseWay.getZoom_scale() < slider_value) {
                gc.setStroke(Color.BLACK);
                landuseWay.draw(gc, slider_value, dark, model.getColorScheme());
                landuseWay.fill(gc, dark, model.getColorScheme(), "landuse");
            }
        }

    }

    private void drawBuilding(Point2D canvasTopLeft, Point2D canvasBottomRight){
        Queue<Way> buildingNodesFromKD = model.kdTreeBuildings.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());
        Queue<Way> waysBuildingNodesFromKD = model.kdTreeWaysBuilding.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());

        for(Way buildingWay : buildingNodesFromKD){
            Relation relation = buildingWay.getRefRelation();
            relation.Draw(gc,slider_value,dark,"building");
        }

        // Drawing building ways
        for (Way buildingWay : waysBuildingNodesFromKD) {
            if (buildingWay != null && buildingWay.getZoom_scale() < slider_value) {
                gc.setStroke(Color.BLACK);
                buildingWay.draw(gc, slider_value, dark, model.getColorScheme());
                buildingWay.fill(gc, dark, model.getColorScheme(), "building");
            }
        }
    }

    private void drawRoad(Point2D canvasTopLeft, Point2D canvasBottomRight){
        Queue<Way> waysRoadNodesFromKD = model.kdTreeWaysRoad.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());

        for (Way roadWay : waysRoadNodesFromKD) {
            if (roadWay != null && roadWay.getZoom_scale() < slider_value) {
                gc.setStroke(Color.BLACK);

                if (model.xsmallRoads.contains(((Road) roadWay).getRoadType) && slider_value >= 60) {
                    roadWay.draw(gc, slider_value, dark, model.getColorScheme());
                }
                else if (model.smallRoads.contains(((Road) roadWay).getRoadType()) && slider_value >= 50){
                    roadWay.draw(gc, slider_value, dark, model.getColorScheme());
                }
                else if (model.mediumRoads.contains(((Road) roadWay).getRoadType()) && slider_value >= 40){
                    roadWay.draw(gc, slider_value, dark, model.getColorScheme());
                }
                else if (model.bigRoads.contains(((Road) roadWay).getRoadType()) && slider_value >= 30){
                    roadWay.draw(gc, slider_value, dark, model.getColorScheme());
                }

            }
        }
    }
}