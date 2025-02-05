module com.example.teest {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.teest to javafx.fxml;
    exports com.example.teest;
}