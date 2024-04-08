package com.telos.mapofdenmark;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        var View_Boot = new View_Boot(primaryStage);
        // få data fra controller om den er blevet chosen.
    }


}

/*
String path = System.getProperty("user.dir"); // gets which directory the project is placed
        String filename = path + "\\data\\kbh.osm";
        var model = Model.load(filename);
        var view = new View(model, primaryStage);

*/
