module com.example.mapofdenmark {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens com.example.mapofdenmark to javafx.fxml;
    exports com.example.mapofdenmark;
}