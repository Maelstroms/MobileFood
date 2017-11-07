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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

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

public class FinalProjectMainActivity extends Activity {

    private Intent recognizerIntent;
    private String LOG_TAG = "FinalProjectSpeechRecognitionActivity";

    FinalProjectItemListDataHolder il = new FinalProjectItemListDataHolder();
//    public ArrayList<String> myItems;

    InputStream is;

    ListView listView;

    List<String> fileData;

    MyItemListAdapter myListAdapter;
    Button speakButton;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    ImageButton settingsButton;
    Button discardButton;
    Button acceptButton;
    ImageButton addMoreButton;
    Boolean recordButton;

    public static final String PREFS_FINAL_PROJ_FIRST_SCREEN = "MyPrefsFile_FinalProject_FirstScreen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finalproject_activity_main);

        listView = (ListView) findViewById(R.id.finalProjListViewMain);
        speakButton = (Button) findViewById(R.id.finalProjSpeakButton);
        settingsButton = (ImageButton) findViewById(R.id.finalProjSettings);
        discardButton = (Button) findViewById(R.id.finalProjDiscardButton);
        acceptButton = (Button) findViewById(R.id.finalProjAcceptButton);
        addMoreButton = (ImageButton) findViewById(R.id.finalProjAddMoreButton);

//        myItems = new ArrayList<String>();
        final SharedPreferences prefs = getSharedPreferences(PREFS_FINAL_PROJ_FIRST_SCREEN, 0);
        boolean restore2 = prefs.getBoolean("save", false);
        recordButton = false;
        if(!restore2){
            il.myItems = new ArrayList<String>();
            myListAdapter = new MyItemListAdapter(this, R.layout.finalproject_listview_item, il.myItems);
            listView.setAdapter(myListAdapter);
        }

        settingsButton.setImageResource(R.drawable.ic_settings_black_18dp);
        addMoreButton.setImageResource(R.drawable.ic_add_black_18dp);

        is = getResources().openRawResource(R.raw.list);

        try{
            fileData = FilterConvertedText.getFileData(is);
        }catch(Exception e){
        }

        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinalProjectMainActivity.this, FinalProjectSettings.class);
                startActivity(intent);
            }
        });

        speakButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                recordButton = true;
                promptSpeechInput();
            }
        });

        addMoreButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                il.myItems.add(" ");
                myListAdapter.notifyDataSetChanged();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                savePreferences();
                Intent intent = new Intent(FinalProjectMainActivity.this, FinalProjectEditScreen.class);
                startActivity(intent);
            }
        });

        discardButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("saveList");
                editor.remove("save");
                editor.commit();
                il.myItems.clear();
                myListAdapter.notifyDataSetChanged();
            }
        });
    }


    private void promptSpeechInput() {

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        try {
            startActivityForResult(recognizerIntent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> matches = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    Log.d(LOG_TAG, "arraylist " + matches);

                    String result = matches.get(0);
                    String[] splitResult = result.split("\\s+");

                    for(int i = 0; i < splitResult.length; i++) {

                        if(fileData.contains(splitResult[i].toLowerCase())){
                            il.myItems.add(splitResult[i]);
                        }
                    }
                    if(il.myItems!=null) {
                        myListAdapter.notifyDataSetChanged();
                    }

                    File file = getFileStreamPath("test.txt");

                    try {
                        if (!file.exists()) {
                            Log.d(LOG_TAG, "creating file");
                            file.createNewFile();
                        }
                        Log.d(LOG_TAG, "file is "+ file.toString());
                        FileOutputStream writer = openFileOutput(file.getName(), Context.MODE_PRIVATE);

//                        for(int i = 0; i < splitResult.length; i++) {
//                            Log.d(LOG_TAG, "writing " + splitResult[i]);
//                            String edited = (splitResult[i])+"\n";
//                            writer.write(edited.getBytes());
//                            writer.flush();
//                        }

                        for(int i = 0; i < il.myItems.size(); i++) {
                            Log.d(LOG_TAG, "writing " + splitResult[i]);
                            String edited = (splitResult[i])+"\n";
                            writer.write(edited.getBytes());
                            writer.flush();
                        }

                        writer.close();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }

                    FileInputStream reader;
                    String word = "failed to load";

                    try{
                        if (file.exists()) {
                            Log.d(LOG_TAG, "found file");
                            reader = openFileInput(file.getName());
                            BufferedReader homer = new BufferedReader(new InputStreamReader(reader));
                            word = homer.readLine();
                            int stopreadingFile = 0;
                            while (word != null || (stopreadingFile > 10)){
                                Log.d(LOG_TAG, "file "+ word);
                                word = homer.readLine();
                            }
                        }

                    }
                    catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                break;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(PREFS_FINAL_PROJ_FIRST_SCREEN, 0);
        boolean restore = prefs.getBoolean("save", false);
        if (restore && !recordButton) {
            loadSavedPreferences();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(!recordButton){
            savePreferences();
        }


    }

    public void loadSavedPreferences(){

        SharedPreferences prefs = getSharedPreferences(PREFS_FINAL_PROJ_FIRST_SCREEN, 0);
        Gson gson = new Gson();
        String saveList = prefs.getString("saveList", "");
        FinalProjectItemListDataHolder dh = gson.fromJson(saveList, FinalProjectItemListDataHolder.class);
        il.myItems = dh.myItems;
        myListAdapter = new MyItemListAdapter(this, R.layout.finalproject_listview_item, il.myItems);
        listView.setAdapter(myListAdapter);

    }

    public void savePreferences(){

        String json = "";
        Gson gson = new Gson();
        json =  gson.toJson(il);
        SharedPreferences prefs = getSharedPreferences(PREFS_FINAL_PROJ_FIRST_SCREEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Boolean save;
        save = true;
        editor.putString("saveList",json);
        editor.putBoolean("save", save);
        editor.commit();

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        recordButton = false;
    }



}
