module com.example.connect4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.intershala.connect4 to javafx.fxml;
    exports com.intershala.connect4;
}