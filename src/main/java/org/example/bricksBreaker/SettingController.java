package org.example.bricksBreaker;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static org.example.bricksBreaker.Main.showAim;
import static org.example.bricksBreaker.Main.sound;


public class SettingController {
    private Stage stage;
    private Parent root;
    private Scene scene;


//    public Button soundON;

    @FXML
    void soundOn(ActionEvent event){
        if(!sound.isPlaying()){
            sound.play();
        }

    }
    @FXML
    void soundOff(ActionEvent event){
        sound.stop();

    }


    public void switchToMainMenu(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));

        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    void AimOff(ActionEvent event){
        showAim = false;

    }
    @FXML
    void AimOn(ActionEvent event){
        showAim = true;

    }
}
