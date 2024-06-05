module com.example.hexgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.hexgame to javafx.fxml;
    exports com.example.hexgame;
}