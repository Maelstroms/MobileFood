package edu.neu.madcourse.dhvanisheth.scraggle;

import java.io.Serializable;

/**
 * Created by Dhvani Sheth on 2/19/2016.
 */
public class SubmitWord implements Serializable {
    public static String checkword = new String();

    public void reset(){
        checkword = new String();
    }

    public void setCheckword(char c){
        checkword += c;
    }
}
