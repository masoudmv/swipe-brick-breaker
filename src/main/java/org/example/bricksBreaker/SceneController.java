package org.example.bricksBreaker;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class SceneController {



    private Stage stage;
    private Scene scene;
    private Parent root;




        public void switchToSetting(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("setting.fxml"));

        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    } public void switchToGamePreparation(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("gamePreparation.fxml"));

        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

//    public void startGame(ActionEvent event) throws IOException {
//        new GamePlay();
//    }

//    public void switchToSetting(ActionEvent event) throws IOException {
//        root = FXMLLoader.load(getClass().getResource("setting.fxml"));
//        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//
//    }

//    public void setting(ActionEvent event){
//        stage = (Stage) scenePane.getScene().getWindow();
//        stage.close();
//
//    }



    @FXML
    private Button exit;
    @FXML
    private AnchorPane scenePane;
    public void exit(ActionEvent event){
        stage = (Stage) scenePane.getScene().getWindow();
        stage.close();

    }

}