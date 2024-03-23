package org.example.bricksBreaker;

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
import static org.example.bricksBreaker.Game.*;


public class RegularItem {
    static Timer speedItem;

    Circle circle;
    Color color;
    static ArrayList<RegularItem> regularItems = new ArrayList<>();

    RegularItem(Circle circle){
        this.circle = circle;
    }
    void addBall(){
        Circle circle = new Circle();
        circle.setFill(ballColor);
        circle.setRadius(8);
        Ball ball = new Ball(circle);

    }
    public static void speed() {
        totalVel *= 2;
        for (Ball ball : ballsList) {
            ball.velX *= 2;
            ball.velY *= 2;
        }
        speedInProgress = true; // Set the flag to indicate speed item is active

        Timer speedItemTimer = new Timer();
        speedItemTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Restore original velocities only if the speed item is still active
                if (speedInProgress) {
//                    for (Ball ball : ballsList) {
//                        ball.velX /= 2;
//                        ball.velY /= 2;
//                    }
                    totalVel = 1;
                    speedInProgress = false; // Reset the flag after the duration
                }
                speedItemTimer.cancel(); // Cancel the timer after executing once
            }
        }, 15000); // 15 seconds
    }


    static void strength(){
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
//        TODO violet generator ...

        regularItems.add(regularItem);
        int color = random.nextInt(4);
        if (color == 0){
            regularItem.circle.setFill(Color.PURPLE);
        } else if (color == 1){
            regularItem.circle.setFill(Color.PURPLE);
        } else if (color == 2){
            regularItem.circle.setFill(Color.PURPLE);
        } else if (color == 3) {
            regularItem.circle.setFill(Color.PURPLE);
        }
//        TODO add bonus items


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
