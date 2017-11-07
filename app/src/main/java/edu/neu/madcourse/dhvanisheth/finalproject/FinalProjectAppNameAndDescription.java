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
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.dhvanisheth.R;
import edu.neu.madcourse.dhvanisheth.trickiestpart.FilterConvertedText;
import edu.neu.madcourse.dhvanisheth.twoplayerwordgame.TwoPlayerWordGameSelectPlayer;

public class FinalProjectAppNameAndDescription extends Activity {

    TextView appName;
    TextView appDesc;
    Button startFinalProj;
    Button acknowledgements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finalproject_app_description_screen);

        appName = (TextView) findViewById(R.id.finalProjAppName);
        appDesc = (TextView) findViewById(R.id.finalProjAppDesc);
        startFinalProj = (Button) findViewById(R.id.finalProjStartButton);
        acknowledgements = (Button) findViewById(R.id.finalProjAcknowledgementsButton);

//        appName.setText(getResources().getString(R.string.final_project_app_name));
//
//        appDesc.setText(getResources().getString(R.string.final_project_app_desc));

        startFinalProj.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FinalProjectAppNameAndDescription.this, FinalProjectMainActivity.class);
                startActivity(intent);

            }
        });


        acknowledgements.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


            }
        });


    }

}
