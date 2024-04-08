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

    public String userFile;
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
    }



    @FXML
    private void readFile() throws XMLStreamException, IOException, ClassNotFoundException {
        System.out.println("Attempting to show read file dialog");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open .Obj or .OSM File");

        // Set extension filters such that the user cannot choose undesird file types/extensions
        FileChooser.ExtensionFilter osmFilter = new FileChooser.ExtensionFilter("OSM files (*.osm)", "*.osm");
        FileChooser.ExtensionFilter objFilter = new FileChooser.ExtensionFilter("OBJ files (*.obj)", "*.obj");
        fileChooser.getExtensionFilters().addAll(osmFilter, objFilter);

        // This retrieves the Stage from this component's scene
        Stage stage = (Stage) btn_YES.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        String path = System.getProperty("user.dir"); // gets which directory the project is placed

        if (file != null) {
            userFile = file.getPath();


        } else  {
            System.out.println("error"); // or something else
            userFile = path + "\\data\\kbh.osm.obj";
        }
        chosen = true;
        view.setPath(userFile);
        view.setChosen(chosen);
        runMap(primaryStage,userFile);

    }
    @FXML
    private void default_path() throws XMLStreamException, IOException, ClassNotFoundException {
        String path = System.getProperty("user.dir"); // gets which directory the project is placed
        userFile = path + "\\data\\kbh.osm.obj";
        chosen = true;
        view.setPath(userFile);
        view.setChosen(chosen);
        runMap(primaryStage,userFile);

    }
    private void runMap(Stage primaryStage,String path) throws XMLStreamException, IOException, ClassNotFoundException {
        var model = Model.load(path);
        var view = new View(model, primaryStage);
    }

}
