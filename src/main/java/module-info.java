module com.obiwanwheeler {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    opens com.obiwanwheeler to javafx.fxml;
    exports com.obiwanwheeler;
}