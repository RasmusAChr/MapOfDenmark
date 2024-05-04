package com.telos.mapofdenmark;

import javafx.application.Application;
/**
 * The Launcher class is responsible for launching the JavaFX application.
 * It contains the main method that calls the launch method. Only used so that the jar version of the application can work properly
 */
public class Launcher {
    /**
     * Main method to launch the JavaFX application
     * @param args -  the command line arguments passed to the application
     */
    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}