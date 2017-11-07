package edu.neu.madcourse.dhvanisheth.twoplayerwordgame;

import java.util.Date;

/**
 * Created by Dhvani on 3/25/2016.
 */
public class TwoPlayerWordGameLeaderboard implements Comparable <TwoPlayerWordGameLeaderboard>{

    private String fullName;
    private int score;
    private String dateTime;

    public TwoPlayerWordGameLeaderboard() {}

    public TwoPlayerWordGameLeaderboard(String fullName, int score, String dateTime) {
        this.fullName = fullName;
        this.dateTime = dateTime;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDateTime() {
        return dateTime;
    }

    @Override
    public int compareTo(TwoPlayerWordGameLeaderboard lb){

        int score = ((TwoPlayerWordGameLeaderboard)lb).getScore();

        return score - this.score;


    }

}
