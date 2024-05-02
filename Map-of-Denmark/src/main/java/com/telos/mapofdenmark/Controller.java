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
    private Pane mapPane; //This is a reference to the pane over in the FXML file aka the GUI
    @FXML
    private Pane backgroundPane;
    @FXML
    private javafx.scene.shape.Line distanceLine;
    @FXML
    private Text distanceLabel;
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
    @FXML
    private TextField searchBar1;
    @FXML
    private ToggleButton ToggleMode;
    @FXML
    private Boolean vehicle;

    @FXML
    private ImageView searchImage;
    private int  searchBarCounter = 0;

    private Node lastPannedToAddress;

    public void init(Model inputModel, View inputView) {
        this.model = inputModel;
        this.view = inputView;

        view.canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();

            if(e.getButton() == MouseButton.PRIMARY && POI_MODE){
                System.out.println("Point called");
                System.out.println("lastX: " + lastX + " lastY: " + lastY);
                Point2D modelPoint = view.mousetoModel(lastX, lastY);
//                System.out.println(modelPoint.getX()+","+modelPoint.getY());
                model.addPOI(modelPoint);
                view.redraw();
            }
        });
        view.canvas.setOnMouseDragged(e -> {

                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
            System.out.println("x is: " + dx + " and y is: " + dy);
                view.pan(dx, dy);

            lastX = e.getX();
            lastY = e.getY();
        });
    }
    @FXML
    private void initialize(){
        vehicle = false;
        zoomSlider.setValue(50.0);
        // Sets the visuals of the theme toggle
        themeToggleBtn.getStyleClass().add("root-light");

        // Ensure the ImageView starts at the correct position corresponding to the slider's initial value
        updateImageViewPosition(zoomSlider.getValue());
        // Add a listener to the slider's value
        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateImageViewPosition(newVal.doubleValue()
        ));
        suggestionsBox.setOnMouseClicked(event ->{
            if(!suggestionsBox.getSelectionModel().getSelectedItem().isEmpty()){
                String chosenSelection = suggestionsBox.getSelectionModel().getSelectedItem();
                // Puts the text into the first searchbar
                if(searchBarCounter == 0){
                    searchBar.setText(chosenSelection);
                    panToAddress(chosenSelection);
                }
                // Puts it into the second searchbar
                else{
                    searchBar1.setText(chosenSelection);
                }
                suggestionsBox.setVisible(false);
            }

        });

        searchImage.setOnMouseClicked(event -> {
            panToAddress(searchBar.getText());
        });

        searchBar.setOnKeyPressed(event -> {
            if (!(event.getCode() == KeyCode.BACK_SPACE) && !(searchBar.getText().isEmpty())) {
                System.out.println(searchBar.getText());
                addressParsing(trie, searchBar.getText());
                searchBarCounter = 0;
            }
        });
        searchBar1.setOnKeyPressed(event -> {
            if (!(event.getCode() == KeyCode.BACK_SPACE) && !(searchBar1.getText().isEmpty())) {
                System.out.println(searchBar1.getText());
                addressParsing(trie, searchBar1.getText());
                searchBarCounter = 1;
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
    @FXML
    private void handleSearch() {
        StartSearch();
        StopSearch();
    }


    @FXML
    private void StartSearch(){
        String input = searchBar.getText().toLowerCase();
        Node node = model.getAddressIdMap().get(input);
        model.StartDijkstra(node,vehicle);
    }

    @FXML
    private void StopSearch(){
        String input = searchBar1.getText().toLowerCase();
        Node node = model.getAddressIdMap().get(input);
        model.list.add(new Line(model.getDijkstraPath(node)));
        view.redraw();

    }
    @FXML
    private void toggleMode(){
        if(ToggleMode.isSelected()){
        vehicle = true;
        } else {
            vehicle = false;
        }
        if(vehicle){
            System.out.println("BIKE");
        }else {
            System.out.println("CAR");
        }
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
    private void placeInterest(){
        if(POI_MODE){
            POI_MODE = false;
            System.out.println("Leaving POI MODE");
        } else {
            POI_MODE = true;
            System.out.println("Started POI MODE");
        }    }
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

    private void panToAddress(String selectedAddress){
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

            view.pan(dx, dy);

            view.drawCircle(addressX,addressY);
        }
        else if (model.getAddressIdMap().get(selectedAddress) == null){
            System.out.println("Not a valid Address");
        }
        else if(lastPannedToAddress == model.getAddressIdMap().get(selectedAddress)){
            System.out.println("Already panned to this address");
        }
    }

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

        distanceLabel.setText(String.format("Scale of line : %.0f m", distance));  // Setting the distance text directly formatted
    }
}