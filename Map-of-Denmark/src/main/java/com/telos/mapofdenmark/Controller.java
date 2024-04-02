package com.telos.mapofdenmark;


import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;

public class Controller {
    //JavaFX requires a non-parameter constructor to load and run the FXML file.
    // So it needs to stay (even though it's empty)
    public Controller(){}

    double lastX;
    double lastY;
    private Model model;
    private View view;

    @FXML
    private Pane mapPane; //This is a reference to the pane over in the FXML file aka the GUI
    @FXML
    private Pane backgroundPane;

    @FXML
    private ToggleButton themeToggleBtn;
    @FXML
    private Button fileBtn;
    @FXML
    private Button markerBtn;
    @FXML
    private Slider zoomSlider;
    @FXML
    private ImageView sliderEmoji;
    @FXML
    private boolean Dark;
    public void init(Model inputModel, View inputView) {
        this.model = inputModel;
        this.view = inputView;
        view.canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
            //view.resizecanvas(this); // change
        });
        view.canvas.setOnMouseDragged(e -> {

                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                view.pan(dx, dy);



            lastX = e.getX();
            lastY = e.getY();
        });
        view.canvas.setOnScroll(e -> {
            double factor = e.getDeltaY();
            view.zoom(e.getX(), e.getY(), Math.pow(1.01, factor));

        });

    }
    @FXML
    private void initialize(){
        // Sets the visuals of the theme toggle
        themeToggleBtn.getStyleClass().add("root-light");

// Ensure the ImageView starts at the correct position corresponding to the slider's initial value
        updateImageViewPosition(zoomSlider.getValue());

        // Add a listener to the slider's value
        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateImageViewPosition(newVal.doubleValue()));
    }

    @FXML
    private void toggleTheme(){
        // if the themebutton has been toggled on
        if (themeToggleBtn.isSelected()) {
            themeToggleBtn.getStyleClass().remove("root-light");
            themeToggleBtn.getStyleClass().add("root-dark");
            System.out.println("Dark theme");
            Dark = true;
            view.togglecolor(Dark);
            view.redraw();
        } else {
            themeToggleBtn.getStyleClass().remove("root-dark");
            themeToggleBtn.getStyleClass().add("root-light");
            System.out.println("Light theme");
            Dark = false;
            view.togglecolor(Dark);
            view.redraw();
        }
    }
    @FXML
    private void readFile(){
        System.out.println("Attempting to show read file dialog");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open .Obj or .OSM File");

        // Set extension filters such that the user cannot choose undesird file types/extensions
        FileChooser.ExtensionFilter osmFilter = new FileChooser.ExtensionFilter("OSM files (*.osm)", "*.osm");
        FileChooser.ExtensionFilter objFilter = new FileChooser.ExtensionFilter("OBJ files (*.obj)", "*.obj");
        fileChooser.getExtensionFilters().addAll(osmFilter, objFilter);

        // This retrieves the Stage from this component's scene
        Stage stage = (Stage) fileBtn.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String userFile = file.getPath();
            System.out.println(file.getName());
        } else  {
            System.out.println("error"); // or something else
        }

    }
    @FXML
    private void placeInterest(){
        System.out.println("You clicked the interest button");
    }
    @FXML
    public double getPanWidth(){
       return backgroundPane.getWidth();
    }
    @FXML
    public double getPanHeight(){
        return backgroundPane.getHeight();
    }
    @FXML
    private void updateImageViewPosition(double sliderValue){
        // This portion changes the location of imageview
        // Calculate the percentage position of the thumb on the slider
        double thumbPositionPercentage = (sliderValue - zoomSlider.getMin()) / (zoomSlider.getMax() - zoomSlider.getMin());

        // Adjust the formula to correctly map the value to the Y position
        double sliderTrackHeight = zoomSlider.getPrefHeight(); // The full height of the slider

        // Calculate the newY position for the ImageView
        double newY = zoomSlider.getLayoutY() + sliderTrackHeight * (1 - thumbPositionPercentage) - sliderEmoji.getFitHeight() * thumbPositionPercentage;

        sliderEmoji.setLayoutY(newY);



        // This portion changes image the image itself
        // Note this loads the image everytime so it may be faster to store all images in seperate image variables but may cost more memory
        String imagePath;
        if (sliderValue < 25) {
            imagePath = "Sunflower Emoji.png";
        } else if (sliderValue < 50) {
            imagePath = "Bicycle Emoji.png";
        } else if (sliderValue < 75) {
            imagePath = "Airplane Emoji.png";
        } else {
            imagePath = "Earth Emoji.png";
        }

        // Load and set the new image
        Image image = new Image(getClass().getResourceAsStream("/com/telos/mapofdenmark/GUI Icons/" + imagePath));
        sliderEmoji.setImage(image);
    }
}