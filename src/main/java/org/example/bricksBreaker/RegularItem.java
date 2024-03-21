package org.example.bricksBreaker;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Random;

public class RegularItem {

    Circle circle;
    Color color;
    static ArrayList <Circle> visibleItems = new ArrayList<>();
    static ArrayList<RegularItem> regularItems = new ArrayList<>();

    void addBall(){
        Circle circle = new Circle();
//        circle.setTranslateX(ballStartLocationX);
//        circle.setTranslateY(ballStartLocationY);
        circle.setFill(Color.BLUE);
        circle.setRadius(8);
        Ball ball = new Ball(circle);
//        ballsList.add(ball);
//        pane.getChildren().add(circle);
    }
    void doubleBallVelocity(){

    }
    void strength(){

    }
    void dizziness(){

    }
    static void showRegularItem(Pane pane){
        Random random = new Random();
        int rand = random.nextInt(regularItems.size());
        Circle circle = regularItems.get(rand).circle;
        while (circle.getTranslateY() > 325){
            rand = random.nextInt(regularItems.size());
            circle = regularItems.get(rand).circle;
        } pane.getChildren().add(circle);
        visibleItems.add(circle);
    }

    static void createRegularItem(Scene scene, Pane pane){
        Random random = new Random();
        double x = random.nextDouble(scene.getWidth());
        double y = random.nextDouble(10, scene.getHeight()/2);
        Circle circle = new Circle(x, y, 10);
        boolean tryAgain = false;

        while (true){
            for (Brick brick: Brick.bricksList){
                if (brick.rectangle.getBoundsInParent().intersects(circle.getBoundsInParent())){
                    tryAgain = true;
                }
            } for (Circle c : visibleItems){
                c.setRadius(10);
                if (c.getBoundsInParent().intersects(circle.getBoundsInParent())){
                    tryAgain = true;
                }
                c.setRadius(8);
            } if (!tryAgain){
                circle.setRadius(8);
                visibleItems.add(circle);
                pane.getChildren().add(circle);
                return;

            }
            x = random.nextDouble(scene.getWidth());
            y = random.nextDouble(10, scene.getHeight()/2);

            circle.setTranslateX(x);
            circle.setTranslateY(y);

        }



    }


}
