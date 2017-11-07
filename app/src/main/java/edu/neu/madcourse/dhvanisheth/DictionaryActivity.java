package edu.neu.madcourse.dhvanisheth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import edu.neu.madcourse.dhvanisheth.scraggle.SubmitWord;


public class DictionaryActivity extends Activity {

    private AlertDialog mDialog;

    String submitWordCheck;
    String submitWordSearch;


    InputStream is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);


        final ArrayAdapter<String> adapter;
        final ArrayList<String> arrayList;

//        final InputStream is = getResources().openRawResource(R.raw.wordlist);



        String s1 = "aa";
        String s2 = s1+".txt";
        File f = this.getDir("raw", Context.MODE_PRIVATE);
        String filePath = "/data/data/edu.neu.madcouse.dhvanisheth/raw";


        final SearchFile searcher = SearchFile.getInstance();

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.beep_dictionary);



        View dictionary_clearButton = findViewById(R.id.dictionary_clear_button);
        View dictionary_returnButton = findViewById(R.id.dictionary_return_button);
        View dictionary_acknowledgementsButton = findViewById(R.id.dictionary_acknowledgements_button);
        EditText dictionary_input1 = (EditText) findViewById(R.id.dictionary_input);

        final ListView listView = (ListView) findViewById(R.id.dictionary_list_words);

        arrayList = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);

        listView.setAdapter(adapter);

        final TextWatcher inputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                EditText dictionary_inputText1 = (EditText) findViewById(R.id.dictionary_input);

                String input = dictionary_inputText1.getText().toString();

                try {

                    int i = input.length();

                    if (i > 2) {

                        boolean b;

                        submitWordCheck = input.substring(0, 2);

                        if((submitWordCheck.equals("do"))||(submitWordCheck.equals("if"))){
                            submitWordSearch = submitWordCheck+"1";
                        }
                        else{
                            submitWordSearch = input.substring(0, 2);
                        }
                        is = getResources().openRawResource(
                                getResources().getIdentifier(submitWordSearch,
                                        "raw", getPackageName()));

//                        int fileName123 = getResources().getIdentifier("aa",
//                                "raw", getPackageName());
//                        //getReferrer().getPath()
//                        InputStream is = getResources().openRawResource(
//                                getResources().getIdentifier(input.substring(0, 2),
//                                        "raw", getPackageName()));
                        b = searcher.search(input, is);
                        if (b) {

                            if (!arrayList.contains(input)) {
                                arrayList.add(input);
                                // next thing you have to do is check if your adapter has changed
                                adapter.notifyDataSetChanged();
                                mp.start();
                            }

                        } else {

                        }
                    }

                } catch (Exception x) {
                    System.out.println("Error: " + x);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        dictionary_input1.addTextChangedListener(inputWatcher);

        dictionary_clearButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EditText dictionary_inputText = (EditText) findViewById(R.id.dictionary_input);
                dictionary_inputText.setText("");

                adapter.clear();

               listView.setAdapter(adapter);

            }

        });

        dictionary_returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        dictionary_acknowledgementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = LayoutInflater.from(DictionaryActivity.this);
                View view1 = inflater.inflate(R.layout.acknowledgements_dictionary,null);
                TextView acknowledgements_dictionary = (TextView) view.findViewById(R.id.acknowledgements_dictionary);
                AlertDialog.Builder builder = new AlertDialog.Builder(DictionaryActivity.this);
                builder.setView(view1);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // nothing
                            }
                        });
                mDialog = builder.show();
            }

        });
    }

    @Override
    public void onPause() {
        super.onPause();

        // Get rid of the about dialog if it's still up
        if (mDialog != null)
            mDialog.dismiss();
    }
}