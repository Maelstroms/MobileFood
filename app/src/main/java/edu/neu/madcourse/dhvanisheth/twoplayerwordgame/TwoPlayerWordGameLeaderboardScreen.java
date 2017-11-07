/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.dhvanisheth.twoplayerwordgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import edu.neu.madcourse.dhvanisheth.R;

public class TwoPlayerWordGameLeaderboardScreen extends Activity {

    TwoPlayerWordGameRemoteClient tpwgRemoteClient;

    TextView r1c1;
    TextView r1c2;
    TextView r1c3;
    TextView r2c1;
    TextView r2c2;
    TextView r2c3;
    TextView r3c1;
    TextView r3c2;
    TextView r3c3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.twoplayerwordgame_leaderboard);

        tpwgRemoteClient = new TwoPlayerWordGameRemoteClient(this);

        r1c1 = (TextView) findViewById(R.id.r1c1);
        r1c2 = (TextView) findViewById(R.id.r1c2);
        r1c3 = (TextView) findViewById(R.id.r1c3);
        r2c1 = (TextView) findViewById(R.id.r2c1);
        r2c2 = (TextView) findViewById(R.id.r2c2);
        r2c3 = (TextView) findViewById(R.id.r2c3);
        r3c1 = (TextView) findViewById(R.id.r3c1);
        r3c2 = (TextView) findViewById(R.id.r3c2);
        r3c3 = (TextView) findViewById(R.id.r3c3);



        getDataFromFirebase();


    }

    private void getDataFromFirebase(){

    Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/leaderboard");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List <TwoPlayerWordGameLeaderboard> scorelist = new ArrayList<TwoPlayerWordGameLeaderboard>();
            int i = 0;
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                TwoPlayerWordGameLeaderboard lb = snapshot.getValue(TwoPlayerWordGameLeaderboard.class);

                scorelist.add(i, lb);

                i++;

            }

            Collections.sort(scorelist);
            int scoreIds[] = {R.id.r1c1, R.id.r1c2, R.id.r1c3,
                    R.id.r2c1, R.id.r2c2, R.id.r2c3, R.id.r3c1, R.id.r3c2,
                    R.id.r3c3};

            if(scorelist.size() >= 3){

                TwoPlayerWordGameLeaderboard lb = scorelist.get(0);
                TextView v1 = (TextView) findViewById(scoreIds[0]);
                TextView v2 = (TextView) findViewById(scoreIds[1]);
                TextView v3 = (TextView) findViewById(scoreIds[2]);

                v1.setText(lb.getFullName());
                v2.setText(Integer.toString(lb.getScore()));
                v3.setText(lb.getDateTime());

                TwoPlayerWordGameLeaderboard lb1 = scorelist.get(1);
                TextView v4 = (TextView) findViewById(scoreIds[3]);
                TextView v5 = (TextView) findViewById(scoreIds[4]);
                TextView v6 = (TextView) findViewById(scoreIds[5]);

                v4.setText(lb1.getFullName());
                v5.setText(Integer.toString(lb1.getScore()));
                v6.setText(lb1.getDateTime());

                TwoPlayerWordGameLeaderboard lb2 = scorelist.get(2);
                TextView v7 = (TextView) findViewById(scoreIds[6]);
                TextView v8 = (TextView) findViewById(scoreIds[7]);
                TextView v9 = (TextView) findViewById(scoreIds[8]);

                v7.setText(lb2.getFullName());
                v8.setText(Integer.toString(lb2.getScore()));
                v9.setText(lb2.getDateTime());

            }
            else{

                if(scorelist.size() == 2) {
                    TwoPlayerWordGameLeaderboard lb = scorelist.get(0);
                    TextView v1 = (TextView) findViewById(scoreIds[0]);
                    TextView v2 = (TextView) findViewById(scoreIds[1]);
                    TextView v3 = (TextView) findViewById(scoreIds[2]);

                    v1.setText(lb.getFullName());
                    v2.setText(Integer.toString(lb.getScore()));
                    v3.setText(lb.getDateTime());

                    TwoPlayerWordGameLeaderboard lb1 = scorelist.get(1);
                    TextView v4 = (TextView) findViewById(scoreIds[3]);
                    TextView v5 = (TextView) findViewById(scoreIds[4]);
                    TextView v6 = (TextView) findViewById(scoreIds[5]);

                    v4.setText(lb1.getFullName());
                    v5.setText(Integer.toString(lb1.getScore()));
                    v6.setText(lb1.getDateTime());
                }

                else{
                    if(scorelist.size() == 1) {
                        TwoPlayerWordGameLeaderboard lb = scorelist.get(0);
                        TextView v1 = (TextView) findViewById(scoreIds[0]);
                        TextView v2 = (TextView) findViewById(scoreIds[1]);
                        TextView v3 = (TextView) findViewById(scoreIds[2]);

                        v1.setText(lb.getFullName());
                        v2.setText(Integer.toString(lb.getScore()));
                        v3.setText(lb.getDateTime());

                    }
                    else{

                    }
                }

            }

        }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                if (firebaseError != null) {
                    Log.e("ERROR", firebaseError.getMessage());
                }
            }
        });

    }
}