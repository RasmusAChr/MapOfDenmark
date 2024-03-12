package com.example.mapofdenmark;


import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

public class Controller {

    public Controller(){} // For some reason the code will not run without this line, so don't delete pls....

    double lastX;
    double lastY;
    private Model model;
    private View view;

    @FXML
    private Pane mapPane; //This is a reference to the pane over in the FXML file aka the GUI
    @FXML
    private ToggleButton themeToggleBtn;

    public Controller(Model inputModel, View inputView) {
        this.model = inputModel;
        this.view = inputView;
        view.canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
        });
        view.canvas.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {
                Point2D lastmodel = view.mousetoModel(lastX, lastY);
                Point2D newmodel = view.mousetoModel(e.getX(), e.getY());
                model.add(lastmodel, newmodel);
                view.redraw();
            } else {
                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                view.pan(dx, dy);
            }

            lastX = e.getX();
            lastY = e.getY();
        });
        view.canvas.setOnScroll(e -> {
            double factor = e.getDeltaY();
            view.zoom(e.getX(), e.getY(), Math.pow(1.01, factor));

        });
    }
    @FXML
    private void initialize(){
        themeToggleBtn.getStyleClass().add("root-light");
    }

    @FXML
    private void toggleTheme(){
        if (themeToggleBtn.isSelected()) {
            themeToggleBtn.getStyleClass().remove("root-light");
            themeToggleBtn.getStyleClass().add("root-dark");
            System.out.println("Dark theme");
        } else {
            themeToggleBtn.getStyleClass().remove("root-dark");
            themeToggleBtn.getStyleClass().add("root-light");
            System.out.println("Light theme");
        }
    }
}