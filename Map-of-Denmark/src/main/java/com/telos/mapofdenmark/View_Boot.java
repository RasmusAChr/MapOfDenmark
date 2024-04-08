package com.telos.mapofdenmark;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class View_Boot {
    public boolean chosen;
    public String path;
    public View_Boot(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Map of Denmark");
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/telos/mapofdenmark/235861.png")));
        primaryStage.getIcons().add(image);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Boot.fxml"));
        // creates a root of the type parent to set scene
        Parent root = loader.load();
        // Creates the controller object from the GUI controller
         Controller_Boot controller_boot = loader.getController();
        // intizalise the rest of the controller with the model and view to run commands on
        controller_boot.init(this, primaryStage);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public void setPath(String a){
        path = a;
    }
    public void setChosen(Boolean a){
        chosen = a;
    }
}
