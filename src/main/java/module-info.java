module org.example.demo4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.bricksBreaker to javafx.fxml;
    exports org.example.bricksBreaker;
}