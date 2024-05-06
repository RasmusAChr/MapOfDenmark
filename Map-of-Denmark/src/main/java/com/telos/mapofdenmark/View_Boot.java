package com.telos.mapofdenmark;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
/**
 * The View_Boot class is responsible for initializing the primary stage and also loading the boot screen.
 * It sets the title, icon, and scene of the primary stage, but also initializes the controller associated with the boot screen.
 */
public class View_Boot {
    public boolean chosen;
    public String path;

    /**
     * Constructs a View_Boot object and initializes the primary stage with the boot screen.
     * @param primaryStage - the primary stage of the application
     * @throws IOException - if an error occurs during the loading of the FXML file
     */
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
    /**
     * Setter method to set the path of the selected file.
     * @param a - path to the selected file
     */
    public void setPath(String a){
        path = a;
    }
    /**
     * Setter method to set the boolean to indicate whether a custom file has been chosen or not.
     * @param a - true = a custom file has been chosen, false = has not chosen a custom file
     */
    public void setChosen(Boolean a){
        chosen = a;
    }
}
