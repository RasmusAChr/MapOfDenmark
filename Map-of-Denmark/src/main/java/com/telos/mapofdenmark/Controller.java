package com.telos.mapofdenmark;


import com.telos.mapofdenmark.TrieClasses.Trie;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;


import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * The controller class is used to manage user interactions but also updates the view accordingly.
 * The controller class is used to handle events such as mouse clicks, keyboard inputs and slider adjustments.
 * The controller interacts with the Model class to perform address searches, calculate distances,
 * manages points of interests, manages suggestions to addresses but also makes them clickable etc.
 */
public class Controller {
    //JavaFX requires a non-parameter constructor to load and run the FXML file.
    // So it needs to stay (even though it's empty)
    public Controller(){}

    double lastX;
    double lastY;
    private Model model;
    private View view;
    private Trie trie;
    private boolean POI_MODE = false;
    @FXML
    private javafx.scene.shape.Line distanceLine;
    @FXML
    private Text distanceLabel;
    @FXML
    private ToggleButton themeToggleBtn;
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
    @FXML
    private TextField searchBar1;
    @FXML
    private ToggleButton ToggleMode;
    @FXML
    private Boolean vehicle;

    @FXML
    private ImageView searchImage;
    private int  searchBarCounter = 0;
    private double distance;

    private Node lastPannedToAddress;

    public boolean switchToRadix = false;

    /**
     * Initializes the Controller objects with the given Model and View.
     * @param inputModel - The Model object
     * @param inputView - The View object
     */
    public void init(Model inputModel, View inputView) {
        this.model = inputModel;
        this.view = inputView;
        suggestionsBox.setVisible(true);
        view.canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();

            if(e.getButton() == MouseButton.PRIMARY && POI_MODE){
                Point2D modelPoint = view.mousetoModel(lastX, lastY);
                model.addPOI(modelPoint);
                view.redraw();
            }
        });
        view.canvas.setOnMouseDragged(e -> {

                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                view.pan(dx, dy);

            lastX = e.getX();
            lastY = e.getY();
        });
    }

    /**
     * Initializes the controller's different components. Also used to manage different events
     * such as managing the different searchbars, the suggestionsbox to add suggestions and make them clickable etc.
     */
    @FXML
    private void initialize(){
        zoomSlider.setValue(50.0);
        vehicle = false;
        // Sets the visuals of the theme toggle
        themeToggleBtn.getStyleClass().add("root-light");

        // Ensure the ImageView starts at the correct position corresponding to the slider's initial value
        updateImageViewPosition(zoomSlider.getValue());
        // Add a listener to the slider's value
        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateImageViewPosition(newVal.doubleValue()
        ));

        // Clickable Suggestions
        suggestionsBox.setOnMouseClicked(event ->{
            String chosenSelection = suggestionsBox.getSelectionModel().getSelectedItem();
            if (chosenSelection != null && !chosenSelection.isEmpty()) {
                // Puts the text into the first searchbar
                if (searchBarCounter == 0) {
                    searchBar.setText(chosenSelection);
                    checkForIfTextIsStreet(searchBar);
                    addressParsing(chosenSelection, searchBar);
                    panToAddress(chosenSelection, true);
                }
                // Puts it into the second searchbar
                else {
                    searchBar1.setText(chosenSelection);
                    checkForIfTextIsStreet(searchBar1);
                    addressParsing(chosenSelection, searchBar1);
                    panToAddress(chosenSelection, false);
                }
            }
        });
        searchImage.setOnMouseClicked(event -> {
            panToAddress(searchBar.getText(), true);
        });

        searchBar.setOnKeyPressed(event -> {
            if (!(event.getCode() == KeyCode.BACK_SPACE) && !(searchBar.getText().isEmpty())) {
                checkForIfTextIsStreet(searchBar);
                addressParsing(searchBar.getText(), searchBar);
                searchBarCounter = 0;
            }
            if(event.getCode() == KeyCode.BACK_SPACE && searchBar.getText().isEmpty()) {
                // Backspace key was pressed and search bar is empty
                view.setTempAddressStartPoint(null, null);
                view.redraw();
            }
        });
        searchBar1.setOnKeyPressed(event -> {
            if (!(event.getCode() == KeyCode.BACK_SPACE) && !(searchBar1.getText().isEmpty())) {
                checkForIfTextIsStreet(searchBar1);
                addressParsing(searchBar1.getText(), searchBar1);
                searchBarCounter = 1;
            }
            if(event.getCode() == KeyCode.BACK_SPACE && searchBar1.getText().isEmpty()) {
                // Backspace key was pressed and search bar is empty
                view.setTempAddressEndPoint(null, null);
                view.redraw();
            }
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

            Point2D lineStart = view.mousetoModel(distanceLine.getStartX(), distanceLine.getStartY());
            Point2D lineEnd = view.mousetoModel(distanceLine.getEndX(), distanceLine.getEndY());
            CalculateDistance(lineStart, lineEnd);
          });
    }

    /**
     * Checks if the text in the given searchbar is a street name.
     * If it is it updates the switchToRadix boolean to indicate a switch to a RadixTrie with full addresses
     * @param modularSearchBar - The given search bar
     */
    private void checkForIfTextIsStreet(TextField modularSearchBar){
        if(model.isWordInTrie(modularSearchBar.getText())){
            switchToRadix = true;
        }
        else{
            switchToRadix = false;
        }
    }

    /**
     * Used to initiate the search process for Dijkstra by inputting text from the second search bar
     */
    @FXML
    private void StartSearch(){
        String input = searchBar1.getText().toLowerCase();
        Node node1 = model.getAddressIdMap().get(input);

        String input1 = searchBar.getText().toLowerCase();
        Node node2 = model.getAddressIdMap().get(input1);

        model.StartDijkstra(node1,node2,vehicle);

        model.list.add(new Line(model.getDijkstraPath(node2)));
        view.redraw();
        System.gc();
    }


    /**
     * Toggles the vehicle mode to either Bike or Car depending on the input
     */
    @FXML
    private void toggleMode(){
        if(ToggleMode.isSelected()){
        vehicle = true;
        } else {
            vehicle = false;
        }
    }

    /**
     * Toggles the theme mode to either dark or light depending on input
     */
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

    /**
     * Toggles the Point of Interest (POI) mode to either true or false depending on input
     */
    @FXML
    private void placeInterest(){
        if(POI_MODE){
            POI_MODE = false;
        } else {
            POI_MODE = true;
        }    }

    /**
     * Updates the position of the image view based on the slider value.
     * @param sliderValue - The current value of the slider
     */
    @FXML
    private void updateImageViewPosition(double sliderValue){
        // This portion changes image the image itself
        // Note this loads the image everytime so it may be faster to store all images in seperate image variables but may cost more memory
        String imagePath;
        if ( distance < 0.1) {
                imagePath = "Earth Emoji.png";
        }else if (distance < 50.0) {
                imagePath = "Sunflower Emoji.png";
        } else if (distance < 400.0) {
                imagePath = "Bicycle Emoji.png";
        } else if (distance < 3500.0) {
                imagePath = "Airplane Emoji.png";
        } else {
                imagePath = "Earth Emoji.png";
        }

        // Load and set the new image
        Image image = new Image(getClass().getResourceAsStream("/com/telos/mapofdenmark/GUI Icons/" + imagePath));
        sliderEmoji.setImage(image);

    }

    /**
     * Parses the address and updates the suggestions box accordingly
     * @param newValue - Text from the searchbar given
     * @param modularSearchBar - the searchbar given
     */
    @FXML
    private void addressParsing(String newValue, TextField modularSearchBar) {
        suggestionsBox.getItems().clear(); // Clear previous suggestions
        checkForIfTextIsStreet(modularSearchBar);
        // Only proceed if the new value is not empty
        if (!newValue.isEmpty()) {
            List<String> suggestionsList;
            if(!switchToRadix){
                // Get new suggestions based on the current text in the search bar
                suggestionsList = model.getStreetNamesList(newValue);
            }
            else{
                suggestionsList = model.getSuggestionList(newValue);
            }


            // Logic for the suggestionsBox in UI
            if (!suggestionsList.isEmpty()) {

                suggestionsBox.setItems(FXCollections.observableArrayList(suggestionsList));
            } else {

            }
        }
    }

    /**
     * Tests encoding with UY by adding a test string to the suggestions box
     */
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

    /**
     * Pans over to the given address, and makes sure that it is centered in the canvas and draws a circle on the given address
     * @param selectedAddress - address given in string that needs to be panned to
     * @param startPoint - Boolean which decides which tempAddressPoint that should be given the coordinates. True = start address point, false = end address point
     */
    private void panToAddress(String selectedAddress, boolean startPoint){
        String addressToLowerCase = selectedAddress.toLowerCase();
        if(model.getAddressIdMap().get(addressToLowerCase) != null && lastPannedToAddress != model.getAddressIdMap().get(addressToLowerCase)){
            Node addressNode = model.getAddressIdMap().get(addressToLowerCase);
            lastPannedToAddress = addressNode;
            double addressX = addressNode.getLon() * 0.56;
            double addressY = -addressNode.getLat();

            Point2D canvasCenterAsPixels = view.getCanvasCenterPoint();

            Point2D addressAsPixels = view.convertGeoCoordsToPixels(addressX, addressY);

            double dx = canvasCenterAsPixels.getX() - addressAsPixels.getX();
            double dy = canvasCenterAsPixels.getY() - addressAsPixels.getY();



            view.drawCircle(addressX,addressY);
            if(startPoint){
                view.setTempAddressStartPoint(addressX,addressY);
            }
            else{
                view.setTempAddressEndPoint(addressX,addressY);
            }
            view.pan(dx, dy);
        }
//        else if (model.getAddressIdMap().get(selectedAddress) == null){
//            System.out.println("Not a valid Address");
//        }
//        else if(lastPannedToAddress == model.getAddressIdMap().get(selectedAddress)){
//            System.out.println("Already panned to this address");
//        }
    }

    /**
     * Calculates the distance between to given points in corresponding to real life distance, and updates the distanceLabel accordingly
     * @param startPoint - start point for the line
     * @param endPoint - end point for the line
     */
    //Inspiration for math formula found at https://www.movable-type.co.uk/scripts/latlong.html
    @FXML
    private void CalculateDistance(Point2D startPoint, Point2D endPoint){
        double lat1 = startPoint.getX();
        double lon1 = startPoint.getY();
        double lat2 = endPoint.getX();
        double lon2 = endPoint.getY();

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        this.distance = distance;

        distanceLabel.setText(String.format("Scale of line : %.0f m", distance));  // Setting the distance text directly formatted
    }


}