module com.example.mapofdenmark {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens com.telos.mapofdenmark to javafx.fxml;
    exports com.telos.mapofdenmark;
    exports com.telos.mapofdenmark.KDTree;
    opens com.telos.mapofdenmark.KDTree to javafx.fxml;
}