module com.example.mapofdenmark {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mapofdenmark to javafx.fxml;
    exports com.example.mapofdenmark;
}