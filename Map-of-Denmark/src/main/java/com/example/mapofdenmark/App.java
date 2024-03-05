package com.example.mapofdenmark;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //String filename = "data/denmark.osm.zip.obj";
        String filename = "C:\\Users\\mathi\\Documents\\GitHub\\BFST2024mnla\\xmlparsinglivecoding\\app\\data\\map-2.osm";
        var model = Model.load(filename);
        var view = new View(model, primaryStage);
        new Controller(model, view);
    }

    public Object getGreeting() {
        return null;
    }
}