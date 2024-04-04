package com.telos.mapofdenmark;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //String filename = "data/denmark.osm.zip.obj";
        String path = System.getProperty("user.dir"); // gets which directory the project is placed
        String filename = path + "\\data\\ituAddress.osm";
        var model = Model.load(filename);
        var view = new View(model, primaryStage);
    }
}