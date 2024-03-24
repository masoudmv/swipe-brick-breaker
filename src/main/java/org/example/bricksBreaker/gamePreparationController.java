package org.example.bricksBreaker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;

import static org.example.bricksBreaker.Brick.brickVel;
import static org.example.bricksBreaker.Game.*;
import static org.example.bricksBreaker.Main.brickIncrement;

public class gamePreparationController {
    String diff;
    static double defaultBrickVel = 0.4;


    @FXML
    TextField textField;

    @FXML
    RadioButton easy;
    @FXML
    void easy(ActionEvent event){
        diff = "easy";
        createdNumberOfRectanglesInARow = 3;
        specialItemDifficulty = 10;
        brickVel = 0.4;
        defaultBrickVel = 0.4;
        Main.newBallDifficulty = 2;
        brickIncrement = 1;
        Main.itemPlace = 340;

    }
    @FXML
    void normal(ActionEvent event){
        diff = "normal";
        createdNumberOfRectanglesInARow = 4;
        specialItemDifficulty = 7;
        brickVel = 0.5;
        defaultBrickVel = 0.5;
        Main.newBallDifficulty = 3;
        brickIncrement = 2;
        Main.itemPlace = 250;
    }
    @FXML
    void hard(ActionEvent event){
        diff = "hard";
        createdNumberOfRectanglesInARow = 5;
        specialItemDifficulty = 4;
        brickVel = 0.6;
        defaultBrickVel = 0.6;
        Main.newBallDifficulty = 5;
        brickIncrement = 3;
        Main.itemPlace = 200;

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
