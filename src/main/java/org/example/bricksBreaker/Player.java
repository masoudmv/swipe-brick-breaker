package org.example.bricksBreaker;

import java.util.ArrayList;
import java.util.Date;

public class Player {

    public String name;
    public double score;
    public Date dateTime;
    static ArrayList<Player> playersList = new ArrayList<>();

    Player(String name, int score, Date dateTime){
        this.name = name;
        this.score = score;
        this.dateTime = dateTime;

    }

    public String getName() {;
        return this.name;
    }

    public double getScore() {
        return this.score;
    }

    public Date getDateTime() {
        return this.dateTime;
    }
}
