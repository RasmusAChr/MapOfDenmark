package com.telos.mapofdenmark;

import javafx.application.Application;
import javafx.stage.Stage;
/**
 * The app class is the entry point for the application.
 * It extends from the JavaFX Application class and initializes the application by launching the JavaFX runtime.
 */
public class App extends Application {
    /**
     * The main method which launches the JavaFX application by calling the launch method with the provided arguments.
     * @param args - the command line arguments which are passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * The start method is called when the JavaFX application is launched.
     * It initializes the primary stage of the application by creating an instance of View_Boot.
     * @param primaryStage the primary stage for the JavaFX application.
     * @throws Exception - if an exception happens during the initialization.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        var View_Boot = new View_Boot(primaryStage); // creates the boot loader
    }
}

