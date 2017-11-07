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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.neu.madcourse.dhvanisheth.R;

public class TwoPlayerWordGameActivityGameOver extends Activity {

    public static final String PREFS_NAME_TPWG = "MyPrefsFile_GameOver_TPWG";
    public static final String PREFS_NAME_WORD_GAME_TPWG = "MyPrefsFile_WordGame_TPWG";
    public static final String PREFS_TWO_PLAYER_WORD_GAME = "MyPrefsFile_TwoPlayerWordGame";
    private static final String PROPERTY_OPPONENT_NAME = "opponent_name";
    private static final String PROPERTY_OPPONENT_REG_ID = "opponent_reg_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.twoplayerwordgame_game_over);

        TextView final_score = (TextView) findViewById(R.id.tpwg_game_over_points_player1);

        TextView score_player2 = (TextView) findViewById(R.id.tpwg_game_over_points_player2);

        TextView name_player1 = (TextView) findViewById(R.id.tpwg_game_over_name_player1);

        TextView name_player2 = (TextView) findViewById(R.id.tpwg_game_over_name_player2);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME_TPWG, 0);
        int finalScore = 0;
        String scorePlayer2;
        String namePlayer1;
        String namePlayer2;
        finalScore = settings.getInt("scoreFromPhase1", 0);
        scorePlayer2 = settings.getString("opponentScore", "0");
        int i = Integer.parseInt(scorePlayer2);
        namePlayer1 = settings.getString("userName", "");
        namePlayer2 = settings.getString("opponentName", "");

        final_score.setText("" + finalScore);
        score_player2.setText("" + i);
        name_player1.setText(namePlayer1);
        name_player2.setText(namePlayer2);

        View main_menu_button = findViewById(R.id.tpwg_game_over_main_menu_button);

        SharedPreferences prefs = getSharedPreferences(PREFS_TWO_PLAYER_WORD_GAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_prefs = prefs.edit();
        editor_prefs.remove(PROPERTY_OPPONENT_NAME);
        editor_prefs.remove(PROPERTY_OPPONENT_REG_ID);

        SharedPreferences wordGame = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
        SharedPreferences.Editor editor_wordGame = wordGame.edit();
        editor_wordGame.clear();
        editor_wordGame.commit();

        SharedPreferences gameOver = getSharedPreferences(PREFS_NAME_TPWG, 0);
        SharedPreferences.Editor editor_gameOver = gameOver.edit();
        editor_gameOver.clear();
        editor_gameOver.commit();


        main_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences wordGame = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
                SharedPreferences.Editor editor_wordGame = wordGame.edit();
                editor_wordGame.clear();
                editor_wordGame.commit();

                SharedPreferences gameOver = getSharedPreferences(PREFS_NAME_TPWG, 0);
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