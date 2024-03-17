package org.example.demo4;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GamePlay {
    Line line;

//    TODO add borders ...
    Line upperBorderLine;
    Line LowerBorderLine;

    private final Pane pane;
    private Scene scene;
    private Circle circle;
    private final double totalVel = 4;
    private double velX;
    private double velY;
    private boolean isPaused;

//    private boolean isIncreasing;

    Timeline animation = new Timeline(new KeyFrame(Duration.millis(16.63), event -> {
        circle.setTranslateX(circle.getTranslateX() + velX);
        circle.setTranslateY(circle.getTranslateY() + velY);

        boolean rightBorder = circle.getTranslateX() >= scene.getWidth() - circle.getRadius() - 0.1;
        boolean leftBorder = circle.getTranslateX() <= circle.getRadius() + 0.1;
        boolean topBorder = circle.getTranslateY() <= circle.getRadius() + 0.1;
        boolean bottomBorder = circle.getTranslateY() >= scene.getHeight() - circle.getRadius() - 0.1;

        if (bottomBorder || topBorder) {
            velY *= -1;
        }
        if (rightBorder || leftBorder) {
            velX *= -1;
        }

//        if (rightBorder || leftBorder || topBorder || bottomBorder) {
//            if (!isIncreasing)
//                isIncreasing = true;
//        } else isIncreasing = false;


//        if (isIncreasing) {
////            velX = velX > 0 ? velX + 0.5 : velX - 0.5;
////            velY = velY > 0 ? velY + 0.5 : velY - 0.5;
//
//            Color randomColor = Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
//            circle.setFill(randomColor);
//
//            if (rightBorder)
//                circle.setTranslateX(circle.getTranslateX() - circle.getRadius() - 3);
//            if (leftBorder)
//                circle.setTranslateX(circle.getTranslateX() + circle.getRadius() + 3);
//            if (topBorder)
//                circle.setTranslateY(circle.getTranslateY() + circle.getRadius() + 3);
//            if (bottomBorder)
//                circle.setTranslateY(circle.getTranslateY() - circle.getRadius() - 3);
//
//
//            circle.setRadius(circle.getRadius() + 3);
//
//        }
    }));
    private final Button pauseButton;

    public GamePlay() {
        pane = new Pane();
        scene = new Scene(pane, 300, 500);
        Stage stage = new Stage();
        stage.setScene(scene);

        circle = new Circle(20, Color.BLUE);
        circle.setTranslateX(scene.getWidth() / 2);
        circle.setTranslateY(scene.getHeight() - 100);

        animation.setCycleCount(Animation.INDEFINITE);
        line = new Line();



        animation.play();
        pauseButton = new Button("pause");
        pauseButton.setTranslateX(50);
        pauseButton.setTranslateY(50);
//        scene.setOnKeyPressed(e -> keyPress(e.getCode()));
        pauseButton.setOnAction(e -> handleButtonClick());


        pane.getChildren().add(line);
        pane.getChildren().add(circle);
        pane.getChildren().add(pauseButton);




        scene.setOnMouseMoved(e -> handleMouseMove(e.getX(), e.getY()));
        scene.setOnMouseClicked(e -> handleMouseClick(e.getX(), e.getY()));
        stage.show();
    }

//    public void keyPress(KeyCode code) {
//        switch (code) {
//            case RIGHT -> circle.setTranslateX(circle.getTranslateX() + 2);
//            case LEFT -> circle.setTranslateX(circle.getTranslateX() - 2);
//            case UP -> circle.setTranslateY(circle.getTranslateY() - 2);
//            case DOWN -> circle.setTranslateY(circle.getTranslateY() + 2);
//        }
//    }

    public void handleMouseMove(double x, double y) {
        pane.getChildren().remove(line);
        line = new Line(x, y, circle.getTranslateX(), circle.getTranslateY());
        line.setStrokeWidth(3);
        line.setFill(Color.rgb(200, 200, 200, 0.5));
        line.getStrokeDashArray().addAll(2d, 21d);
        pane.getChildren().add(line);
    }

    public void handleMouseClick(double x, double y){
        double deltaX = x - circle.getTranslateX();
        double deltaY = y - circle.getTranslateY();
        double distance = Math.hypot(deltaX, deltaY);


        velX = totalVel * deltaX / distance;
        velY = totalVel * deltaY / distance;
//        System.out.println(velX+"  "+ deltaX + "   "+ deltaY +"   "+ distance);
    }

    public void handleButtonClick() {
        if (isPaused) {
            animation.play();
            pauseButton.setText("pause");
            isPaused = false;
        } else {
            pauseButton.setText("play");
            animation.pause();
            isPaused = true;
        }
    }
}