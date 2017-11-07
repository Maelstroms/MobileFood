/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.dhvanisheth.trickiestpart;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Stack;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import edu.neu.madcourse.dhvanisheth.R;

public class TrickiestPartMainActivity extends Activity {

    private TextView returnedText;
    private ToggleButton toggleButton;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "SpeechRecognitionActivity";

    public String[] splitResultMain;
    public ArrayList<String> itemList;
    public ArrayList<String> myItems;

    InputStream is;

    TextView touchToStart;
    ListView listView;
    Context context;
    List<String> fileData;

    MyListAdapter myListAdapter;
    Button speakButton;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trickiestpart_activity_main);
//        returnedText = (TextView) findViewById(R.id.textView3);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
//        touchToStart = (TextView) findViewById(R.id.touchToStart);

        listView = (ListView) findViewById(R.id.listViewMain);
        speakButton = (Button) findViewById(R.id.speakButton);

        itemList = new ArrayList<>();


        myItems = new ArrayList<String>();

//        touchToStart.setText("Touch to Start");

        is = getResources().openRawResource(R.raw.list);
        try{
            fileData = FilterConvertedText.getFileData(is);
        }catch(Exception e){
        }

        splitResultMain = new String[500];

        myListAdapter = new MyListAdapter(this, R.layout.trickiestpart_listview_item, myItems);
        listView.setAdapter(myListAdapter);


//        progressBar.setVisibility(View.INVISIBLE);
//        speech = SpeechRecognizer.createSpeechRecognizer(this);
//        speech.setRecognitionListener(this);
//        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
//                "en");
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
//                this.getPackageName());
//
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
//
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
//
//        toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if (isChecked) {
//                    progressBar.setVisibility(View.VISIBLE);
//                    progressBar.setIndeterminate(true);
//                    speech.startListening(recognizerIntent);
//                    touchToStart.setText("");
//                } else {
//                    progressBar.setIndeterminate(false);
//                    progressBar.setVisibility(View.INVISIBLE);
//                    speech.stopListening();
//                    touchToStart.setText("Touch to Start");
//                }
//            }
//        });

        speakButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                promptSpeechInput();
            }
        });


    }

//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (speech != null) {
//            speech.destroy();
//            Log.i(LOG_TAG, "destroy");
//        }
//
//    }
//
//    @Override
//    public void onBeginningOfSpeech() {
//        Log.i(LOG_TAG, "onBeginningOfSpeech");
//        progressBar.setIndeterminate(false);
//        progressBar.setMax(10);
//    }
//
//    @Override
//    public void onBufferReceived(byte[] buffer) {
//        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
//    }
//
//    @Override
//    public void onEndOfSpeech() {
//        Log.i(LOG_TAG, "onEndOfSpeech");
//        progressBar.setIndeterminate(true);
//        toggleButton.setChecked(false);
//    }
//
//    @Override
//    public void onError(int errorCode) {
//        String errorMessage = getErrorText(errorCode);
//        Log.d(LOG_TAG, "FAILED " + errorMessage);
//        returnedText.setText(errorMessage);
//        toggleButton.setChecked(false);
//    }
//
//    @Override
//    public void onEvent(int arg0, Bundle arg1) {
//        Log.i(LOG_TAG, "onEvent");
//    }
//
//    @Override
//    public void onPartialResults(Bundle arg0) {
//        Log.i(LOG_TAG, "onPartialResults");
//    }
//
//    @Override
//    public void onReadyForSpeech(Bundle arg0) {
//        Log.i(LOG_TAG, "onReadyForSpeech");
//    }
//
//    @Override
//    public void onResults(Bundle results) {
//
//        Log.i(LOG_TAG, "onResults");
//        ArrayList<String> matches = results
//                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//        String text = "";
//        Log.d(LOG_TAG, "arraylist " + matches);
//
//        String result = matches.get(0);
//        String[] splitResult = result.split("\\s+");
//
//            for(int i = 0; i < splitResult.length; i++) {
//
//                if(fileData.contains(splitResult[i].toLowerCase())){
//                    text += splitResult[i] + "\n";
//                    splitResultMain[i] = splitResult[i];
//
//                    itemList.add(splitResult[i]);
//                    myItems.add(splitResult[i]);
//                }
//            }
//        if(myItems!=null) {
//            myListAdapter.notifyDataSetChanged();
//        }
//
//        File file = getFileStreamPath("test.txt");
//
//        try {
//            if (!file.exists()) {
//                Log.d(LOG_TAG, "creating file");
//                file.createNewFile();
//            }
//            Log.d(LOG_TAG, "file is "+ file.toString());
//            FileOutputStream writer = openFileOutput(file.getName(), Context.MODE_PRIVATE);
//
//            for(int i = 0; i < splitResult.length; i++) {
//                Log.d(LOG_TAG, "writing " + splitResult[i]);
//                String edited = (splitResult[i])+"\n";
//                writer.write(edited.getBytes());
//                writer.flush();
//            }
//
//            writer.close();
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//
//        FileInputStream reader;
//        String word = "failed to load";
//
//        try{
//            if (file.exists()) {
//                Log.d(LOG_TAG, "found file");
//                reader = openFileInput(file.getName());
//                BufferedReader homer = new BufferedReader(new InputStreamReader(reader));
//                word = homer.readLine();
//                int stopreadingFile = 0;
//                while (word != null || (stopreadingFile > 10)){
//                    Log.d(LOG_TAG, "file "+ word);
//                    word = homer.readLine();
//                }
//            }
//
//        }
//        catch (FileNotFoundException e){
//            e.printStackTrace();
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//
//
//    }
//
//    @Override
//    public void onRmsChanged(float rmsdB) {
//        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
//        progressBar.setProgress((int) rmsdB);
//    }
//
//    public static String getErrorText(int errorCode) {
//        String message;
//        switch (errorCode) {
//            case SpeechRecognizer.ERROR_AUDIO:
//                message = "Audio recording error";
//                break;
//            case SpeechRecognizer.ERROR_CLIENT:
//                message = "Client side error";
//                break;
//            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
//                message = "Insufficient permissions";
//                break;
//            case SpeechRecognizer.ERROR_NETWORK:
//                message = "Network error";
//                break;
//            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
//                message = "Network timeout";
//                break;
//            case SpeechRecognizer.ERROR_NO_MATCH:
//                message = "No match";
//                break;
//            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
//                message = "RecognitionService busy";
//                break;
//            case SpeechRecognizer.ERROR_SERVER:
//                message = "error from server";
//                break;
//            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
//                message = "No speech input";
//                break;
//            default:
//                message = "Didn't understand, please try again.";
//                break;
//        }
//        return message;
//    }

    private void promptSpeechInput() {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
//                getString(R.string.speech_prompt));

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));

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

//                    ArrayList<String> result = data
//                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    txtSpeechInput.setText(result.get(0));

                    ArrayList<String> matches = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String text = "";
                    Log.d(LOG_TAG, "arraylist " + matches);

                    String result = matches.get(0);
                    String[] splitResult = result.split("\\s+");

                    for(int i = 0; i < splitResult.length; i++) {

                        if(fileData.contains(splitResult[i].toLowerCase())){
                            text += splitResult[i] + "\n";
                            splitResultMain[i] = splitResult[i];

                            itemList.add(splitResult[i]);
                            myItems.add(splitResult[i]);
                        }
                    }
                    if(myItems!=null) {
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

                        for(int i = 0; i < splitResult.length; i++) {
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




}
