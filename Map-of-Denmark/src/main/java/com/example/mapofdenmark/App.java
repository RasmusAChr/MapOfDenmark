package com.example.mapofdenmark;

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
//        String path = System.getProperty("user.dir"); // gets which directory the project is placed
//        String filename = path+"\\data\\small.osm";
//        System.out.println(path);
//        var model = Model.load(filename);
//        var view = new View(model, primaryStage);
//        new Controller(model, view);

//        String path = System.getProperty("src\\main\\resources\\com\\example\\mapofdenmark\\GUI.fxml"); // gets which directory the project is placed
//        String filename = path+"\\data\\small.osm";
//        System.out.println(path);
        String fxmlPath = "/com/example/mapofdenmark/GUI.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public Object getGreeting() {
        return null;
    }
}