package org.example.bricksBreaker;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import static org.example.bricksBreaker.Aim.intersectionX;
import static org.example.bricksBreaker.Aim.intersectionY;
import static org.example.bricksBreaker.Ball.ballsList;
import static org.example.bricksBreaker.Ball.power;
import static org.example.bricksBreaker.Brick.*;
import static org.example.bricksBreaker.Main.showAim;
import static org.example.bricksBreaker.RegularItem.*;
import static org.example.bricksBreaker.Aim.mouseX;
import static org.example.bricksBreaker.Aim.mouseY;
import static org.example.bricksBreaker.Aim.startX;
import static org.example.bricksBreaker.Aim.startY;



public class Game {
    static boolean earthquakeInProgress = false;
    static boolean colorDanceInProgress = false;
    static boolean speedInProgress = false;

    public static int specialItemDifficulty = 5;
    int score = 0;
    static Color ballColor = Color.BLUE;
    Circle aimCircle;

    public static int createdNumberOfRectanglesInARow = 3;

    static double  ballStartLocationX;
    static double ballStartLocationY;
    double aimCircleVelX;
    double aimCircleVelY;


    private Circle firstCircle;
    public static double totalVel = 1;
    private boolean isPaused;
    static Line line;
    private final Button pauseButton;


    boolean firstBallHitTheBottom = false;
    boolean colorD = false;
    boolean anim = false;


    Line upperBorderLine;
    Line LowerBorderLine;
    Label spentTime;
    Label playerScore;

     Stage primaryStage;
     Scene scene;
     Pane pane;
//    public static double brickVel = 0.1;
//    public final int maxNumberOfRectanglesInARow = 6;
//    public final int brickHeight = 36;
//    public final double brickWidth = (double) 350 / maxNumberOfRectanglesInARow;

    public void generatorOfBricksInARow(Scene scene, Pane pane, int row){
        Random rand = new Random();
        int numberOfRectangles = rand.nextInt(createdNumberOfRectanglesInARow) + 1;
        ArrayList <Integer> randNumbers = new ArrayList<>();
        int randomX;
        ArrayList<Brick> bricksInThisRow = new ArrayList<>();
        for (int i = 0; i < numberOfRectangles; i++) {
            Brick b = new Brick(new Rectangle(), row);
            randomX = rand.nextInt(maxNumberOfRectanglesInARow);
            for (Integer n : randNumbers){
                while (randomX == n){
                    randomX = rand.nextInt(maxNumberOfRectanglesInARow);
                }
            } randNumbers.add(randomX);
            double rectangleX = (int) (randomX * (double) 350 / maxNumberOfRectanglesInARow);
            b.rectangle.setX(rectangleX);
            b.rectangle.setHeight(brickHeight);
            b.rectangle.setWidth(brickWidth);
            b.label.setLayoutX(rectangleX + b.rectangle.getWidth()/2 - 5);
            if (colorDanceInProgress){
                b.color = getRandomColor();
            } else {
                b.color = Color.BLACK;
            }

            b.rectangle.setStroke(Color.WHITE);
            Brick.bricksList.add(b);
            bricksInThisRow.add(b);
            pane.getChildren().add(b.rectangle);
            pane.getChildren().add(b.label);

        } int s = rand.nextInt(specialItemDifficulty);
//        TODO color dance
        if (s == 0){
            int index = (int)(Math.random() * bricksInThisRow.size());
            bricksInThisRow.get(index).rectangle.setFill(Color.GREEN);
            bricksInThisRow.get(index).color = Color.GREEN;

//            TODO earthquake
        } else if (s == 1){
            int index = (int)(Math.random() * bricksInThisRow.size());
            bricksInThisRow.get(index).rectangle.setFill(Color.GRAY);
            bricksInThisRow.get(index).color = Color.GRAY;

        }





//        int color;
//        for (int i = 0; i < maxNumberOfRectanglesInARow; i++) {
//            if (!randNumbers.contains(i)){
//                RegularItem item = new RegularItem();
//                item.circle = new Circle();
//                color = rand.nextInt(4);
//                if (color == 0){
//                    item.circle.setFill(Color.GREEN);
//                } else if (color ==1){
//                    item.circle.setFill(Color.BLUE);
//                } else if (color ==2){
//                    item.circle.setFill(Color.VIOLET);
//                } else {
//                    item.circle.setFill(Color.YELLOW);
//                }
//
//                item.circle.setCenterX(i * brickWidth + brickWidth/2);
//                item.circle.setCenterY(100 + (double) brickHeight / 2);
//                item.circle.setRadius(8);
//                RegularItem.regularItems.add(item);
//
//            }
//        }

    }

    private final Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        spentTime.setText(String.valueOf(Integer.parseInt(spentTime.getText()) + 1));
    }));

    AtomicInteger ro = new AtomicInteger(1);
    Timeline brickGenerator = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
        generatorOfBricksInARow(scene, pane, ro.get());
        ro.getAndIncrement();

    }));


//    TODO color dance timeline
    Timeline colorDance = new Timeline(new KeyFrame(Duration.seconds(0.5), event1 -> {
        Brick.colorDance(pane);

    }));



    Timeline animation = new Timeline(new KeyFrame(Duration.millis(4), event -> {


//        System.out.println(earthquakeInProgress);
        //        score update:
//        TODO design a score formula
        playerScore.setText(Integer.toString(score));

        for (Ball b : ballsList) {

            Circle circle = b.circle;
            circle.setTranslateX(circle.getTranslateX() + b.velX);
            circle.setTranslateY(circle.getTranslateY() + b.velY);

            boolean rightBorder = circle.getTranslateX() >= scene.getWidth() - circle.getRadius() - 0.1;
            boolean leftBorder = circle.getTranslateX() <= circle.getRadius() + 0.1;
            boolean topBorder = circle.getTranslateY() <= 100 + circle.getRadius() + 0.1;
            boolean bottomBorder = circle.getTranslateY() - 1 >= scene.getHeight() - 100 - circle.getRadius() - 0.1;

            if (topBorder) {
                b.velY *= -1;
            }
            if (rightBorder || leftBorder) {
                b.velX *= -1;
            }

            for (RegularItem r:regularItems){
                Circle c = r.circle;
                if (b.circle.getBoundsInParent().intersects(c.getBoundsInParent())){
                    if (c.getFill() == Color.GREEN){
                        pane.getChildren().remove(c);
                        strength();


                    } else if (c.getFill() == Color.GOLD){
                        pane.getChildren().remove(c);

                    } else if (c.getFill() == Color.VIOLET){
                        pane.getChildren().remove(c);

                    } else if (c.getFill() == Color.PURPLE) {
                        pane.getChildren().remove(c);

                    }

                }
            }




            if (bottomBorder && Ball.numberOfBalls ==1 && b.inMotion) {
                Brick.brickJump();

//                TODO vmdkvmlkasdvnklsdsnvav
//                pane.getChildren().add(generateRandomItem());


                anim = false;
                line.setVisible(true);




//                RegularItem.createRegularItem(scene, pane);

                Ball.ballsHitTheBottom ++;
                b.velY = 0;
                b.velX = 0;
                b.inMotion = false;
                brickGenerator.play();
//                bricksMotion.play();
                brickVel = 0.4;
                ballStartLocationX = b.circle.getTranslateX();
                ballStartLocationY = b.circle.getTranslateY();
                firstCircle = b.circle;
                firstBallHitTheBottom = true;
                anim = false;
                pane.getChildren().add(generateRandomItem());



                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        scene.setOnMouseClicked(e -> {
                            try {

                                handleMouseClick(e.getX(), e.getY(), line, pane);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        });

                    }
                },251);










            }

            if (bottomBorder && firstBallHitTheBottom && Ball.ballsHitTheBottom+1 != Ball.numberOfBalls && b.inMotion) {
                b.circle.setTranslateY(ballStartLocationY);
                b.circle.setTranslateX(ballStartLocationX);
                b.inMotion = false;
                Ball.ballsHitTheBottom ++;
                b.velY = 0;
                b.velX = 0;

            }  if (bottomBorder && firstBallHitTheBottom && Ball.ballsHitTheBottom+1 == Ball.numberOfBalls&& b.inMotion) {
                Brick.brickJump();
//                TODO ascnaskcnasncacn

//                RegularItem.createRegularItem(scene, pane);
                b.circle.setTranslateY(ballStartLocationY);
                b.circle.setTranslateX(ballStartLocationX);
                b.inMotion = false;

                b.velY = 0;
                b.velX = 0;

                brickGenerator.play();
                line.setVisible(true);
//                bricksMotion.play();
                brickVel = 0.4;
                anim = false;
                pane.getChildren().add(generateRandomItem());
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        scene.setOnMouseClicked(e -> {
                            try {

                                handleMouseClick(e.getX(), e.getY(), line, pane);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        });

                    }
                },251);
            } if (bottomBorder && !firstBallHitTheBottom && b.inMotion) {
                Ball.ballsHitTheBottom ++;
                b.velY = 0;
                b.velX = 0;
                b.inMotion = false;
                ballStartLocationX = b.circle.getTranslateX();
                ballStartLocationY = b.circle.getTranslateY();
                firstCircle = b.circle;
                firstBallHitTheBottom = true;


            }

            for (int i = 0; i < Brick.bricksList.size(); i++) {
                Rectangle rect = Brick.bricksList.get(i).rectangle;
                if (circle.getBoundsInParent().intersects(rect.getBoundsInParent())) {
                    Brick brick = Brick.bricksList.get(i);
                    brick.currentPoints -= power;
                    if (brick.currentPoints <= 0) {
                        if (brick.rectangle.getFill() == Color.GREEN){

                            colorDance.setCycleCount(20);
                            colorDance.play();
                            colorD = true;
                            // Reset the colors back to original after 10 seconds
                            Timer resetTimer = new Timer();
                            resetTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Brick.resetColors(pane);
                                    colorDance.stop();
                                    colorD = false;
                                }
                            }, 10000); // 10 seconds

                        }
//                        todo TODO earthquake
                        if (brick.rectangle.getFill() == Color.GRAY) {
                            if (!earthquakeInProgress){
                                earthquake();
                            }



                        }
                        for (Brick bri: bricksList){
                            bri.rectangle.setWidth(brickWidth);
                            bri.rectangle.setHeight(brickHeight);
                        }
                        score += brick.points;
                        Brick.bricksList.remove(brick);
                        pane.getChildren().remove(brick.rectangle);
                        pane.getChildren().remove(brick.label);

                    }
                    brick.label.setText(Integer.toString(brick.currentPoints));


                    boolean brickLeftBorder = Math.abs(circle.getTranslateX() + circle.getRadius() - rect.getX()) < 0.5;
                    boolean brickRightBorder = Math.abs(rect.getX() + brickWidth - circle.getTranslateX() + circle.getRadius()) < 0.5;

                    if (brickRightBorder || brickLeftBorder) {
                        b.velX *= -1;
                        circle.setTranslateX(circle.getTranslateX() + b.velX / 2);
                        break;
                    } else {
                        b.velY *= -1;
                        circle.setTranslateY(circle.getTranslateY() + b.velY / 2);
                        break;
                    }

                }
            }
        }

    }));

    Timeline bricksMotion = new Timeline(new KeyFrame(Duration.millis(16.63), event -> {
//        System.out.println(brickVel);
//        System.out.println(brickHeight + "  "+ brickWidth);



//        TODO

//        pane.getChildren().remove(line);
//        pane.getChildren().add(line);

        for (RegularItem i : RegularItem.regularItems){
            i.circle.setCenterY(i.circle.getCenterY()+brickVel);
        }
        for (Brick b : Brick.bricksList){
            b.rectangle.setY(b.rectangle.getY() + brickVel);
            b.label.setLayoutY(b.rectangle.getY() + brickVel + 8);

            if (b.rectangle.getHeight() + b.rectangle.getY() >= scene.getHeight()-100){
//                TODO game over screen
                brickVel = 0;
                brickGenerator.stop();
                animation.stop();
                Label label = new Label("GAME OVER");
                label.setLayoutX(150);
                label.setLayoutY(50);



            }
        }

    }));


    public Game() throws InterruptedException {
        primaryStage = new Stage();
        pane = new Pane();
        scene = new Scene(pane, 350, 650);
        primaryStage.setScene(scene);


        scene.setOnMouseMoved(e -> handleMouseMove(e.getX(), e.getY(), pane));

        upperBorderLine = new Line(0, 100,scene.getWidth(), 100);
        LowerBorderLine = new Line(0, 550, scene.getWidth(), 550);



        Circle circle = new Circle(8, ballColor);
        Ball b = new Ball(circle);
        ballsList.add(b);
        circle.setTranslateX(scene.getWidth() / 2);
        circle.setTranslateY(scene.getHeight() - 107);
        firstCircle = circle;


        line = new Line();



        pauseButton = new Button("pause");
        pauseButton.setTranslateX(5);
        pauseButton.setTranslateY(5);
        pauseButton.setOnAction(e -> {
            try {
                handleButtonClick();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });




        Label scr = new Label("score:");
        scr.setLayoutX(239);
        scr.setLayoutY(568);
        scr.setScaleX(2);
        scr.setScaleY(2);
        scr.setTextFill(Color.RED);
        pane.getChildren().add(scr);

        playerScore = new Label("0");
        playerScore.setLayoutX(300);
        playerScore.setLayoutY(570);
        playerScore.setScaleX(2);
        playerScore.setScaleY(2);
        playerScore.setTextFill(Color.RED);



        Label clock = new Label("Time:");
        clock.setLayoutX(40);
        clock.setLayoutY(568);
        clock.setScaleX(2);
        clock.setScaleY(2);
        clock.setTextFill(Color.RED);
        pane.getChildren().add(clock);


        spentTime = new Label();
        spentTime.setText("0");
        spentTime.setLayoutX(100);
        spentTime.setLayoutY(570);
        spentTime.setTextFill(Color.RED);
        spentTime.setScaleX(2);
        spentTime.setScaleY(2);
        pane.getChildren().add(spentTime);


//        smallify.setCycleCount(Animation.INDEFINITE);


        bricksMotion.setCycleCount(Animation.INDEFINITE);
        brickGenerator.setCycleCount(Animation.INDEFINITE);
        animation.setCycleCount(Animation.INDEFINITE);
        timer.setCycleCount(Animation.INDEFINITE);

        bricksMotion.play();
        brickGenerator.play();
        timer.play();
        animation.play();



        scene.setOnMouseClicked(e -> {
            try {
                handleMouseClick(e.getX(), e.getY(), line, pane);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });


        if (showAim){
            pane.getChildren().add(line);
        }


        pane.setBackground(Background.fill(Color.WHITE));

        pane.getChildren().add(circle);
        pane.getChildren().add(pauseButton);
        pane.getChildren().add(upperBorderLine);
        pane.getChildren().add(LowerBorderLine);
        pane.getChildren().add(playerScore);


        primaryStage.show();
    }


    public void handleButtonClick() throws InterruptedException {
        if (isPaused) {
            bricksMotion.play();
            timer.play();
            animation.play();
            pauseButton.setText("pause");
//            speedItem.notify();
            line.setVisible(true);
            if (colorD){
                colorDance.play();
            } if (!anim){
                brickGenerator.play();
            }

            isPaused = false;
        } else {
            pauseButton.setText("play");
            brickGenerator.pause();
            bricksMotion.pause();
            colorDance.pause();
            timer.pause();
            animation.pause();
//            speedItem.wait();
            line.setVisible(false);
            isPaused = true;
        }
    }




    public void handleMouseMove(double x, double y, Pane pane) {

            mouseX = x;
            mouseY = y;
            startX = firstCircle.getTranslateX();
            startY = firstCircle.getTranslateY();

            line.setStrokeWidth(3);
            line.setFill(Color.rgb(200, 200, 200, 0.5));
            line.getStrokeDashArray().addAll(2d, 21d);
            new Aim();

            line.setStartX(firstCircle.getTranslateX());
            line.setStartY(firstCircle.getTranslateY());
            line.setEndX(intersectionX);
            line.setEndY(intersectionY);



//        TODO fix aiming ...



    }



    public void handleMouseClick(double x, double y, Line line, Pane pane) throws InterruptedException {
//        TODO
        line.setVisible(false);

        anim = true;
        if (firstBallHitTheBottom){
            Circle circle = new Circle();
            circle.setTranslateX(ballStartLocationX);
            circle.setTranslateY(ballStartLocationY);
            circle.setFill(ballColor);
            circle.setRadius(8);
            Ball ball = new Ball(circle);
            ballsList.add(ball);
            pane.getChildren().add(circle);

        }
        firstBallHitTheBottom = false;
        Ball.ballsHitTheBottom = 0;




//        bricksMotion.stop();
        brickVel = 0;
        brickGenerator.stop();
        scene.setOnMouseClicked(null);

        double deltaX = x - firstCircle.getTranslateX();
        double deltaY = y - firstCircle.getTranslateY();
        double distance = Math.hypot(deltaX, deltaY);

        Timer timer = new Timer();
        long delay= 100;


        for (int i = 0; i < ballsList.size(); i++){
            final int index = i;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ballsList.get(index).inMotion = true;
                    ballsList.get(index).velX = totalVel * deltaX / distance;
                    ballsList.get(index).velY = totalVel * deltaY / distance;

                    ballsList.get(index).circle.setTranslateX(ballsList.get(index).circle.getTranslateX()+ballsList.get(index).velX);
                    ballsList.get(index).circle.setTranslateY(ballsList.get(index).circle.getTranslateY()+ballsList.get(index).velY);


                }
            },i*delay);

        }
    }



}









