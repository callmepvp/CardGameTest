module org.example.cardgametest {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.cardgametest to javafx.fxml;
    exports org.example.cardgametest;
}