package org.example.bricksBreaker;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import static org.example.bricksBreaker.Game.ballsList;

//import static org.example.bricksBreaker.Ball.ballsList;

//import static org.example.bricksBreaker.Game.maxNumberOfRectanglesInARow;

public class Brick {

    public static int startSpecialItem = 2;
    public static int endSpecialItem = 3;
    public Color color;
    String type;
    Rectangle rectangle;
//    static double brickWidth = (double) 350 / maxNumberOfRectanglesInARow;
    static double brickHeight = 36;
    Label label;
    int points;
    int  currentPoints;
    static ArrayList<Brick> bricksList = new ArrayList<>();

    Brick(Rectangle r, int points){
        this.rectangle = r;
        this.points = points;
        this.currentPoints = points;
        this.label = new Label(String.valueOf(points));
        this.label.setTextFill(Color.WHITE);
        this.rectangle.setY(100);
//        this.label.setLayoutY(5);
//        this.label.setLayoutX(rectangle.getX());
//        this.label.setLayoutY(rectangle.getY());

    }

//    void getColor(b){
//        return this.color;
//    }

    public static void brickJump(){
//        for( Brick b : bricksList){
//            Rectangle r = b.rectangle;
//            r.setY(r.getY()+15);
//
//        }
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Game.brickVel = 0.6;

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Game.brickVel = 0.1;
            }
        }).start();
    }



//    TODO add color dance methode

    // Method to change the color of all balls and bricks to a random color
    public static void colorDance(Pane pane) {

        pane.setBackground(Background.fill(getRandomColor()));

        for (Ball ball : ballsList) {
            Random random = new Random();
            int index = random.nextInt(100);
            if (index < 20){
                ball.circle.setVisible(false);
            } else {
                ball.circle.setVisible(true);
                ball.circle.setFill(getRandomColor());
            }
        }

        for (Brick brick : bricksList) {
            Random random = new Random();
            int index = random.nextInt(100);
            if (index < 20){
//                invisible the bricks
                brick.rectangle.setVisible(false);
//                brick.rectangle.setStroke(pane.getBackground);
                brick.label.setVisible(false);
            } else {
                brick.rectangle.setVisible(true);
                brick.label.setVisible(true);
                brick.rectangle.setFill(getRandomColor());
            }

        }
//         TODO color dance items
    }



    // Method to get a random color
    public static Color getRandomColor() {
        Random rand = new Random();
        float red = rand.nextFloat();
        float green = rand.nextFloat();
        float blue = rand.nextFloat();
        return new Color(red, green, blue, 1.0); // Alpha value of 1.0 for full opacity
    }

    // Method to reset the colors back to their original state
    public static void resetColors(Pane pane) {
        pane.setBackground(Background.fill(Color.WHITE));
        // Reset the colors of all balls and bricks to their original colors
        for (Ball ball : ballsList) {
            ball.circle.setFill(Color.BLUE);
            ball.circle.setVisible(true);
        }

        for (Brick brick : bricksList) {
            brick.rectangle.setFill(brick.color);
            brick.rectangle.setVisible(true);
            brick.label.setVisible(true);
        }

//        TODO revert color dance items
    }



//    public class Ability {
//        private static final int DURATION = 5000;
//
//        private long activatedAt = Long.MAX_VALUE;
//
//        public void activate() {
//            activatedAt = System.currentTimeMillis();
//        }
//
//        public boolean isActive() {
//            long activeFor = System.currentTimeMillis() - activatedAt;
//
//            return activeFor >= 0 && activeFor <= DURATION;
//        }
//    }



}
