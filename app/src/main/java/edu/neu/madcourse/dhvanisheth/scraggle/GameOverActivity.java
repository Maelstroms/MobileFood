/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.dhvanisheth.scraggle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.dhvanisheth.R;
import edu.neu.madcourse.dhvanisheth.SearchFile;

public class GameOverActivity extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile_GameOver";
    public static final String PREFS_NAME_WORD_GAME = "MyPrefsFile_WordGame";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scraggle_game_over);

        TextView final_score = (TextView) findViewById(R.id.scraggle_game_over_points);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        int finalScore = 0;
        finalScore = settings.getInt("scoreFromPhase1", 0);

        final_score.setText("" + finalScore);

        View main_menu_button = findViewById(R.id.scraggle_game_over_main_menu_button);

        main_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences wordGame = getSharedPreferences(PREFS_NAME_WORD_GAME, 0);
                SharedPreferences.Editor editor_wordGame = wordGame.edit();
                editor_wordGame.clear();
                editor_wordGame.commit();

                SharedPreferences gameOver = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor_gameOver = gameOver.edit();
                editor_gameOver.clear();
                editor_gameOver.commit();

                finish();
                return;

            }
        });

    }

//    @Override
//    protected void onDestroy() {
//
//        super.onDestroy();
////        SharedPreferences wordGame = getSharedPreferences(PREFS_NAME_WORD_GAME, 0);
////        SharedPreferences.Editor editor_wordGame = wordGame.edit();
////        editor_wordGame.clear();
////        editor_wordGame.commit();
//    }


}