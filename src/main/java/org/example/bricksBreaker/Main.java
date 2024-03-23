package org.example.bricksBreaker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
//import org.json.JSONArray;
//import org.json.JSONObject;

public class Main extends Application {
//    static Player currentPlayer;
    static AudioClip sound;

    static boolean showAim;
    static boolean saveRecords;
    static String name;


    @Override
    public void start(Stage stage) throws IOException {
        try{

            showAim = true;
            saveRecords = true;

//            JSONArray array = new JSONArray();


            File saves = new File("saves.json");




            sound = new AudioClip(getClass().getResource("soundtrack.mp3").toExternalForm());
//            sound.play();



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