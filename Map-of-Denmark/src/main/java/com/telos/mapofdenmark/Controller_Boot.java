package com.telos.mapofdenmark;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Controller_Boot {

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


    public void init(View_Boot view, Stage primaryStage){
        this.view = view;
        this.primaryStage = primaryStage;
        this.path = System.getProperty("user.dir"); // gets which directory the project is placed
        this.userFile = path + "/data/kbh.osm"; // To be Looked at
    }


    // Loads the custim file when pressing Yes
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
            System.out.println("Error: File not selected or found."); // or something else
            // Using default file
        }

    }
    // Loads the dault file when pressing no
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
    private InputStream loadDefaultFile() {
        InputStream inputStream = Controller_Boot.class.getResourceAsStream("/kbh.osm");
        System.out.println("Loaded default file: " + inputStream); // Print loaded file for debugging
        return inputStream;
    }
    private void runMap(Stage primaryStage,InputStream inputStream, String userFilename) throws XMLStreamException, IOException, ClassNotFoundException {
        var model = Model.load(inputStream, userFilename);
        var view = new View(model, primaryStage);

    }
    public String FileName(){
        if (userFilename == null) return "KBH";
        else return userFilename;
    }
}
