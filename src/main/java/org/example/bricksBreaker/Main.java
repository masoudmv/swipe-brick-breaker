package org.example.bricksBreaker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.media.AudioClip;

import java.io.IOException;

public class Main extends Application {
    static AudioClip sound;
    static Color ballColor;


    @Override
    public void start(Stage stage) throws IOException {
        try{



            sound = new AudioClip(getClass().getResource("soundtrack.mp3").toExternalForm());
            sound.play();



            Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

//            new Game();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}