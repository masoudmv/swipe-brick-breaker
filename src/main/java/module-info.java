module org.example.demo4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;


    opens org.example.bricksBreaker to javafx.fxml;
    exports org.example.bricksBreaker;
}