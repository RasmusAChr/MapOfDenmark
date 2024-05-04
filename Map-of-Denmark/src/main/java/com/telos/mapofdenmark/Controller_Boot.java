package com.telos.mapofdenmark;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * The Controller_Boot class controls the boot screen of the application, but only the boot screen.
 * It handles user interactions which includes: loading a custom file or choosing the default file.
 */
public class Controller_Boot {
    /**
     * The default constructor for the Controller_Boot class.
     * Only initializes an empty constructor for the FXML file.
     */
    public Controller_Boot(){}

    public String userFilename;

    public String userFile;
    public String path;
    private Stage primaryStage;

    public View_Boot view;

    public boolean chosen = false;

    @FXML
    private Button btn_YES;

    @FXML
    private Button btn_no;
    @FXML
    private Text Loading;

    /**
     * Initializes the controller with the specified view and stage.
     * @param view - the View_Boot associated with this controller
     * @param primaryStage - the primary stage of the JavaFX application
     */
    public void init(View_Boot view, Stage primaryStage){
        this.view = view;
        this.primaryStage = primaryStage;
        this.path = System.getProperty("user.dir"); // gets which directory the project is placed
        this.userFile = path + "/kbh.osm"; // To be Looked at
    }


    /**
     * Method used when the user chooses to load a custom file. It creates a file chooser with the appropriate settings for the allowed file types.
     * If the user back out of choosing a file, it will use the default file to load.
     * @throws XMLStreamException - if an error occurs during XML parsing
     * @throws IOException - if an Input/Output error occurs
     * @throws ClassNotFoundException - If the specified class could not be found
     */
    @FXML
    private void readFile() throws XMLStreamException, IOException, ClassNotFoundException {


        System.out.println("Attempting to show read file dialog");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open .Obj or .OSM File");

        // Set extension filters such that the user cannot choose undesird file types/extensions
        FileChooser.ExtensionFilter osmFilter = new FileChooser.ExtensionFilter("OSM files (*.osm)", "*.osm");
        FileChooser.ExtensionFilter objFilter = new FileChooser.ExtensionFilter("OBJ files (*.obj)", "*.obj");
        FileChooser.ExtensionFilter zippedFilter = new FileChooser.ExtensionFilter("Zipped OSM files (*.osm.zip)", "*.osm.zip");
        fileChooser.getExtensionFilters().addAll(osmFilter, objFilter,zippedFilter);

        Loading.setVisible(true);
        // This retrieves the Stage from this component's scene
        Stage stage = (Stage) btn_YES.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            System.out.println("This is the file name" + file.getName());
            userFilename = file.getName();
            try (InputStream inputStream = new FileInputStream(file)) {
                chosen = true;
                view.setPath(userFilename);
                view.setChosen(chosen);
                runMap(primaryStage, inputStream, userFilename);
            } catch (IOException e) {
                System.err.println("Error: Failed to read file.");
                e.printStackTrace();
            }
        } else {
            // If no file has been selected, pick the default map
            default_path();
        }

    }
    /**
     * Method triggered if the user don't want to pick a custom file.
     * If so the method will load the default file for the user
     * @throws XMLStreamException - if an error occurs during XML parsing
     * @throws IOException - if an Input/Output error occurs
     * @throws ClassNotFoundException - If the specified class could not be found
     */
    @FXML
    private void default_path() throws XMLStreamException, IOException, ClassNotFoundException {
        // Using default file
        InputStream mapInputStream = loadDefaultFile();
        if (mapInputStream != null) {
            chosen = true;
            view.setPath("kbh.osm"); // Set the path to the default filename
            view.setChosen(chosen);
            userFilename = userFile;
            runMap(primaryStage, mapInputStream, userFilename);
        } else {
            System.err.println("Error: Failed to load default file.");
        }
    }

    /**
     * Method used to load the default file from resources using InputStreams.
     * @return - an InputStream for the default file
     */
    private InputStream loadDefaultFile() {
        InputStream inputStream = getClass().getResourceAsStream("/kbh.osm");
        System.out.println("Loaded default file: " + inputStream); // Print loaded file for debugging
        return inputStream;
    }

    /**
     * Method used to run the map with the given InputStream and file name.
     * @param primaryStage - The primary stage of the JavaFX application
     * @param inputStream - the InputStream for the map data
     * @param userFilename - the name of the file chosen
     * @throws XMLStreamException - if an error occurs during XML parsing
     * @throws IOException - if an Input/Output error occurs
     * @throws ClassNotFoundException - If the specified class could not be found
     */
    private void runMap(Stage primaryStage,InputStream inputStream, String userFilename) throws XMLStreamException, IOException, ClassNotFoundException {
        var model = Model.load(inputStream, userFilename);
        var view = new View(model, primaryStage);

    }

    /**
     * Getter method to return the file name of the chosen file
     * @return - String containing the file name
     */
    public String FileName(){
        if (userFilename == null) return "KBH";
        else return userFilename;
    }
}
