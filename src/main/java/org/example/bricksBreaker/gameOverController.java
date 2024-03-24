package org.example.bricksBreaker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

import static org.example.bricksBreaker.Game.score;

public class gameOverController {

    private Stage stage;
    private Parent root;
    private Scene scene;

    @FXML
    void playAgain(ActionEvent event) throws IOException, InterruptedException {
        new Game(event);

    }

    @FXML
    static Label yourScore = new Label();
    public static void init() {
        yourScore.setText(Integer.toString(score));
    }


    @FXML
    void gamePreparation(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("gamePreparation.fxml"));

        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void mainMenu(ActionEvent event) throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Main.updateRecord( root);

        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
