package org.example.bricksBreaker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.example.bricksBreaker.Aim.*;
import static org.example.bricksBreaker.Ball.ballsList;
import static org.example.bricksBreaker.Ball.power;
import static org.example.bricksBreaker.Brick.*;
import static org.example.bricksBreaker.Main.saveRecords;
import static org.example.bricksBreaker.Main.showAim;
import static org.example.bricksBreaker.RegularItem.*;
import static org.example.bricksBreaker.gameOverController.yourScore;


public class Game {
    static boolean earthquakeInProgress = false;
    static boolean colorDanceInProgress = false;
    static boolean speedInProgress = false;

    public static int specialItemDifficulty = 5;
    static int score = 0;
    static Color ballColor = Color.BLUE;
    boolean addNewBall = false;

    public static int createdNumberOfRectanglesInARow = 3;

    static double  ballStartLocationX;
    static double ballStartLocationY;
    static boolean dizzinessInProgress = false;


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


    void gameOver() throws IOException {

        earthquakeInProgress = false;
        colorDanceInProgress = false;
        speedInProgress = false;
        dizzinessInProgress = false;
        addNewBall = false;

        colorD = false;
        anim = false;
        firstBallHitTheBottom = false;
        Ball.numberOfBalls = 0;

        primaryStage = (Stage) pane.getScene().getWindow();
        primaryStage.close();


        Stage stage = new Stage();


        Pane root = FXMLLoader.load(getClass().getResource("gameOverScreen.fxml"));
        yourScore.setText(Integer.toString(score));
        yourScore.setVisible(true);
//        System.out.println(yourScore.getText());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        yourScore.setLayoutX(200);
        yourScore.setLayoutY(233);
        yourScore.setScaleX(1.5);
        yourScore.setScaleY(1.5);
        yourScore.setTextFill(new Color(0.1026, 0.27, 0.1054, 1.0));
        root.getChildren().add(yourScore);


        if (saveRecords) {
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();
            List<Player> players = new ArrayList<>();
            players.add(new Player(Main.name, score, currentDate));
            try (FileReader reader = new FileReader("saves.json")) {
                Gson gson = new Gson();
                Player[] playersArray = gson.fromJson(reader, Player[].class);
                if (playersArray != null) {
                    for (Player player : playersArray) {
                        players.add(player);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (FileWriter writer = new FileWriter("saves.json")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(players, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stage.show();
    }

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
            bricksList.add(b);
            bricksInThisRow.add(b);
            pane.getChildren().add(b.rectangle);
            pane.getChildren().add(b.label);

        } int s = rand.nextInt(specialItemDifficulty);
        if (s == 0){
            int index = (int)(Math.random() * bricksInThisRow.size());
            bricksInThisRow.get(index).rectangle.setFill(Color.GREEN);
            bricksInThisRow.get(index).color = Color.GREEN;

        } else if (s == 1){
            int index = (int)(Math.random() * bricksInThisRow.size());
            bricksInThisRow.get(index).rectangle.setFill(Color.GRAY);
            bricksInThisRow.get(index).color = Color.GRAY;

        }
    }

    private final Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        spentTime.setText(String.valueOf(Integer.parseInt(spentTime.getText()) + 1));
    }));

    AtomicInteger ro = new AtomicInteger(1);
    Timeline brickGenerator = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
        generatorOfBricksInARow(scene, pane, ro.get());
        ro.getAndIncrement();

    }));


    Timeline colorDance = new Timeline(new KeyFrame(Duration.seconds(0.5), event1 -> {
        colorDance(pane);

    }));



    Timeline animation = new Timeline(new KeyFrame(Duration.millis(4), event -> {


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
//                        strength
                    if (c.getFill() == Color.GREEN){
                        c.setFill(Color.WHITE);
                        pane.getChildren().remove(c);
                        strength();
//                        speed
                    } if (c.getFill() == Color.GOLD){
                        pane.getChildren().remove(c);
                        c.setFill(Color.WHITE);
                        if (!speedInProgress){
                            speed();
                        }
//                        add ball
                    } if (c.getFill() == Color.VIOLET){
                        c.setFill(Color.WHITE);
                        pane.getChildren().remove(c);
                        addNewBall = true;
//                      dizziness
                    } if (c.getFill() == Color.PURPLE) {
                        c.setFill(Color.WHITE);
                        pane.getChildren().remove(c);
                        dizzinessInProgress = true;

                    }

                }
            }




            if (bottomBorder && Ball.numberOfBalls ==1 && b.inMotion) {
                brickJump();

                anim = false;
                line.setVisible(true);



                Ball.ballsHitTheBottom ++;
                b.velY = 0;
                b.velX = 0;
                b.inMotion = false;
                brickGenerator.play();

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
                brickJump();

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

            for (int i = 0; i < bricksList.size(); i++) {
                Brick brick = bricksList.get(i);
                Rectangle rect = brick.rectangle;

                // Check for collision
                if (circle.getBoundsInParent().intersects(rect.getBoundsInParent())) {
                    // Calculate distance between ball center and rectangle center
                    double dx = circle.getTranslateX() - rect.getX() - rect.getWidth() / 2;
                    double dy = circle.getTranslateY() - rect.getY() - rect.getHeight() / 2;

                    // Calculate the maximum distance between ball center and rectangle edges
                    double maxXDist = rect.getWidth() / 2 + circle.getRadius();
                    double maxYDist = rect.getHeight() / 2 + circle.getRadius();

                    // If the distance is within the maximum allowed distance, handle collision
                    if (Math.abs(dx) < maxXDist && Math.abs(dy) < maxYDist) {
                        // Determine collision side
                        double offsetX = maxXDist - Math.abs(dx);
                        double offsetY = maxYDist - Math.abs(dy);

                        if (offsetX < offsetY) {
                            // Horizontal collision
                            b.velX *= -1;
                            if (dx > 0) {
                                // Ball is on the right side of the rectangle
                                circle.setTranslateX(rect.getX() + rect.getWidth() + circle.getRadius());
                            } else {
                                // Ball is on the left side of the rectangle
                                circle.setTranslateX(rect.getX() - circle.getRadius());
                            }
                        } else {
                            // Vertical collision
                            b.velY *= -1;
                            if (dy > 0) {
                                // Ball is below the rectangle
                                circle.setTranslateY(rect.getY() + rect.getHeight() + circle.getRadius());
                            } else {
                                // Ball is above the rectangle
                                circle.setTranslateY(rect.getY() - circle.getRadius());
                            }
                        }

                        // Update brick points if its health points reach zero
                        brick.currentPoints -= power;
                        if (brick.currentPoints <= 0) {
                            if (brick.rectangle.getFill() == Color.GREEN){
//
                            colorDance.setCycleCount(20);
                            colorDance.play();
                            colorD = true;
                            // Reset the colors back to original after 10 seconds
                            Timer resetTimer = new Timer();
                            resetTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    resetColors(pane);
                                    colorDance.stop();
                                    colorD = false;
                                }
                            }, 10000); // 10 seconds

                        } if (brick.rectangle.getFill() == Color.GRAY) {
                            if (!earthquakeInProgress){
                                earthquake();
                            }
                        }
                            score += brick.points;
                            bricksList.remove(brick);
                            pane.getChildren().remove(brick.rectangle);
                            pane.getChildren().remove(brick.label);
                        }
                        brick.label.setText(Integer.toString(brick.currentPoints));
                    }
                }
            }

        }

    }));

    Timeline bricksMotion = new Timeline();
    KeyFrame keyFrame = new KeyFrame(Duration.millis(16.63), event -> {
        for (RegularItem i : regularItems){
            i.circle.setCenterY(i.circle.getCenterY() + brickVel);
        }
        for (Brick b : bricksList){
            b.rectangle.setY(b.rectangle.getY() + brickVel);
            b.label.setLayoutY(b.rectangle.getY() + brickVel + 8);

            if (b.rectangle.getHeight() + b.rectangle.getY() >= scene.getHeight() - 100){

                brickGenerator.stop();
                animation.stop();
                bricksMotion.stop();
                timer.stop();

                colorDance.stop();
                ballsList.clear();
                bricksList.clear();
                regularItems.clear();

                try {
                    gameOver();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });


    public Game(Event event) throws InterruptedException, IOException {

        pane = new Pane();

        primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
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




        bricksMotion.setCycleCount(Animation.INDEFINITE);
        brickGenerator.setCycleCount(Animation.INDEFINITE);
        animation.setCycleCount(Animation.INDEFINITE);
        timer.setCycleCount(Animation.INDEFINITE);



        bricksMotion.getKeyFrames().add(keyFrame);


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
            line.setVisible(false);
            isPaused = true;
        }
    }




    public void handleMouseMove(double x, double y, Pane pane) {

            mouseX = x;
            mouseY = y;
            if (dizzinessInProgress){
                Random random = new Random();
                mouseX = random.nextDouble(5, 345);
                mouseY = 450;
            }
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
            if (addNewBall){
                Circle c = new Circle();
                c.setTranslateX(ballStartLocationX);
                c.setTranslateY(ballStartLocationY);
                c.setFill(ballColor);
                c.setRadius(8);
                Ball b = new Ball(c);
                ballsList.add(b);
                pane.getChildren().add(c);
                addNewBall = false;
            }

        }
        firstBallHitTheBottom = false;
        Ball.ballsHitTheBottom = 0;

        brickVel = 0;
        brickGenerator.stop();
        scene.setOnMouseClicked(null);



        double deltaX;
        double deltaY;
        if(dizzinessInProgress){
            deltaX = mouseX - firstCircle.getTranslateX();
            deltaY = mouseY - firstCircle.getTranslateY();
            dizzinessInProgress = false;
        } else {
            deltaX = x - firstCircle.getTranslateX();
            deltaY = y - firstCircle.getTranslateY();
        }
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









