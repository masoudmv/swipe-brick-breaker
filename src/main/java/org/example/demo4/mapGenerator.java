package org.example.demo4;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class mapGenerator{
    private Stage stage;
    private final int maxNumberOfRectanglesInARow = 6;
    private int brickVel = 1;
    ArrayList <Rectangle> rectangles = new ArrayList<>();

    public mapGenerator() throws InterruptedException {

        Pane pane = new Pane();
        Scene scene = new Scene(pane, 300, 500);
        stage = new Stage();
        stage.setScene(scene);


        Timeline animation = animation(scene, pane);

        stage.show();

    }

    private Timeline animation(Scene scene, Pane pane) {
        Timeline rectangleGenerator = new Timeline(new KeyFrame(Duration.millis(750), event -> {
            generatorOfRectanglesInARow(scene, pane);
        }));

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(16.63), event -> {
            for (Rectangle r : rectangles){
                r.setY(r.getY() + brickVel);
                if (r.getHeight()+r.getY() >= scene.getHeight()){
                    brickVel = 0;
                    rectangleGenerator.stop();
                }
            }

        }));

        animation.setCycleCount(Animation.INDEFINITE);
        rectangleGenerator.setCycleCount(Animation.INDEFINITE);

        rectangleGenerator.play();
        animation.play();
        return animation;
    }

    public void generatorOfRectanglesInARow(Scene scene, Pane pane){
        Random rand = new Random();
        int numberOfRectangles = rand.nextInt(3) + 1;

        for (int i = 0; i < numberOfRectangles; i++) {
            Rectangle r = new Rectangle();
//            TODO fix repeated rectangle start location ...
            int rectangleX = (int) (rand.nextInt(maxNumberOfRectanglesInARow) * scene.getWidth()/maxNumberOfRectanglesInARow);
            r.setX(rectangleX);
            r.setY(0);
            r.setStroke(Color.WHITE);
            r.setHeight(20);
            r.setWidth(scene.getWidth()/maxNumberOfRectanglesInARow);
            rectangles.add(r);

//            Text text = new Text("...");
//            text.setFill(Color.WHITE);
//            StackPane stack = new StackPane();
//            stack.getChildren().addAll(r, text);
            pane.getChildren().add(r);
        }

    }




}
