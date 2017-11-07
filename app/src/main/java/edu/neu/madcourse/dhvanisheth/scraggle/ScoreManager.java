package edu.neu.madcourse.dhvanisheth.scraggle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhvani Sheth on 2/19/2016.
 */
public class ScoreManager {

//    public static int score = 0;

    private static List<Integer> ls = new ArrayList<Integer>();
    static {
        ls.add(1);//A
        ls.add(3);//B
        ls.add(3);//C
        ls.add(2);//D
        ls.add(1);//E
        ls.add(4);//F
        ls.add(2);//G
        ls.add(4);//H
        ls.add(1);//I
        ls.add(8);//J
        ls.add(5);//K
        ls.add(1);//L
        ls.add(3);//M
        ls.add(1);//N
        ls.add(1);//O
        ls.add(3);//P
        ls.add(10);//Q
        ls.add(1);//R
        ls.add(1);//S
        ls.add(1);//T
        ls.add(1);//U
        ls.add(4);//V
        ls.add(4);//W
        ls.add(8);//X
        ls.add(4);//Y
        ls.add(10);//Z

    }
    public static int scoreupdate(String s){

        int score = 0;

        for(int i =0; i<s.length(); i++) {

            score = score + ls.get(s.toLowerCase().charAt(i) - 'a');

        }
        if (s.length() == 3) {
            score = score + 1;
        }
        if (s.length() == 4) {
            score = score + 2;
        }
        if (s.length() == 5) {
            score = score + 3;
        }
        if (s.length() == 6) {
            score = score + 4;
        }
        if (s.length() == 7) {
            score = score + 5;
        }
        if (s.length() == 8) {
            score = score + 6;
        }
        if (s.length() == 9) {
            score = score + 7;
        }
        return score;
    }
}
