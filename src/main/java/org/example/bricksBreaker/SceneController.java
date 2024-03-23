package org.example.bricksBreaker;


import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
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

    @FXML
    public void gamesHistory(Event event) throws FileNotFoundException {
        Player[] players = new Player[0];
        try (FileReader reader = new FileReader("saves.json")) {
            Gson gson = new Gson();
            players = gson.fromJson(reader, Player[].class);
            for (Player player : players) {
                System.out.println("Name: " + player.getName() + ", Score: " + player.getScore() + "date" + player.getDateTime());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        for (Player player : players) {
            Label label = new Label("Name: " + player.getName() + "- Score: " + player.getScore()+ "- DateAndTime:"+player.getDateTime());
            vbox.getChildren().add(label);
        }
        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);

        Pane pane = new Pane();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(scrollPane, 350, 650);
        stage.setScene(scene);
        stage.show();


    }


    @FXML
    private Button exit;
    @FXML
    private AnchorPane scenePane;
    public void exit(ActionEvent event){
        stage = (Stage) scenePane.getScene().getWindow();
        stage.close();

    }

}