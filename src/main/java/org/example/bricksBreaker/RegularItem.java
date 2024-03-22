package org.example.bricksBreaker;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static org.example.bricksBreaker.Ball.ballsList;
import static org.example.bricksBreaker.Ball.power;
import static org.example.bricksBreaker.Brick.bricksList;
import static org.example.bricksBreaker.Brick.getRandomColor;
import static org.example.bricksBreaker.Game.speedInProgress;
import static org.example.bricksBreaker.Game.totalVel;


public class RegularItem {
    static Timer speedItem;

    Circle circle;
    Color color;
    static ArrayList <Circle> visibleItems = new ArrayList<>();
    static ArrayList<RegularItem> regularItems = new ArrayList<>();

    RegularItem(Circle circle){
        this.circle = circle;
    }
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
    public static void speed(){
//        System.out.println("speed is running");
//        Game.speedInProgress = true;

        totalVel *= 2;
        for (Ball ball : ballsList){
            ball.velX =2*ball.velX;
            ball.velY =2 * ball.velY;
        }
        speedItem = new Timer();
        speedItem.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Ball ball : ballsList){
                    ball.velX =ball.velX/2;
                    ball.velY = ball.velY/2;
                }
                totalVel = 1;
                speedInProgress = false;
            }
        }, 15000); // 10 seconds

    }
    static void strength(){
//        System.out.println("speed is running");
//        Game.speedInProgress = true;

        power = 2;

        speedItem = new Timer();
        speedItem.schedule(new TimerTask() {
            @Override
            public void run() {
                power = 1;
            }
        }, 15000); // 10 seconds


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
            for (Brick brick: bricksList){
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


    static Circle generateRandomItem() {
        Random random = new Random();
        Circle item = new Circle(8, Color.RED);
        double itemX;
        double itemY;

        do {
            itemX = random.nextDouble(10, 340) ; // Random x-coordinate in the scene
            itemY = random.nextDouble(100, 325) ; // Random y-coordinate in the top half of the scene

            item.setCenterX(itemX);
            item.setCenterY(itemY);
        } while (checkIntersection(item));

        RegularItem regularItem = new RegularItem(item);

        regularItems.add(regularItem);
        int color = random.nextInt(4);
        if (color == 0){
            regularItem.circle.setFill(Color.GREEN);
        } else if (color == 1){
            regularItem.circle.setFill(Color.GREEN);
        } else if (color == 2){
            regularItem.circle.setFill(Color.GREEN);
        } else if (color == 3) {
            regularItem.circle.setFill(Color.GREEN);
        }
//        TODO add bonus itemssssssssssssssssssssssssssssssssssss +=================+++++++++++++++++++


        return item;
    }
    public  static boolean checkIntersection(Circle item) {
        for (Brick brick : bricksList) {
            Rectangle rect = brick.rectangle;
            if (item.getBoundsInParent().intersects(rect.getBoundsInParent())) {
                return true;
            }
        }

        for (RegularItem regularItem : regularItems) {
            Circle existingItem = regularItem.circle;
            if (item != existingItem && item.getBoundsInParent().intersects(existingItem.getBoundsInParent())) {
                return true;
            }
        }

        return false;
    }


}
