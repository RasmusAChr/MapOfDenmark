package com.telos.mapofdenmark;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;

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
        this.userFile = path + "/data/Bornholm.osm.obj"; // To be Looked at
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
            userFile = file.getPath();
            userFilename = file.getName();

        } else  {
            System.out.println("Error: File not selected or found."); // or something else
            // Using default file
        }
        chosen = true;
        view.setPath(userFile);
        view.setChosen(chosen);
        runMap(primaryStage,userFile);

    }
    // Loads the dault file when pressing no
    @FXML
    private void default_path() throws XMLStreamException, IOException, ClassNotFoundException {
        // Using default file
        chosen = true;
        view.setPath(userFile);
        view.setChosen(chosen);
        runMap(primaryStage,userFile);

    }
    private void runMap(Stage primaryStage,String path) throws XMLStreamException, IOException, ClassNotFoundException {
        var model = Model.load(path);
        var view = new View(model, primaryStage);

    }
    public String FileName(){
        if (userFilename == null) return "KBH";
        else return userFilename;
    }
}
