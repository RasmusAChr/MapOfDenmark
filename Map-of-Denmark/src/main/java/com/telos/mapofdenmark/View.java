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
        //Write a system out println for cavas height width and model minlon maxlon minlat and maxlat
        System.out.println("Canvas Width: " + canvas.getWidth() + " Canvas Height: " + canvas.getHeight());
        System.out.println("Model minLon: " + model.getMinlon() + " Model maxLon: " + model.getMaxlon() + " Model minLat: " + model.getMinlat() + " Model maxLat: " + model.getMaxlat());
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
        //pan(-0.56*model.minlon, model.maxlat);
        //zoom(0, 0, canvas.getHeight() / (model.maxlat - model.minlat));
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
        /*
        for (Object nodeSpatial : getNodesFromSpatial()) {
            Node node = (Node) nodeSpatial;
            if (node.getWay() != null) {
                node.getWay().draw(gc);
            }
            //node.getWay().draw(gc);
        }

         */
        //System.out.println("Redrawing, number of ways: " + getNodesFromSpatial().size()); // Debug the count of ways
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



    public Rectangle2D createRectangle(double minLon, double maxLon, double minLat, double maxLat){
        // Calculate the width and height, by subtracting maxLon/maxLat with minLon/minLat
        double width = maxLon - minLon;
        double height = maxLat - minLat;


        return new Rectangle2D(minLon, minLat, width, height);
    }

    public Rectangle2D createCanvasRectangle(double minLon, double maxLon, double minLat, double maxLat, double canvasWidth, double canvasHeight) {
        double width = maxLon - minLon;
        double height = maxLat - minLat;
        double aspectRatio = canvasWidth / canvasHeight;

        if (width / height > aspectRatio) {
            double newHeight = width / aspectRatio;
            double deltaLat = (newHeight - height) / 2;
            minLat -= deltaLat;
            maxLat += deltaLat;
            height = newHeight;
        } else {
            double newWidth = height * aspectRatio;
            double deltaLon = (newWidth - width) / 2;
            minLon -= deltaLon;
            maxLon += deltaLon;
            width = newWidth;
        }

        return new Rectangle2D(minLon, minLat, width, height);
    }

    public void setupAffine(double width, double height, double minLon, double maxLon, double minLat, double maxLat) {
        Rectangle2D mapRect = createRectangle(model.getMinlon(), model.getMaxlon(), model.getMinlat(),model.getMaxlat());

        Point2D mapCenter = calculateCenter(model.getMinlon(), model.getMaxlon(), model.getMinlat(),model.getMaxlat());
        double halfCanvasWidth = width / 2;
        double halfCanvasHeight = height / 2;
        double canvasMinX = mapCenter.getX() - halfCanvasWidth;
        double canvasMaxX = mapCenter.getX() + halfCanvasWidth;
        double canvasMinY = mapCenter.getY() - halfCanvasHeight;
        double canvasMaxY = mapCenter.getY() + halfCanvasHeight;
        Rectangle2D canvasRect = createCanvasRectangle(canvasMinX, canvasMaxX, canvasMinY, canvasMaxY, canvas.getWidth(), canvas.getHeight());

        double scaleX = canvasRect.getWidth() / mapRect.getWidth();
        double scaleY = canvasRect.getHeight() / mapRect.getHeight();
        double translateX = -mapRect.getMinX() * scaleX;
        double translateY = -mapRect.getMinY() * scaleY;

        trans.setToTransform(scaleX, 0, translateX, 0, -scaleY, height - translateY);
    }

    public Point2D calculateCenter(double minLon, double maxLon, double minLat, double maxLat){
        // Calculates center for latitude
        double centerLat = (minLat + maxLat) / 2;

        // Calculates center for longitude
        double centerLon = (minLon + maxLon) / 2;

        return new Point2D(centerLat, centerLon);
    }

}