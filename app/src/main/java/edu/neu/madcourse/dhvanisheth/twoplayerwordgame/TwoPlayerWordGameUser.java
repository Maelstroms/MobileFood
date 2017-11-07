package edu.neu.madcourse.dhvanisheth.twoplayerwordgame;

import com.firebase.client.Firebase;

/**
 * Created by Dhvani on 3/25/2016.
 */
public class TwoPlayerWordGameUser {


    private boolean available;
    private String fullName;
    private String regId;

    public TwoPlayerWordGameUser() {}

    public TwoPlayerWordGameUser(String fullName, boolean available, String regId) {
        this.fullName = fullName;
        this.available = available;
        this.regId = regId;
    }

    public boolean getAvailable() {
        return available;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRegId() {
        return regId;
    }

//    Firebase alanRef = ref.child("users").child("alanisawesome");
//
//    User alan = new User("Alan Turing", 1912);
//
//    alanRef.setValue(alan);
}
