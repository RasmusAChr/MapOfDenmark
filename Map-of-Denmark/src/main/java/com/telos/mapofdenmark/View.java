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

        //Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
        redraw();
        pan(-0.56*model.minlon, model.maxlat);
        zoom(0, 0, canvas.getHeight() / (model.maxlat - model.minlat));
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
        Queue<Node> nodesFromKD = model.kdTree.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());
        Queue<Node> buildingNodesFromKD = model.kdTreeBuildings.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());
        Queue<Node> naturalsNodesFromKD = model.kdTreeNaturals.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());
        Queue<Node> landuseNodesFromKD = model.kdTreeLanduses.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());

        Queue<Node> waysPlaceNodesFromKD = model.kdTreeWaysPlace.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());
        Queue<Node> waysNaturalNodesFromKD = model.kdTreeWaysNatural.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());
        Queue<Node> waysLanduseNodesFromKD = model.kdTreeWaysLanduse.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());
        Queue<Node> waysBuildingNodesFromKD = model.kdTreeWaysBuilding.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());
        Queue<Node> waysRoadNodesFromKD = model.kdTreeWaysRoad.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());

        // Draws place relations (land border)
        for (var relation : model.RelationsPlace) {
            relation.Draw(gc,slider_value,dark,"place");
        }

        // Drawing place ways
        for (Node placeNode : waysPlaceNodesFromKD) {
            Way way = placeNode.getWay();
            if (way != null && way.getZoom_scale() < slider_value) {
                gc.setStroke(Color.BLACK);
                way.draw(gc, slider_value, dark, model.getColorScheme());
                way.fill(gc, dark, model.getColorScheme(), "place");
            }
        }

        // Drawing natural relations
        for(Node naturalsNode : naturalsNodesFromKD){
            Relation relation = naturalsNode.getRefRelation();
            relation.Draw(gc,slider_value,dark,"natural");
        }

        // Drawing natural ways
        for (Node naturalsNode : waysNaturalNodesFromKD) {
            Way way = naturalsNode.getWay();
            if (way != null && way.getZoom_scale() < slider_value) {
                gc.setStroke(Color.BLACK);
                way.draw(gc, slider_value, dark, model.getColorScheme());
                way.fill(gc, dark, model.getColorScheme(), "natural");
            }
        }

        // Drawing landuse relations
        for(Node landuseNode : landuseNodesFromKD){
            Relation relation = landuseNode.getRefRelation();
            relation.Draw(gc,slider_value,dark,"landuse");
        }

        // Drawing landuse ways
        for (Node landuseNode : waysLanduseNodesFromKD) {
            Way way = landuseNode.getWay();
            if (way != null && way.getZoom_scale() < slider_value) {
                gc.setStroke(Color.BLACK);
                way.draw(gc, slider_value, dark, model.getColorScheme());
                way.fill(gc, dark, model.getColorScheme(), "landuse");
            }
        }

        // Drawing building relations
        for(Node buildingNode : buildingNodesFromKD){
            Relation relation = buildingNode.getRefRelation();
            relation.Draw(gc,slider_value,dark,"building");
        }

        // Drawing building ways
        for (Node buildingNode : waysBuildingNodesFromKD) {
            Way way = buildingNode.getWay();
            if (way != null && way.getZoom_scale() < slider_value) {
                gc.setStroke(Color.BLACK);
                way.draw(gc, slider_value, dark, model.getColorScheme());
                way.fill(gc, dark, model.getColorScheme(), "building");
            }
        }

        // Drawing road ways
        for (Node roadNode : waysRoadNodesFromKD) {
            Way way = roadNode.getWay();
            if (way != null && way.getZoom_scale() < slider_value) {
                gc.setStroke(Color.BLACK);
                way.draw(gc, slider_value, dark, model.getColorScheme());
            }
        }


        // Drawing ways
        /*for (Node nodeSpatial : nodesFromKD) {
            Way way = nodeSpatial.getWay();
            if (way != null && way.getZoom_scale() < slider_value) {
                gc.setStroke(Color.BLACK);
                way.draw(gc, slider_value, dark, model.getColorScheme());
            }
        }*/



//        // Drawing relations
//        for (var relation : model.RelationsPlace) {
//            relation.Draw(gc,slider_value,dark);
//        }
//        for (var relation : model.RelationsBuilding) {
//            relation.Draw(gc,slider_value,dark);
//        }
//        for (var relation : model.RelationsNatural) {
//            relation.Draw(gc,slider_value,dark);
//        }


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
}