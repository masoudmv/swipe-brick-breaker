package org.example.bricksBreaker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;

import static org.example.bricksBreaker.Brick.brickVel;
import static org.example.bricksBreaker.Game.*;

public class gamePreparationController {
    String diff;


    @FXML
    TextField textField;

    @FXML
    RadioButton easy;
    @FXML
    void easy(ActionEvent event){
        diff = "easy";
        createdNumberOfRectanglesInARow = 3;
        specialItemDifficulty = 6;
        brickVel = 0.4;

    }
    @FXML
    void normal(ActionEvent event){
        diff = "normal";
        createdNumberOfRectanglesInARow = 4;
        specialItemDifficulty = 5;
        brickVel = 0.5;
    }
    @FXML
    void hard(ActionEvent event){
        diff = "hard";
        createdNumberOfRectanglesInARow = 5;
        specialItemDifficulty = 4;
        brickVel = 0.6;

    }
    @FXML
    void blue(ActionEvent event){
        ballColor = Color.BLUE;

    }
    @FXML
    void red(ActionEvent event){
        ballColor = Color.RED;

    }
    @FXML
    void gold(ActionEvent event){
        ballColor = Color.GOLD;

    }
    @FXML
    void start(ActionEvent event) throws InterruptedException, IOException {
        Main.name = textField.getText();
        new Game(event);

    }
}
