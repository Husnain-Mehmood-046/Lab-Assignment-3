module com.example.fxfilehandling {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.fxfilehandling to javafx.fxml;
    exports com.example.fxfilehandling;
}