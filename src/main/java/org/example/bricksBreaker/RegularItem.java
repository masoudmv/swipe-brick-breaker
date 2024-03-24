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
import static org.example.bricksBreaker.Main.newBallDifficulty;
import static org.example.bricksBreaker.Main.newBallGenerator;


public class RegularItem {
    static Timer speedItem;

    Circle circle;
    Color color;
    static ArrayList<RegularItem> regularItems = new ArrayList<>();
    private static Timer strengthItemTimer;
//    private static boolean speedInProgress = false; // Flag to indicate if speed item is active
//    private static Timer strengthInProgress; // Timer for the speed item effect
    static boolean strengthActive = false;



//    private static double totalVel = 1; // Assuming initial velocity is 1
//    private static boolean speedInProgress = false; // Flag to indicate if speed item is active
//    private static Timer speedItemTimer; // Timer for the speed item effect
//    private static long remainingTime = 15000; // Remaining time for the speed item timer

    RegularItem(Circle circle){
        this.circle = circle;
    }
    void addBall(){
        Circle circle = new Circle();
        circle.setFill(ballColor);
        circle.setRadius(8);
        Ball ball = new Ball(circle);

    }

    // Assume these are class level variables
     static Timer speedItemTimer = null;
     static long speedEffectEndTime = 0;

    static Timer StrengthItemTimer = null;
    static long strengthEffectEndTime = 0;


    public static void speed() {
        Game.totalVel *= 2;
        for (Ball ball : ballsList) {
            ball.velX *= 2;
            ball.velY *= 2;
        }
        speedInProgress = true; // Set the flag to indicate speed item is active

        // Cancel any existing timer
        if (speedItemTimer != null) {
            speedItemTimer.cancel();
        }

        speedItemTimer = new Timer();
        long delay = 15000; // 15 seconds
        speedEffectEndTime = System.currentTimeMillis() + delay; // Calculate when the effect should end

        speedItemTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (speedInProgress) {
                    Game.totalVel = 1;
                    speedInProgress = false; // Reset the flag after the duration
//                    System.out.println();
                }
            }
        }, delay);
    }

    public static void pauseSpeed() {

        if (speedInProgress && speedItemTimer != null) {
            speedItemTimer.cancel(); // Cancel the current timer
            speedEffectEndTime -= System.currentTimeMillis(); // Calculate the remaining time for the effect
        }
    }

    public static void resumeSpeed() {

        if (speedInProgress && speedEffectEndTime > 0) {
            // Reinitialize the timer for the remaining duration if the effect was active
            speedItemTimer = new Timer();
            speedItemTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (speedInProgress) {
                        Game.totalVel = 1;
                        speedInProgress = false;
                    }
                }
            }, speedEffectEndTime);

            speedEffectEndTime = 0; // Reset the end time
        }
    }


    public static void strength() {
        power = 2;
//        Game.totalVel *= 2;
//        for (Ball ball : ballsList) {
//            ball.velX *= 2;
//            ball.velY *= 2;
//        }
        strengthInProgress = true; // Set the flag to indicate speed item is active

        // Cancel any existing timer
        if (StrengthItemTimer != null) {
            StrengthItemTimer.cancel();
        }

        StrengthItemTimer = new Timer();
        long delay = 15000; // 15 seconds
        strengthEffectEndTime = System.currentTimeMillis() + delay; // Calculate when the effect should end

        StrengthItemTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (strengthInProgress) {
                    power = 1;
                    strengthInProgress = false; // Reset the flag after the duration
//                    System.out.println();
                }
            }
        }, delay);
    }

    public static void pauseStrength() {

        if (strengthInProgress && StrengthItemTimer != null) {
            StrengthItemTimer.cancel(); // Cancel the current timer
            strengthEffectEndTime -= System.currentTimeMillis(); // Calculate the remaining time for the effect
        }
    }

    public static void resumeStrength() {

        if (strengthInProgress && strengthEffectEndTime > 0) {
            // Reinitialize the timer for the remaining duration if the effect was active
            StrengthItemTimer = new Timer();
            StrengthItemTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (strengthInProgress) {
                        power = 1;
                        strengthInProgress = false;
                    }
                }
            }, strengthEffectEndTime);

            strengthEffectEndTime = 0; // Reset the end time
        }
    }


    static Circle generateRandomItem() {
//        System.out.println(newBallGenerator+"   "+ newBallDifficulty);


        Random random = new Random();
        Circle item = new Circle(8, Color.RED);
        double itemX;
        double itemY;

        do {
            itemX = random.nextDouble(10, 340) ; // Random x-coordinate in the scene
            itemY = random.nextDouble(100, Main.itemPlace) ; // Random y-coordinate in the top half of the scene

            item.setCenterX(itemX);
            item.setCenterY(itemY);
        } while (checkIntersection(item));

        RegularItem regularItem = new RegularItem(item);
//        TODO violet generator ...

        regularItems.add(regularItem);
        if (newBallGenerator % newBallDifficulty == 0){
            regularItem.circle.setFill(Color.VIOLET);
        } else{
            int color = random.nextInt(3);
            if (color == 0){
                regularItem.circle.setFill(Color.GOLD);
            } else if (color == 1){
                regularItem.circle.setFill(Color.PURPLE);
            } else {
                regularItem.circle.setFill(Color.GREEN);
            }
        }

        newBallGenerator ++;




//         else if (color == 3) {
//            regularItem.circle.setFill(Color.GREEN);
//        }
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
