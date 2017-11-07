/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.dhvanisheth.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.neu.madcourse.dhvanisheth.R;

public class FinalProjectSettings extends Activity {

    EditText days;
    EditText hour;
    EditText min;
    public static final String PREFS_FINAL_PROJ_FIRST_SCREEN = "MyPrefsFile_FinalProject_FirstScreen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finalproject_settings);


        days = (EditText) findViewById(R.id.finalProjSettingsDefNoOfDaysValue);
        hour = (EditText) findViewById(R.id.finalProjSettingsDefTimeValueHour);
        min = (EditText) findViewById(R.id.finalProjSettingsDefTimeValueMin);

        SharedPreferences prefs = getSharedPreferences(PREFS_FINAL_PROJ_FIRST_SCREEN, 0);
        String daysSharedPrefs = prefs.getString("daysSharedPrefs", "7");
        String hourSharedPrefs = prefs.getString("hourSharedPrefs", "12");
        String minSharedPrefs = prefs.getString("minSharedPrefs", "00");


        days.setText(daysSharedPrefs);
        hour.setText(hourSharedPrefs);
        min.setText(minSharedPrefs);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(PREFS_FINAL_PROJ_FIRST_SCREEN, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("daysSharedPrefs", days.getText().toString());
        editor.putString("hourSharedPrefs", hour.getText().toString());
        editor.putString("minSharedPrefs", min.getText().toString());
        editor.commit();
    }

}
