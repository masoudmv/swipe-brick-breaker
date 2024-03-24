package org.example.bricksBreaker;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
//import org.json.JSONArray;
//import org.json.JSONObject;

public class Main extends Application {
//    static Player currentPlayer;
    static AudioClip sound;

    static boolean showAim;
    static boolean saveRecords;
    static String name;
    static double maxRecord;
    static int newBallDifficulty = 2;
    static int newBallGenerator = 0;
    static int brickIncrement = 1;
    static double itemPlace = 340;







    public static void updateRecord(Pane root) {
        try (FileReader reader = new FileReader("saves.json")) {
            Gson gson = new Gson();
            Player[] playersArray = gson.fromJson(reader, Player[].class);
            maxRecord = 0;
            for (Player p : playersArray) {
                if (maxRecord < p.score) {
                    maxRecord = p.score;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Label record = new Label();

        record.setText(Double.toString(maxRecord));
        record.setLayoutX(195);
        record.setLayoutY(217);
        record.setTextFill(Color.BLACK);
        root.getChildren().add(record);

    }

//    public class SceneController {
//        @FXML
//        private Label rec;
//
//        // Method to update the label text
//        public void updateLabelText(String text) {
//            rec.setText(text);
//        }
//
//        // Other methods in your controller...
//    }



    @Override
    public void start(Stage stage) throws IOException {
        try{

            showAim = true;
            saveRecords = true;

//            JSONArray array = new JSONArray();


            File saves = new File("saves.json");






            sound = new AudioClip(getClass().getResource("soundtrack.mp3").toExternalForm());
//            sound.play();



            Pane root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));

//            new SceneController();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            updateRecord( root);


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