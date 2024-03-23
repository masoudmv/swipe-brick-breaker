package org.example.bricksBreaker;

import javafx.scene.shape.Rectangle;

import static org.example.bricksBreaker.Brick.*;

public class Aim {

    static double startX;
    static double startY;


    static double mouseX;
    static double mouseY;
    boolean border ;
    static double intersectionX;

    static double intersectionY;

    Aim(){
        border = true;

        double distance = 1000;


        double deltaY = mouseY - startY;
        double deltaX = mouseX - startX;
        double slope = deltaY / deltaX;
        double intercept = startY - slope * startX;

        for (Brick brick: bricksList){
            Rectangle rect = brick.rectangle;

            double brickRightBorderY = slope * (rect.getX()+brickWidth) + intercept;
            double brickLeftBorderY = slope * rect.getX() + intercept;
            double brickBottomBorderX = (rect.getY()+brickHeight - intercept) / slope;



            boolean brickRightBorder = rect.getY() < brickRightBorderY && brickRightBorderY < rect.getY()+brickHeight;
            boolean brickLeftBorder=rect.getY() < brickLeftBorderY && brickLeftBorderY < rect.getY()+brickHeight;
            boolean brickBottomBorder= rect.getX() < brickBottomBorderX && brickBottomBorderX< rect.getX()+brickWidth;




                if (brickLeftBorder){
                    intersectionX = rect.getX();
                    intersectionY = slope * intersectionX + intercept;
                    if (Math.hypot(intersectionX - startX, intersectionY - startY) < distance){
                        distance = Math.hypot(intersectionX - startX, intersectionY - startY);
                        border = false;
                    }

                } if (brickRightBorder){
                    intersectionX = rect.getX() + brickWidth;
                    intersectionY = slope * intersectionX + intercept;
                    if (Math.hypot(intersectionX - startX, intersectionY - startY) < distance){
                        distance = Math.hypot(intersectionX - startX, intersectionY - startY);
                        border = false;
                    }

                } if (brickBottomBorder){
                    intersectionY = rect.getY() + brickHeight;
                    intersectionX = (intersectionY - intercept) / slope;
                    if (Math.hypot(intersectionX - startX, intersectionY - startY) < distance){
                        distance = Math.hypot(intersectionX - startX, intersectionY - startY);
                        border = false;
                    }

                }



        }
        if (distance > 900){

            double rightBorderY = slope * 350 + intercept;
            double leftBorderY = slope * 0 + intercept;
            double topBorderX = (100 - intercept) / slope;
            if (100 < rightBorderY && rightBorderY < 550){
                intersectionX = 350;
                intersectionY = rightBorderY;
            } if (100 < leftBorderY && leftBorderY < 550){
                intersectionX = 0;
                intersectionY = leftBorderY;
            } if (0 < topBorderX && topBorderX < 350){
                intersectionX = topBorderX;
                intersectionY = 100;
            }

        } else {
            double dist = Math.hypot(deltaX, deltaY);
            intersectionX = startX+ distance *deltaX/dist;
            intersectionY = startY+ distance *deltaY/dist;
        }



    }


}
