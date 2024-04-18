module com.telos.mapofdenmark {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;

    opens com.telos.mapofdenmark to javafx.fxml;
    exports com.telos.mapofdenmark;
    exports com.telos.mapofdenmark.KDTree_OLD;
    opens com.telos.mapofdenmark.KDTree_OLD to javafx.fxml;
}