package org.example.bricksBreaker;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static org.example.bricksBreaker.Ball.ballsList;
import static org.example.bricksBreaker.Game.*;

public class Brick {
    public static final int maxNumberOfRectanglesInARow = 6;
    public static double brickHeight = 36;
    public static double brickWidth = (double) 350 / maxNumberOfRectanglesInARow;

    public Color color;
    public static double brickVel = 0.4;
    String type;
    Rectangle rectangle;

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
//        System.out.println("brick jump is running");
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                brickVel *= 6;

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


                brickVel /= 6;
            }
        }).start();
    }



//    TODO add color dance methode

    // Method to change the color of all balls and bricks to a random color
    public static void colorDance(Pane pane) {
        earthquakeInProgress = true;
        Color color = getRandomColor();
        pane.setBackground(Background.fill(color));

        for (Ball ball : ballsList) {
            Random random = new Random();
            int index = random.nextInt(100);
            if (index < 20){
                ball.circle.setVisible(false);
            } else {
                ball.circle.setVisible(true);
                ball.circle.setFill(getRandomColor());
            }
        } for (Brick brick : bricksList) {
            Random random = new Random();
            int index = random.nextInt(100);
            if (index < 20){
//                invisible the bricks
                brick.rectangle.setVisible(false);
                brick.label.setVisible(false);
            } else {
                brick.rectangle.setVisible(true);
                brick.label.setVisible(true);
                brick.rectangle.setFill(getRandomColor());
                brick.rectangle.setStroke(color);
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
        colorDanceInProgress = false;
        pane.setBackground(Background.fill(Color.WHITE));
        // Reset the colors of all balls and bricks to their original colors
        for (Ball ball : ballsList) {
            ball.circle.setFill(ballColor);
            ball.circle.setVisible(true);
        }

        for (Brick brick : bricksList) {
            brick.rectangle.setFill(brick.color);
            brick.rectangle.setStroke(Color.WHITE);
            brick.rectangle.setVisible(true);
            brick.label.setVisible(true);
        }

//        TODO revert color dance items
    }


    static Timeline smallify = new Timeline(new KeyFrame(Duration.millis(16.63), event -> {
        reduce();

    }));

    static Timeline biggify = new Timeline(new KeyFrame(Duration.millis(16.63), event -> {
        magnify();

    }));

    public static void reduce(){
        brickHeight -= 0.2;
        brickWidth -= 0.2;
        for(Brick b:bricksList){
            b.rectangle.setWidth(brickWidth);
            b.rectangle.setHeight(brickHeight);
            b.rectangle.setX(b.rectangle.getX()+0.1);
            b.rectangle.setY(b.rectangle.getY()+0.1);
            b.label.setLayoutX(b.label.getLayoutX()+0.05);
            b.label.setLayoutY(b.label.getLayoutY()-0.05);


        }
    }
    public static void magnify(){
        brickHeight += 0.2;
        brickWidth += 0.2;
        for(Brick b:bricksList){
            b.rectangle.setWidth(brickWidth);
            b.rectangle.setHeight(brickHeight);
            b.rectangle.setX(b.rectangle.getX()-0.1);
            b.rectangle.setY(b.rectangle.getY()-0.1);
            b.label.setLayoutX(b.label.getLayoutX()-0.05);
            b.label.setLayoutY(b.label.getLayoutY()+0.05);

        }
    }
    public static void earthquake(){
        earthquakeInProgress = true;



        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            boolean shrink = true;


            @Override
            public void run() {
                if (shrink) {

                    smallify.setCycleCount(60);
                    smallify.play();

                } else {

                    biggify.setCycleCount(60);
                    biggify.play();
                }

                shrink = !shrink;
            }
        }, 0, 1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {


                timer.cancel();
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
//                        System.out.println("default");
                        brickWidth = (double) 350 / maxNumberOfRectanglesInARow;
                        brickHeight = 36;
                        earthquakeInProgress = false;
                    }
                },1020);



            }
        }, 9000);



    }



}
