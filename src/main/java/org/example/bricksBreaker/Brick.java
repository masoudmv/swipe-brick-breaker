package org.example.bricksBreaker;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Brick {
    Rectangle rectangle;
    Label label;
    int points;
    int  currentPoints;
    static ArrayList<Brick> bricksList = new ArrayList<>();

    Brick(Rectangle r, int points){
        this.rectangle = r;
        this.points = points;
        this.label = new Label(String.valueOf(points));
        this.label.setTextFill(Color.WHITE);
        this.rectangle.setY(100);
//        this.label.setLayoutY(5);
//        this.label.setLayoutX(rectangle.getX());
//        this.label.setLayoutY(rectangle.getY());

    }



}
