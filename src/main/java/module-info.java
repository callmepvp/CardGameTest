module org.example.cardgametest {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens org.example.cardgametest to javafx.fxml;
    exports org.example.cardgametest;
}