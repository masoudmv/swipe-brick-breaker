package org.example.bricksBreaker;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class Game {
    ArrayList <Ball> ballsList = new ArrayList<>();
    boolean firstBallHitTheBottom = false;
    double ballStartLocationX;
    double ballStartLocationY;

    private Circle firstCircle;
    private final double totalVel = 1;
    private boolean isPaused;
    Line line;
    private final Button pauseButton;


    Line upperBorderLine;
    Line LowerBorderLine;

    static Stage primaryStage;
    static Scene scene;
    static Pane pane;
    private double brickVel = 0.1;
    private final int maxNumberOfRectanglesInARow = 6;
    private final int brickHeight = 36;
    private final double brickWidth = (double) 350 / maxNumberOfRectanglesInARow;

    public void generatorOfBricksInARow(Scene scene, Pane pane, int row){
        Random rand = new Random();
        int numberOfRectangles = rand.nextInt(3) + 1;
        ArrayList <Integer> randNumbers = new ArrayList<>();
        int randomX;
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
            b.rectangle.setStroke(Color.WHITE);
            Brick.bricksList.add(b);
            pane.getChildren().add(b.rectangle);
            pane.getChildren().add(b.label);
        } int color;
        for (int i = 0; i < maxNumberOfRectanglesInARow; i++) {
            if (!randNumbers.contains(i)){
                RegularItem item = new RegularItem();
                item.circle = new Circle();
                color = rand.nextInt(4);
                if (color == 0){
                    item.circle.setFill(Color.GREEN);
                } else if (color ==1){
                    item.circle.setFill(Color.BLUE);
                } else if (color ==2){
                    item.circle.setFill(Color.VIOLET);
                } else {
                    item.circle.setFill(Color.YELLOW);
                }

                item.circle.setCenterX(i * brickWidth + brickWidth/2);
                item.circle.setCenterY(100 + (double) brickHeight / 2);
                item.circle.setRadius(8);
                RegularItem.regularItems.add(item);

            }
        }

    }

    AtomicInteger ro = new AtomicInteger(1);
    Timeline brickGenerator = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
        generatorOfBricksInARow(scene, pane, ro.get());
        ro.getAndIncrement();

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

            }
        }

    }));

    Timeline animation = new Timeline(new KeyFrame(Duration.millis(4), event -> {
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

                Ball.ballsHitTheBottom ++;
                b.velY = 0;
                b.velX = 0;
                b.inMotion = false;
                brickGenerator.play();
                bricksMotion.play();
                ballStartLocationX = b.circle.getTranslateX();
                ballStartLocationY = b.circle.getTranslateY();
                firstCircle = b.circle;
                firstBallHitTheBottom = true;
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
                b.circle.setTranslateY(ballStartLocationY);
                b.circle.setTranslateX(ballStartLocationX);
                b.inMotion = false;

                b.velY = 0;
                b.velX = 0;

                brickGenerator.play();
                bricksMotion.play();
                scene.setOnMouseClicked(e -> {
                    try {
                        handleMouseClick(e.getX(), e.getY(), line, pane);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            } if (bottomBorder && !firstBallHitTheBottom&& b.inMotion) {
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
                    brick.points -= 1;
                    if (brick.points == 0) {
                        Brick.bricksList.remove(brick);
                        pane.getChildren().remove(brick.rectangle);
                        pane.getChildren().remove(brick.label);
                    }
                    brick.label.setText(Integer.toString(brick.points));


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


        pane.getChildren().add(line);
        pane.getChildren().add(circle);
        pane.getChildren().add(pauseButton);

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

        bricksMotion.play();
        brickGenerator.play();
        animation.play();

        primaryStage.show();

    }


    public void handleButtonClick() {
        if (isPaused) {
//            animation.play();
            bricksMotion.play();
            brickGenerator.play();
            pauseButton.setText("pause");
            isPaused = false;
        } else {
            pauseButton.setText("play");
            brickGenerator.pause();
            bricksMotion.pause();
            isPaused = true;
        }
    }


    public void handleMouseMove(double x, double y, Pane pane) {
        pane.getChildren().remove(line);
        line = new Line(x, y, firstCircle.getTranslateX(), firstCircle.getTranslateY());
        line.setStrokeWidth(3);
        line.setFill(Color.rgb(200, 200, 200, 0.5));
        line.getStrokeDashArray().addAll(2d, 21d);
    }

    public void handleMouseClick(double x, double y, Line line, Pane pane) throws InterruptedException {



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
        bricksMotion.stop();
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





