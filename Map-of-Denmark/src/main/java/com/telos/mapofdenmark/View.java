package com.telos.mapofdenmark;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
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
import com.telos.mapofdenmark.Node;

import javax.swing.text.Position;
import java.io.IOException;
import java.util.Objects;
import java.util.Queue;

public class View {
    Canvas canvas = new Canvas(1091.0, 638.0);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    double x1 = 100;
    double y1 = 100;
    double x2 = 200;
    double y2 = 800;

    Affine trans = new Affine();

    Model model;

    boolean dark;

    @FXML
    private Pane mapPane; //This is a reference to the pane over in the FXML file aka the GUI
    @FXML
    private Pane backgroundPane;

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
        setupAffine(canvas.getWidth(), canvas.getHeight(), model.getMinlon(), model.getMaxlon(),model.getMinlat(), model.getMaxlat());

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
        if(dark) {
            gc.setStroke(Color.WHITE);
            gc.setFill(Color.LIGHTGRAY);
        } else{
            gc.setFill(Color.WHITE);
            gc.setStroke(Color.BLACK);
        }
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1/Math.sqrt(trans.determinant()));
        /*for (var line : model.list) {
            line.draw(gc);
        }

        for (var way : model.ways) {
            way.draw(gc);
        }*/

        for (Object nodeSpatial : getNodesFromSpatial()) {
            Node node = (Node) nodeSpatial;
            if (node.getWay() != null) {
                node.getWay().draw(gc);
            }
            //node.getWay().draw(gc);
        }
        System.out.println("Redrawing, number of ways: " + getNodesFromSpatial().size()); // Debug the count of ways

//        for (Node node : model.kdTree.rangeSearch(minlon, maxlon, minlat, maxlat)) {
//            Point2D point = model.convertToCoordinates(new Point2D(node.getLon(), node.getLat()), false, trans);
//            gc.lineTo(point.getX(), point.getY());
//            System.out.println("Drawing node at: " + point.getX() + ", " + point.getY());
//        }
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

    public Rectangle2D getCanvasCoordAsGeoCoord(){
//        // Gets the canvas coordinates for top left coordinate and bottom right coordinate
//        Point2D mapPaneTopLeft = mapPane.sceneToLocal(canvas.localToScene(x1,y1));
//        Point2D mapPaneBottomRight = mapPane.sceneToLocal(canvas.localToScene(x2,y2));
//
//        // Converts top left and bottom right coordinates to geo coordinates using the model's method
//        Point2D geoTopLeft = model.convertToCoordinates(mapPaneTopLeft, true, trans);
//        Point2D geoBottomRight = model.convertToCoordinates(mapPaneBottomRight, true, trans);
//
//        //System.out.println("Canvas to Geo Coordinates x1 and y1: " + model.convertToCoordinates(mapPaneTopLeft, true, trans));
//        //System.out.println("Canvas to Geo Coordinates x2 and y2: " + model.convertToCoordinates(mapPaneBottomRight, true, trans));
//
//        // Creates a Rectangle2D object with the geo coordinates
//        return new Rectangle2D(geoTopLeft.getX(), geoTopLeft.getY(),(geoBottomRight.getX() - geoTopLeft.getX()), (geoBottomRight.getY() - geoTopLeft.getY()));

        Point2D mapPaneTopLeft = mapPane.sceneToLocal(canvas.localToScene(x1, y1));
        Point2D mapPaneBottomRight = mapPane.sceneToLocal(canvas.localToScene(x2, y2));

        Point2D geoTopLeft = model.convertToCoordinates(mapPaneTopLeft, true, trans);
        Point2D geoBottomRight = model.convertToCoordinates(mapPaneBottomRight, true, trans);

        double minX = Math.min(geoTopLeft.getX(), geoBottomRight.getX());
        double minY = Math.min(geoTopLeft.getY(), geoBottomRight.getY());
        double width = Math.abs(geoBottomRight.getX() - geoTopLeft.getX());
        double height = Math.abs(geoBottomRight.getY() - geoTopLeft.getY());

        System.out.println("Geo Coordinates - TopLeft: " + geoTopLeft + ", BottomRight: " + geoBottomRight);
        System.out.println("Calculated Rectangle - MinX: " + minX + ", MinY: " + minY + ", Width: " + width + ", Height: " + height);

        return new Rectangle2D(minX, minY, width, height);
    }

    public Queue<Node> getNodesFromSpatial(){
        /*System.out.println("MinX: " + getCanvasCoordAsGeoCoord().getMinX());
        System.out.println("MinY: " + getCanvasCoordAsGeoCoord().getMinY());
        System.out.println("Width: " + getCanvasCoordAsGeoCoord().getWidth());
        System.out.println("Height: " + getCanvasCoordAsGeoCoord().getHeight());*/
        Rectangle2D bounds = getCanvasCoordAsGeoCoord();
        Queue<Node> nodes = model.kdTree.rangeSearch(bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(), bounds.getMaxY());
        return nodes;
        /*return model.kdTree.rangeSearch(getCanvasCoordAsGeoCoord().getMinX(),
                                        getCanvasCoordAsGeoCoord().getWidth(),
                                        getCanvasCoordAsGeoCoord().getMinY(),
                                        getCanvasCoordAsGeoCoord().getHeight());
        //return model.kdTree.rangeSearch(0,40,0,40);
         */
    }

    public void setupAffine(double width, double height, double minLon, double maxLon, double minLat, double maxLat){
//        double scaleX = width/ (maxLon - minLon);
//        double scaleY = height / (maxLat - minLat);
//        trans = new Affine();
//        trans.appendScale(scaleX, -scaleY);
//        trans.appendTranslation(-minLon * scaleX, -minLat * scaleY);
//        System.out.println("Affine Transform Set: " + trans);

        double scaleX = width / (maxLon - minLon);
        double scaleY = height / (maxLat - minLat);

        Point2D mapCenter = calculateCenter(model.getMinlon(), model.getMaxlon(),model.getMinlat(), model.getMaxlat());

        double translateX = width / 2 - mapCenter.getX() * scaleX;
        double translateY = width / 2 - mapCenter.getY() * scaleY;

        // Reset transformations for testing without extreme offsets
        trans = new Affine();
        trans.appendScale(scaleX, -scaleY); // Negate scaleY to flip Y-axis
        trans.appendTranslation(-minLon * scaleX, -maxLat * scaleY);
        System.out.println("Affine Transform Set: " + trans);
        Position position;
    }

    public Point2D calculateCenter(double minLon, double maxLon, double minLat, double maxLat){
        // Calculates center for latitude
        double centerLat = (minLat + maxLat) / 2;

        // Calculates center for longitude
        double centerLon = (minLon + maxLon) / 2;

        return new Point2D(centerLat, centerLon);
    }

}