package org.example.bricksBreaker;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Ball {
    static int numberOfBalls=0;
    boolean inMotion = false;
//    static ArrayList<Ball> ballsList = new ArrayList<>();
    double velX;
    double velY;
    Circle circle;
    public Color color;
    static int ballsHitTheBottom = 0;
    Ball(Circle circle){
        numberOfBalls++;
        this.circle = circle;
    }



}
