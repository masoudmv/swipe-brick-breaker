package org.example.bricksBreaker;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import static org.example.bricksBreaker.Main.sound;


public class SettingController {


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


//    @FXML
//    void close(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("scene2.fxml"));
//        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//
//
//    }
}
