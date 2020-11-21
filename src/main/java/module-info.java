module com.obiwanwheeler {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.obiwanwheeler to javafx.fxml;
    exports com.obiwanwheeler;
}