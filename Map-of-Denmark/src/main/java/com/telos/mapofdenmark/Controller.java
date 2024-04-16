package com.telos.mapofdenmark;


import com.telos.mapofdenmark.Shortest_Route.EdgeWeightedDigraph;
import com.telos.mapofdenmark.Shortest_Route.SP;
import com.telos.mapofdenmark.TrieClasses.Address;
import com.telos.mapofdenmark.TrieClasses.Trie;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


import javafx.scene.image.ImageView;

public class Controller {
    //JavaFX requires a non-parameter constructor to load and run the FXML file.
    // So it needs to stay (even though it's empty)
    public Controller(){}

    double lastX;
    double lastY;
    private Model model;
    private View view;
    private Trie trie;
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
    @FXML
    private ListView<String> suggestionsBox; // The ListView to display suggestions
    @FXML
    private TextField searchBar;

    public void init(Model inputModel, View inputView) {
        this.model = inputModel;
        this.view = inputView;

        view.canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
        });
        view.canvas.setOnMouseDragged(e -> {

                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                view.pan(dx, dy);

            lastX = e.getX();
            lastY = e.getY();
        });
    }
    @FXML
    private void initialize(){
        zoomSlider.setValue(50.0);
        // Sets the visuals of the theme toggle
        themeToggleBtn.getStyleClass().add("root-light");

// Ensure the ImageView starts at the correct position corresponding to the slider's initial value
        updateImageViewPosition(zoomSlider.getValue());
        // Add a listener to the slider's value
        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateImageViewPosition(newVal.doubleValue()
        ));
//        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
//            addressParsing(trie, newValue);
//        });
       // zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> ));

        searchBar.setOnKeyPressed(event -> {
            if (!(event.getCode() == KeyCode.BACK_SPACE) && !(searchBar.getText().isEmpty())) {
                System.out.println(searchBar.getText());
                addressParsing(trie, searchBar.getText());
            }
            testEncodingWithUI();
        });

        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) ->{
            double deltaFactor = newVal.doubleValue() - oldVal.doubleValue(); // Calculate delta factor

            // Calculate zoom direction based on deltaFactor sign
            int zoomDirection = deltaFactor > 0 ? 1 : -1;

            // Take absolute value of deltaFactor
            deltaFactor = Math.abs(deltaFactor);

            double dy = view.canvas.getHeight() / 2;
            double dx = view.canvas.getWidth() / 2;

            // Apply zoom
            view.zoom(dx, dy, Math.pow(1.07, zoomDirection * deltaFactor));
            view.Current_Slider_value(newVal.doubleValue());
          });
    }
    @FXML
    private void StartSearch(){
        // input address

        model.StartDijstra();
    }

    @FXML
    private void StopSearch(){
        // input address
        model.getDijkstraPath();
                // Returns int or notes?
    }

    @FXML
    private void toggleTheme(){
        // if the themebutton has been toggled on. determines wheter do display in dark or light mode.
        if (themeToggleBtn.isSelected()) {
            themeToggleBtn.getStyleClass().remove("root-light");
            themeToggleBtn.getStyleClass().add("root-dark");
            Dark = true;
            view.togglecolor(Dark);
            view.redraw();
        } else {
            themeToggleBtn.getStyleClass().remove("root-dark");
            themeToggleBtn.getStyleClass().add("root-light");
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
       // double thumbPositionPercentage = (sliderValue - zoomSlider.getMin()) / (zoomSlider.getMax() - zoomSlider.getMin());

        // Adjust the formula to correctly map the value to the Y position
      //  double sliderTrackHeight = zoomSlider.getPrefHeight(); // The full height of the slider

        // Calculate the newY position for the ImageView
        //double newY = zoomSlider.getLayoutY() + sliderTrackHeight * (1 - thumbPositionPercentage) - sliderEmoji.getFitHeight() * thumbPositionPercentage;

       // sliderEmoji.setLayoutY(newY);



        // This portion changes image the image itself
        // Note this loads the image everytime so it may be faster to store all images in seperate image variables but may cost more memory
        String imagePath;
        if (sliderValue > 85.0){
            imagePath = "Sunflower Emoji.png";
        } else if (sliderValue > 60.0) {
            imagePath = "Bicycle Emoji.png";
        } else if (sliderValue > 25.0) {
            imagePath = "Airplane Emoji.png";
        } else {
            imagePath = "Earth Emoji.png";
        }

        // Load and set the new image
        Image image = new Image(getClass().getResourceAsStream("/com/telos/mapofdenmark/GUI Icons/" + imagePath));
        sliderEmoji.setImage(image);

    }

    @FXML
    private void addressParsing(Trie trie, String newValue) {
        suggestionsBox.getItems().clear(); // Clear previous suggestions

        // Used keep track of previous suggestions so no duplication happens
        List<String> previousSuggestions = new ArrayList<>();
        previousSuggestions.clear();

            // Only proceed if the new value is not empty
            if (!newValue.isEmpty()) {
                // Get new suggestions based on the current text in the search bar
                List<String> suggestionsList = model.getSuggestionList(newValue);
                // Print and store new suggestions
                for (String suggestion : suggestionsList) {
                    if (!previousSuggestions.contains(suggestion)) {
                        System.out.println(suggestion);
                        previousSuggestions.add(suggestion);
                    }
                }
                // Logic for the suggestionsBox in UI
                if (!suggestionsList.isEmpty()) {
                    suggestionsBox.setVisible(true);
                    suggestionsBox.setItems(FXCollections.observableArrayList(suggestionsList));
                } else {
                    suggestionsBox.setVisible(false);
                }
            } else {
                suggestionsBox.setVisible(false);
            }
    }


    @FXML
    private void testEncodingWithUI() {
        // Directly set a test string to verify UTF-8 characters are displayed correctly
        //suggestionsBox.getItems().add("Åkirkeby");
        String input = searchBar.getText();
        Node node = model.getAddressIdMap().get(input);
        try {
            suggestionsBox.getItems().add("Lat is: " + node.getLat() + " Lon is: " + node.getLon());
        } catch (NullPointerException E) {}
    }

}