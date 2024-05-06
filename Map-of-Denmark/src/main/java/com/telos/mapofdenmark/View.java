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

/**
 * The View class represents the graphical view of the Map of Denmark application.
 * It handles the visualization of map data and user interactions.
 */
public class View {
    Canvas canvas = new Canvas(1120.0, 638.0);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    double slider_value;
    Affine trans = new Affine();
    Model model;
    boolean dark;

    @FXML
    private Pane mapPane; //This is a reference to the pane over in the FXML file aka the GUI

    Point2D tempAddressStartPoint;
    Point2D tempAddressEndPoint;

    /**
     * Constructs a View object.
     * @param model - The model of the application
     * @param primaryStage - The primary stage of the application
     * @throws IOException - If an Input/Output error occurs
     */
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
        zoom(0, 0, 10000);
        //Listens for changes done to the width then changes the canvas acordingly
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> resizePanes(primaryStage.getWidth(), primaryStage.getHeight()));
        //Listens for changes done to the Height then changes the canvas acordingly
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> resizePanes(primaryStage.getWidth(), primaryStage.getHeight()));
    }
    /**
     * Redraws the entire map when called with relevant data such as ways and relations.
     */
    void redraw() {
        gc.setTransform(new Affine());
        gc.setFill(model.cs.getColor("water", dark, "natural"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(0.000005);
        // Logic for drawing ways from KDTree instead of all ways
        Point2D canvasTopLeft =  mousetoModel(-400,-400);
        Point2D canvasBottomRight = mousetoModel(canvas.getWidth() + 400,canvas.getHeight() + 400);

        // Drawing place
        drawPlace(canvasTopLeft, canvasBottomRight);
        // Drawing landuse
        if (slider_value >= 60){
            drawLanduse(canvasTopLeft, canvasBottomRight);
        }
        // Drawing natural
        if (slider_value >= 50){
            drawNatural(canvasTopLeft, canvasBottomRight);
        }
        // Drawing building
        if (slider_value >= 70){
            drawBuilding(canvasTopLeft, canvasBottomRight);
        }
        // Drawing road
        // Slider values are inside the function
        drawRoad(canvasTopLeft, canvasBottomRight);
        for (var line : model.list) {
            line.draw(gc, dark, slider_value);
        }
        drawPOI();
        if(tempAddressStartPoint != null){
            drawCircle(tempAddressStartPoint.getX(), tempAddressStartPoint.getY());
        }
        if(tempAddressEndPoint != null){
            drawCircle(tempAddressEndPoint.getX(), tempAddressEndPoint.getY());
        }
    }

    /**
     * Draws points of interests using the {@link #drawCircle(double, double)} method.
     * It collects the points stored inside the Model class using a getter method and draws circles from them when called
     */
    private void drawPOI() {
        if (!model.getPointsOfInterest().isEmpty()) {
            for (Point2D pointOfInterest : model.getPointsOfInterest()) {
                // Calculate the visible size of the POI on the canvas
                double x = pointOfInterest.getX();
                double y = pointOfInterest.getY();
                // Draw a filled circle centered on the point
                drawCircle(x,y);
            }
        }
    }
    /**
     * Draws a circle on the canvas on the given coordinates when called
     * @param x - The x-coordinate of the center of the circle
     * @param y - The y-coordinate of the center of the circle
     */
    void drawCircle(double x, double y){
        double radius = 0.0001; // Radius of the circle in pixels
        gc.setFill(Color.BLACK);  // Set the fill color for the circle
        gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        gc.setFill(Color.RED);  // Set the fill color for the circle
        gc.fillOval(x - radius+0.000025, y - radius+0.000025, 1.5 * radius, 1.5 * radius);
    }
    /**
     * Pans the view to a specified part of the map defined by the given coordinates
     * @param minlat - The minimum latitude of the map area
     * @param minlon - The minimum longitude of the map area
     * @param maxlat - The maximum latitude of the map area
     * @param maxlon - The maximum longitude of the map area
     */
    void panToMap(double minlat, double minlon, double maxlat, double maxlon) {
        double midpointLat = (minlat + maxlat) / 2.0;
        double midpointLon = (minlon + maxlon) / 2.0;
        trans.prependTranslation(-0.56 * midpointLon, midpointLat);
        redraw();
    }

    /**
     * Pans the view by a given amount given in horizontal & vertical distance
     * @param dx - The horizontal distance to pan
     * @param dy - The vertical distance to pan
     */
    void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();
    }

    /**
     * Resizes the panes used in the application when called.
     * @param resizedWidth - The new width of the panes
     * @param resizedHeight - The height of the panes
     */
    private void resizePanes(double resizedWidth, double resizedHeight){
        canvas.setWidth(resizedWidth);
        canvas.setHeight(resizedHeight);
        redraw();
    }

    /**
     * Toggles the color scheme of the map.
     * @param a - Boolean value: true = dark mode color scheme, false = light mode color scheme
     */
    void togglecolor(boolean a){
        dark = a;
    }

    /**
     * Zoom method that zooms the view by given factors.
     * @param dx - The horizontal distance for zooming
     * @param dy - The vertical distance for zooming
     * @param factor - The zoom factor
     */
    void zoom(double dx, double dy, double factor) {
        pan(-dx, -dy);
        trans.prependScale(factor, factor);
        pan(dx, dy);
        redraw();
    }

    /**
     * Converts pixel coordinates to geographical coordinates
     * @param lastX - The x-coordinate in pixel coordinates
     * @param lastY - The y-coordinate in pixel coordinates
     * @return - The corresponding coordinates in geographical coordinates
     */
    public Point2D mousetoModel(double lastX, double lastY) {
        try {
            return trans.inverseTransform(lastX, lastY);
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts geographical coordinates to pixels on the canvas.
     * @param lon - The longitude(x) coordinate
     * @param lat - The latitude(y) coordinate
     * @return - Corresponding geographical coordinates in pixels
     */
    public Point2D convertGeoCoordsToPixels(double lon, double lat){
        return trans.transform(lon, lat);
    }

    /**
     * Sets the current value of the slider.
     * @param value - The value of the slider
     */
    public void Current_Slider_value(double value){
        slider_value = value;
    }

    /**
     * Gets the center point of the canvas in pixel coordinates
     * @return - The center point of the canvas in pixel coordinates
     */
    public Point2D getCanvasCenterPoint(){
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;

        return new Point2D(centerX,centerY);
    }

    public void setTempAddressStartPoint(Double x, Double y) {
        if (x == null || y == null) {
            tempAddressStartPoint = null;
        } else {
            tempAddressStartPoint = new Point2D(x, y);
        }
    }
    public void setTempAddressEndPoint(Double x, Double y) {
        if (x == null || y == null) {
            tempAddressEndPoint = null;
        } else {
            tempAddressEndPoint = new Point2D(x, y);
        }
    }

    /**
     * Used to draw place relations on the canvas.
     * @param canvasTopLeft - The top-left corner of the canvas
     * @param canvasBottomRight - The bottom-right corner of the canvas
     */
    private void drawPlace(Point2D canvasTopLeft, Point2D canvasBottomRight){
        Queue<Way> waysPlaceNodesFromKD = model.kdTreeWaysPlace.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());

        for (var relation : model.RelationsPlace) {
            relation.Draw(gc,slider_value,dark,"place");
        }

        for (Way placeWay : waysPlaceNodesFromKD) {
            if (placeWay != null) {
                gc.setStroke(Color.BLACK);
                placeWay.draw(gc, slider_value, dark, model.getColorScheme());
                placeWay.fill(gc, dark, model.getColorScheme(), "place");
            }
        }
    }

    /**
     * Draws natural relations onto the canvas
     * @param canvasTopLeft - The top-left corner of the canvas
     * @param canvasBottomRight - The bottom-right corner of the canvas
     */
    private void drawNatural(Point2D canvasTopLeft, Point2D canvasBottomRight){
        Queue<Way> naturalsNodesFromKD = model.kdTreeNaturals.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());
        Queue<Way> waysNaturalNodesFromKD = model.kdTreeWaysNatural.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());

        for(Way naturalsWay : naturalsNodesFromKD){
            Relation relation = naturalsWay.getRefRelation();
            relation.Draw(gc,slider_value,dark,"natural");
        }

        // Drawing natural ways
        for (Way naturalsWay : waysNaturalNodesFromKD) {
            if (naturalsWay != null) {
                gc.setStroke(Color.BLACK);
                naturalsWay.draw(gc, slider_value, dark, model.getColorScheme());
                naturalsWay.fill(gc, dark, model.getColorScheme(), "natural");
            }
        }
    }

    /**
     * Draws Landuse relations onto the canvas
     * @param canvasTopLeft - The top-left corner of the canvas
     * @param canvasBottomRight - The bottom-right corner of the canvas
     */
    private void drawLanduse(Point2D canvasTopLeft, Point2D canvasBottomRight){
        Queue<Way> landuseNodesFromKD = model.kdTreeLanduses.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());
        Queue<Way> waysLanduseNodesFromKD = model.kdTreeWaysLanduse.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());

        for(Way landuseWay : landuseNodesFromKD){
            Relation relation = landuseWay.getRefRelation();
            relation.Draw(gc,slider_value,dark,"landuse");
        }

        // Drawing landuse ways
        for (Way landuseWay : waysLanduseNodesFromKD) {
            if (landuseWay != null) {
                gc.setStroke(Color.BLACK);
                landuseWay.draw(gc, slider_value, dark, model.getColorScheme());
                landuseWay.fill(gc, dark, model.getColorScheme(), "landuse");
            }
        }

    }

    /**
     * Draws Building relations onto the canvas
     * @param canvasTopLeft - The top-left corner of the canvas
     * @param canvasBottomRight - The bottom-right corner of the canvas
     */
    private void drawBuilding(Point2D canvasTopLeft, Point2D canvasBottomRight){
        Queue<Way> buildingNodesFromKD = model.kdTreeBuildings.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());
        Queue<Way> waysBuildingNodesFromKD = model.kdTreeWaysBuilding.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());

        for(Way buildingWay : buildingNodesFromKD){
            Relation relation = buildingWay.getRefRelation();
            relation.Draw(gc,slider_value,dark,"building");
        }

        // Drawing building ways
        for (Way buildingWay : waysBuildingNodesFromKD) {
            if (buildingWay != null) {
                gc.setStroke(Color.BLACK);
                buildingWay.draw(gc, slider_value, dark, model.getColorScheme());
                buildingWay.fill(gc, dark, model.getColorScheme(), "building");
            }
        }
    }

    /**
     * Draws road relations onto the canvas
     * @param canvasTopLeft - The top-left corner of the canvas
     * @param canvasBottomRight - The bottom-right corner of the canvas
     */
    private void drawRoad(Point2D canvasTopLeft, Point2D canvasBottomRight){
        Queue<Way> waysRoadNodesFromKD = model.kdTreeWaysRoad.rangeSearch(canvasTopLeft.getX(), canvasBottomRight.getX(), canvasTopLeft.getY(), canvasBottomRight.getY());

        for (Way roadWay : waysRoadNodesFromKD) {
            if (roadWay != null) {
                gc.setStroke(Color.BLACK);

                if (model.xsmallRoads.contains(((Road) roadWay).getRoadType()) && slider_value >= 80) {
                    roadWay.draw(gc, slider_value, dark, model.getColorScheme());
                }
                else if (model.smallRoads.contains(((Road) roadWay).getRoadType()) && slider_value >= 70){
                    roadWay.draw(gc, slider_value, dark, model.getColorScheme());
                }
                else if (model.mediumRoads.contains(((Road) roadWay).getRoadType()) && slider_value >= 20){
                    roadWay.draw(gc, slider_value, dark, model.getColorScheme());
                }
                else if (model.bigRoads.contains(((Road) roadWay).getRoadType()) && slider_value >= 10){
                    roadWay.draw(gc, slider_value, dark, model.getColorScheme());
                }
            }
        }
    }
}