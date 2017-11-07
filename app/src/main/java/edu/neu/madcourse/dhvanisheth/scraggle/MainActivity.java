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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import edu.neu.madcourse.dhvanisheth.GameActivity;
import edu.neu.madcourse.dhvanisheth.R;

public class MainActivity extends Activity {
   MediaPlayer mMediaPlayer;

    private AlertDialog mDialog_acknowledgements;
    private AlertDialog mDialog_instructions;
    public static final String PREFS_NAME_WORD_GAME = "MyPrefsFile_WordGame";
    View scraggle_resume_button;

   // ...

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.scraggle_activity_main);
       setTitle("Word Game");

       View scraggle_new_game_button = findViewById(R.id.scraggle_new_game_button_new);

       View scraggle_acknowledgements_button = findViewById(R.id.scraggle_acknowledgements_button_new);

       View scraggle_quit_button = findViewById(R.id.scraggle_quit_button_new);

       View scraggle_instructions_button = findViewById(R.id.scraggle_instructions_button_new);

       scraggle_resume_button = findViewById(R.id.scraggle_resume_game_button_new);

       SharedPreferences wordGame1 = getSharedPreferences(PREFS_NAME_WORD_GAME, 0);
       boolean restore = wordGame1.getBoolean("save_shared_preference", false);

       if (restore) {
           scraggle_resume_button.setEnabled(true);
       }
       else{
           scraggle_resume_button.setEnabled(false);
       }



//       if (getIntent().getBooleanExtra("Resume Game", false)){
//           scraggle_resume_button.setEnabled(false);
//       }
//       else{
//           scraggle_resume_button.setEnabled(true);
//       }

       scraggle_acknowledgements_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
               View view1 = inflater.inflate(R.layout.scraggle_acknowledgements, null);
               TextView scraggle_acknowledgements = (TextView) view.findViewById(R.id.scraggle_acknowledgements);
               AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
               builder.setView(view1);
               builder.setCancelable(false);
               builder.setPositiveButton(R.string.ok_label,
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               // nothing
                           }
                       });
               mDialog_acknowledgements = builder.show();
           }

       });

       scraggle_instructions_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
               View view1 = inflater.inflate(R.layout.scraggle_instructions, null);
               TextView acknowledgements_instructions = (TextView) view.findViewById(R.id.scraggle_instructions);
               AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
               builder.setView(view1);
               builder.setCancelable(false);
               builder.setPositiveButton(R.string.ok_label,
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               // nothing
                           }
                       });
               mDialog_instructions = builder.show();
           }

       });

       scraggle_quit_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               SharedPreferences wordGame = getSharedPreferences(PREFS_NAME_WORD_GAME, 0);
               SharedPreferences.Editor editor_wordGame = wordGame.edit();
               editor_wordGame.clear();
               editor_wordGame.commit();
               finish();
               return;
           }
       });

       scraggle_resume_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this, WordGamePhases.class);
               intent.putExtra("Resume Game", true);
               startActivity(intent);
           }
       });


       scraggle_new_game_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               SharedPreferences wordGame = getSharedPreferences(PREFS_NAME_WORD_GAME, 0);
               SharedPreferences.Editor editor_wordGame = wordGame.edit();
               editor_wordGame.clear();
               editor_wordGame.commit();

//             SharedPreferences wordGame_load = getSharedPreferences(PREFS_NAME_WORD_GAME, 0);
               Intent intent = new Intent(MainActivity.this, WordGamePhases.class);
               startActivity(intent);
           }
       });

   }



   @Override
   protected void onResume() {
      super.onResume();
      /* Changed the music file */
      mMediaPlayer = MediaPlayer.create(this, R.raw.let_the_tournament_begin_scraggle_main);
      mMediaPlayer.setVolume(0.5f, 0.5f);
      mMediaPlayer.setLooping(true);
      mMediaPlayer.start();
   }

   @Override
   protected void onPause() {
      super.onPause();
      mMediaPlayer.stop();
      mMediaPlayer.reset();
      mMediaPlayer.release();
      // Get rid of the about dialog if it's still up
      if (mDialog_acknowledgements != null)
         mDialog_acknowledgements.dismiss();

       if (mDialog_instructions != null)
           mDialog_instructions.dismiss();
   }

    @Override
    protected void onDestroy() {

        super.onDestroy();
//        SharedPreferences wordGame = getSharedPreferences(PREFS_NAME_WORD_GAME, 0);
//        SharedPreferences.Editor editor_wordGame = wordGame.edit();
//        editor_wordGame.clear();
//        editor_wordGame.commit();
    }

    @Override
    protected void onStart() {

        super.onStart();
        SharedPreferences wordGame1 = getSharedPreferences(PREFS_NAME_WORD_GAME, 0);
        boolean restore = wordGame1.getBoolean("save_shared_preference", false);

        if (restore) {
            scraggle_resume_button.setEnabled(true);
        }
        else{
            scraggle_resume_button.setEnabled(false);
        }

    }

}
