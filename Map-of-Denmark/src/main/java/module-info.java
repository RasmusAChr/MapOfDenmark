module com.telos.mapofdenmark {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires com.google.common;

    opens com.telos.mapofdenmark to javafx.fxml;
    exports com.telos.mapofdenmark;
}