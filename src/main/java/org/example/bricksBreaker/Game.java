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

import static org.example.bricksBreaker.Brick.getRandomColor;

public class Game {

    private final int specialItemDifficulty = 5;
    static ArrayList <Ball> ballsList = new ArrayList<>();
    int score = 0;
    boolean firstBallHitTheBottom = false;
    double ballStartLocationX;
    double ballStartLocationY;
    boolean colorD = false;
    boolean anim = false;

    private Circle firstCircle;
    private final double totalVel = 1;
    private boolean isPaused;
    Line line;
    private final Button pauseButton;


    Line upperBorderLine;
    Line LowerBorderLine;
    Label spentTime;
    Label playerScore;

    static Stage primaryStage;
    static Scene scene;
    static Pane pane;
    public static double brickVel = 0.1;
    private final int maxNumberOfRectanglesInARow = 6;
    private final int brickHeight = 36;
    private final double brickWidth = (double) 350 / maxNumberOfRectanglesInARow;

    public void generatorOfBricksInARow(Scene scene, Pane pane, int row){
        Random rand = new Random();
        int numberOfRectangles = rand.nextInt(3) + 1;
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
            double rectangleX = (int) (randomX * brickWidth);
            b.rectangle.setX(rectangleX);
            b.rectangle.setHeight(brickHeight);
            b.rectangle.setWidth(scene.getWidth()/maxNumberOfRectanglesInARow);
            b.label.setLayoutX(rectangleX + b.rectangle.getWidth()/2 - 5);
            b.color = Color.BLACK;
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
            bricksInThisRow.get(index).rectangle.setFill(Color.GREEN);
            bricksInThisRow.get(index).color = Color.GREEN;

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

    private Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        spentTime.setText(String.valueOf(Integer.parseInt(spentTime.getText()) + 1));
//        pauseButton.setOnAction();

    }));

    AtomicInteger ro = new AtomicInteger(1);
    Timeline brickGenerator = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
        generatorOfBricksInARow(scene, pane, ro.get());
        ro.getAndIncrement();

    }));


//    TODO color dance timeline
    Timeline colorDance = new Timeline(new KeyFrame(Duration.seconds(0.5), event1 -> {
        Brick.colorDance(pane);

//        primaryStage.setScene(scene);


    }));



    Timeline animation = new Timeline(new KeyFrame(Duration.millis(4), event -> {
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

            if (bottomBorder && Ball.numberOfBalls ==1 && b.inMotion) {
                Brick.brickJump();
//                RegularItem.createRegularItem(scene, pane);

                Ball.ballsHitTheBottom ++;
                b.velY = 0;
                b.velX = 0;
                b.inMotion = false;
                brickGenerator.play();
//                bricksMotion.play();
                brickVel = 0.1;
                ballStartLocationX = b.circle.getTranslateX();
                ballStartLocationY = b.circle.getTranslateY();
                firstCircle = b.circle;
                firstBallHitTheBottom = true;
                anim = false;
                scene.setOnMouseClicked(e -> {
                    try {
                        handleMouseClick(e.getX(), e.getY(), line, pane);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                });



            }if (bottomBorder && firstBallHitTheBottom && Ball.ballsHitTheBottom+1 != Ball.numberOfBalls && b.inMotion) {
                b.circle.setTranslateY(ballStartLocationY);
                b.circle.setTranslateX(ballStartLocationX);
                b.inMotion = false;
                Ball.ballsHitTheBottom ++;
                b.velY = 0;
                b.velX = 0;

            }  if (bottomBorder && firstBallHitTheBottom && Ball.ballsHitTheBottom+1 == Ball.numberOfBalls&& b.inMotion) {
                Brick.brickJump();
//                RegularItem.createRegularItem(scene, pane);
                b.circle.setTranslateY(ballStartLocationY);
                b.circle.setTranslateX(ballStartLocationX);
                b.inMotion = false;

                b.velY = 0;
                b.velX = 0;

                brickGenerator.play();
//                bricksMotion.play();
                brickVel = 0.1;
                anim = false;
                scene.setOnMouseClicked(e -> {
                    try {
                        handleMouseClick(e.getX(), e.getY(), line, pane);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                });
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
                    brick.currentPoints -= 1;
                    if (brick.currentPoints == 0) {
                        if (brick.rectangle.getFill() == Color.GREEN){

                            colorDance.setCycleCount(Animation.INDEFINITE);
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

    Timeline bricksMotion = new Timeline(new KeyFrame(Duration.millis(4), event -> {

        pane.getChildren().remove(line);
        pane.getChildren().add(line);

        for (RegularItem i : RegularItem.regularItems){
            i.circle.setCenterY(i.circle.getCenterY()+brickVel);
        }
        for (Brick b : Brick.bricksList){
            b.rectangle.setY(b.rectangle.getY() + brickVel);
            b.label.setLayoutY(b.rectangle.getY() + brickVel + 8);

            if (b.rectangle.getHeight() + b.rectangle.getY() >= scene.getHeight()-100){
                brickVel = 0;
                brickGenerator.stop();
                animation.stop();
                timer.stop();

            }
        }

    }));


    public Game() throws InterruptedException {
        primaryStage = new Stage();
        pane = new Pane();
        scene = new Scene(pane, 350, 650);
        primaryStage.setScene(scene);

        upperBorderLine = new Line();
        LowerBorderLine = new Line();




        upperBorderLine.setStartX(0);
        upperBorderLine.setStartY(100);
        upperBorderLine.setEndX(scene.getWidth());
        upperBorderLine.setEndY(100);


        LowerBorderLine.setStartX(0);
        LowerBorderLine.setStartY(550);
        LowerBorderLine.setEndX(scene.getWidth());
        LowerBorderLine.setEndY(550);


        pane.getChildren().add(upperBorderLine);
        pane.getChildren().add(LowerBorderLine);


        Circle circle = new Circle(8, Color.BLUE);
        Ball b = new Ball(circle);
        ballsList.add(b);
        circle.setTranslateX(scene.getWidth() / 2);
        circle.setTranslateY(scene.getHeight() - 107);
        firstCircle = circle;


        line = new Line();



        pauseButton = new Button("pause");
        pauseButton.setTranslateX(5);
        pauseButton.setTranslateY(5);

        pauseButton.setOnAction(e -> handleButtonClick());


        pane.setBackground(Background.fill(Color.WHITE));
        pane.getChildren().add(line);
        pane.getChildren().add(circle);
        pane.getChildren().add(pauseButton);
//        pane.getChildren().add(spentTime);

        scene.setOnMouseMoved(e -> handleMouseMove(e.getX(), e.getY(), Game.pane));
        scene.setOnMouseClicked(e -> {
            try {
                handleMouseClick(e.getX(), e.getY(), line, pane);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        bricksMotion.setCycleCount(Animation.INDEFINITE);
        brickGenerator.setCycleCount(Animation.INDEFINITE);
        animation.setCycleCount(Animation.INDEFINITE);
        timer.setCycleCount(Animation.INDEFINITE);



//        Timer t = new Timer();
//        Stopwatch stopwatch = new Stopwatch();


        playerScore = new Label();
        playerScore.setText("0");

        playerScore.setLayoutX(200);
        playerScore.setLayoutY(570);
        playerScore.setTextFill(Color.RED);
        pane.getChildren().add(playerScore);

//        stopwatch.start();
        spentTime = new Label();

        spentTime.setText("0");
        spentTime.setLayoutX(100);
        spentTime.setLayoutY(570);
        spentTime.setTextFill(Color.RED);
        pane.getChildren().add(spentTime);
//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                label.setText(Float.toString(stopwatch.getElapsedTimeSeconds()));
//
//
//            }
//        },1000);

//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//
//                while (true){
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                    label.setText(Float.toString(stopwatch.getElapsedTimeSeconds()));
//                }
//
//
//
////
//            }
//        }).start();







//        label = ;
        bricksMotion.play();
        brickGenerator.play();
        timer.play();
        animation.play();


        primaryStage.show();



//        System.out.println("Time elapsed in seconds: " + stopwatch.getElapsedTimeSeconds());

    }


    public void handleButtonClick() {
        if (isPaused) {
//            animation.play();
            bricksMotion.play();

            timer.play();
            animation.play();
            pauseButton.setText("pause");
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
//            TODO pause timer???
            timer.pause();
            animation.pause();
            isPaused = true;
        }
    }


    public void handleMouseMove(double x, double y, Pane pane) {
        pane.getChildren().remove(line);
        line = new Line(x, y, firstCircle.getTranslateX(), firstCircle.getTranslateY());
        line.setStrokeWidth(3);
        line.setFill(Color.rgb(200, 200, 200, 0.5));
        line.getStrokeDashArray().addAll(2d, 21d);

//        TODO fix aiming ...

//        for (Brick b : Brick.bricksList){
//            if (b.rectangle.getBoundsInParent().intersects(line.getBoundsInParent())){
//                line.setStartX(b.rectangle.getX());
//                line.setStartY(b.rectangle.getY());
//
//            }
//        }

    }

    public void handleMouseClick(double x, double y, Line line, Pane pane) throws InterruptedException {

        anim = true;



        if (firstBallHitTheBottom){
            Circle circle = new Circle();
            circle.setTranslateX(ballStartLocationX);
            circle.setTranslateY(ballStartLocationY);
            circle.setFill(Color.BLUE);
            circle.setRadius(8);
            Ball ball = new Ball(circle);
            ballsList.add(ball);
            pane.getChildren().add(circle);

        }
        firstBallHitTheBottom = false;
        Ball.ballsHitTheBottom = 0;



        pane.getChildren().remove(line);
//        bricksMotion.stop();
        brickVel =0;
        brickGenerator.stop();
        scene.setOnMouseClicked(null);

        double deltaX = x - firstCircle.getTranslateX();
        double deltaY = y - firstCircle.getTranslateY();
        double distance = Math.hypot(deltaX, deltaY);

        Timer timer = new Timer();
        long delay= 80;


        for (int i = 0; i < ballsList.size(); i++){
            final int index = i;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ballsList.get(index).inMotion = true;
                    ballsList.get(index).velX = totalVel * deltaX / distance;
                    ballsList.get(index).velY = totalVel * deltaY / distance;

                }
            },i*delay);

        }
    }



}





