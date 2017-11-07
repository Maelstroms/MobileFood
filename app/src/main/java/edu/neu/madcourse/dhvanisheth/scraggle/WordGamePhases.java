/***
 * /***
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.neu.madcourse.dhvanisheth.R;
import edu.neu.madcourse.dhvanisheth.SearchFile;

public class WordGamePhases extends Activity {

    WordGamePhasesDataHolder dh = new WordGamePhasesDataHolder();
    String FileData = null;

    private MyCountDownTimer countDownTimer;

    final SearchFile searcher = SearchFile.getInstance();

//    private boolean timerStopped = false;
//
//    long millisRemaining;
//
//    int timerIndicator = 0;

    boolean newYN;

    TextView timer_phase1;

    TextView points_phase1;

    View music_on_off_phase1;

    ListView listView;

//    int gridPlayed;
//    int previousGridPlayed;

    TextView scraggle_phase1_label;

    InputStream is;

//    String submitWordCheck;
//
//    String submitWordSearch;

//    BackgroundSound mBackgroundSound = new BackgroundSound();

    private final long startTime = 90000;
    private final long interval = 1000;

    //    int scorephase1 = 0;
//
//    SubmitWord sw = new SubmitWord();
//
//    int count = 1;
//    Integer indexToBlank = 0;
    public static final String PREFS_NAME = "MyPrefsFile_GameOver";

    public static final String PREFS_NAME_WORD_GAME = "MyPrefsFile_WordGame";

//    public static final String KEY_RESTORE = "key_restore";


    MediaPlayer mMediaPlayer;

    int reset = 0;

//    boolean music = true;

    // ...

    //    int phaseNumber = 1;
//    final List<Integer> correctWordGrid = new ArrayList<Integer>();
//    final List<Integer> wrongWordGrid = new ArrayList<Integer>();
//    final List<Integer> notAttemptedGrid = new ArrayList<Integer>();
//    int latestButtonIndex = 0;
    Button previousButton;
    final List<Button> selectedButtonList = new ArrayList<Button>();
    final List<Button> correctWordButtonList = new ArrayList<Button>();
    //
    ArrayAdapter<String> adapter;
//    ArrayList arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        final ArrayAdapter<String> adapter;
//        final ArrayList<String> arrayList;

        SharedPreferences wordGame2 = getSharedPreferences(PREFS_NAME_WORD_GAME, 0);
        boolean restore2 = wordGame2.getBoolean("save_shared_preference", false);

        if(restore2)
            newYN = false;
        else
            newYN = true;

        setContentView(R.layout.scraggle_word_game_phases);


        if (!restore2) {

            for (int i = 0; i < 9; ++i)
                dh.notAttemptedGrid.add(i);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        listView = (ListView) findViewById(R.id.scraggle_phase1_list_words);

        if (!restore2) {
            dh.arrayList = new ArrayList<String>();

            dh.arrayList = new ArrayList<String>();
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, dh.arrayList);

            listView.setAdapter(adapter);

            setTitle("Word Game");


            callfunction(newYN);
        }

        final View submit_word_phase1 = findViewById(R.id.scraggle_ok_button_phase1);
        final View resume_button_phase1 = findViewById(R.id.scraggle_resume_phase1);
        View quit_button_phase1 = findViewById(R.id.scraggle_quit_phase1);
        music_on_off_phase1 = findViewById(R.id.scraggle_music_on_off_phase1);
        final View pause_button_phase1 = findViewById(R.id.scraggle_pause_phase1);


        timer_phase1 = (TextView) findViewById(R.id.scraggle_timer_phase1);
        points_phase1 = (TextView) findViewById(R.id.scraggle_points_phase1);
        scraggle_phase1_label = (TextView) findViewById(R.id.scraggle_phase1_label);

        resume_button_phase1.setEnabled(false);


        music_on_off_phase1.setOnClickListener(new View.OnClickListener() {

            Button music_button = (Button) findViewById(R.id.scraggle_music_on_off_phase1);

            @Override
            public void onClick(View view) {

                if (dh.music) {
                    mMediaPlayer.pause();
                    music_button.setText("Music On");
                    dh.music = false;

                } else {

                    mMediaPlayer = MediaPlayer.create(WordGamePhases.this, R.raw.merge3);
                    mMediaPlayer.setVolume(0.5f, 0.5f);
                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.start();
                    music_button.setText("Music Off");

                    dh.music = true;
                }


            }

        });

        pause_button_phase1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                int mLargeIds1[] = {R.id.large1, R.id.large2, R.id.large3,
                        R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8,
                        R.id.large9,};

                for (int large = 0; large < 9; large++) {
                    View outer1 = findViewById(mLargeIds1[large]);
                    outer1.setVisibility(View.INVISIBLE);


                }

                pause_button_phase1.setEnabled(false);
                resume_button_phase1.setEnabled(true);
                submit_word_phase1.setEnabled(false);
                countDownTimer.cancel();
                dh.timerStopped = true;


            }

        });

        resume_button_phase1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                int mLargeIds1[] = {R.id.large1, R.id.large2, R.id.large3,
                        R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8,
                        R.id.large9,};

                for (int large = 0; large < 9; large++) {
                    View outer1 = findViewById(mLargeIds1[large]);
                    outer1.setVisibility(View.VISIBLE);
                }

                pause_button_phase1.setEnabled(true);
                resume_button_phase1.setEnabled(false);
                submit_word_phase1.setEnabled(true);
                if (dh.timerStopped) {

                    timer_phase1.equals("");
                    countDownTimer = null;
                    countDownTimer = new MyCountDownTimer(dh.millisRemaining, interval);
                    countDownTimer.start();
                }


            }


        });


        quit_button_phase1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                SharedPreferences wordGame = getSharedPreferences(PREFS_NAME_WORD_GAME, 0);
                SharedPreferences.Editor editor_wordGame = wordGame.edit();
                editor_wordGame.clear();
                editor_wordGame.commit();

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();

                editor.putInt("scoreFromPhase1", dh.scorephase1);
                editor.commit();

                Intent intent = new Intent(WordGamePhases.this, GameOverActivity.class);
                startActivity(intent);

                finish();
                return;

            }

        });


        dh.submittedWords = new ArrayList<String>();


        submit_word_phase1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (dh.phaseNumber == 1) {
                    int indexToStore = (dh.latestButtonIndex / 9);
                    dh.indexToBlank = indexToStore;
                    if (dh.notAttemptedGrid.contains(indexToStore))
                        dh.notAttemptedGrid.remove(dh.notAttemptedGrid.indexOf(indexToStore));

                    if (SubmitWord.checkword.length() == 1) {
                        dh.wrongWordGrid.add(indexToStore);

                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(300);
                        MediaPlayer wrongword = MediaPlayer.create(WordGamePhases.this, R.raw.beep_error_scraggle);
                        wrongword.start();

                        dh.sw.reset();

                        changeSelectionForGrid(dh.latestButtonIndex, true);
                        setSelectedButtonList(true);
                        deHighlightSelectedButtons();
                    } else {

                        if (SubmitWord.checkword != null && !(SubmitWord.checkword.isEmpty())) {

                            dh.submitWordCheck = SubmitWord.checkword.substring(0, 2);
                            if ((dh.submitWordCheck.equals("do")) || (dh.submitWordCheck.equals("if"))) {
                                dh.submitWordSearch = SubmitWord.checkword.substring(0, 2) + "1";
                            } else {
                                dh.submitWordSearch = SubmitWord.checkword.substring(0, 2);
                            }
                            is = getResources().openRawResource(
                                    getResources().getIdentifier(dh.submitWordSearch,
                                            "raw", getPackageName()));
                            //b = searcher.search(input, is);
                            if ((searcher.search(SubmitWord.checkword, is))
                                    && !(dh.submittedWords.contains(SubmitWord.checkword))) {

                                dh.correctWordGrid.add(indexToStore);
                                if (dh.wrongWordGrid.contains(indexToStore))
                                    dh.wrongWordGrid.remove(dh.wrongWordGrid.indexOf(indexToStore));

                                correctWordButtonList.addAll(selectedButtonList);

                                setSelectedButtonList(false);
                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(300);

                                dh.submittedWords.add(SubmitWord.checkword);
                                ScoreManager sm = new ScoreManager();
                                dh.scorephase1 = dh.scorephase1 + sm.scoreupdate(SubmitWord.checkword);


                                points_phase1.setText("Score: " + dh.scorephase1);
                                MediaPlayer correctword = MediaPlayer.create(WordGamePhases.this, R.raw.beep_correct_scraggle);
                                correctword.start();

                                dh.arrayList.add(SubmitWord.checkword);
                                // next thing you have to do is check if your adapter has changed
                                adapter.notifyDataSetChanged();

                                dh.sw.reset();

                                callBlankOut();

                            } else {
                                dh.wrongWordGrid.add(indexToStore);

                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(300);
                                MediaPlayer wrongword = MediaPlayer.create(WordGamePhases.this, R.raw.beep_error_scraggle);
                                wrongword.start();

                                dh.sw.reset();

                                changeSelectionForGrid(dh.latestButtonIndex, true);
                                setSelectedButtonList(true);
                                deHighlightSelectedButtons();
                            }

//                            checkGridToDisable();
//                            selectedButtonList.clear();
                        }

//                        checkGridToDisable();
//                        selectedButtonList.clear();
                    }

                    checkGridToDisable();
                    selectedButtonList.clear();
                }

                if (dh.phaseNumber == 2) {
                    int indexToStore = (dh.latestButtonIndex / 9);
                    dh.indexToBlank = indexToStore;

                    if (SubmitWord.checkword.length() == 1) {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                         v.vibrate(300);
                        MediaPlayer wrongword = MediaPlayer.create(WordGamePhases.this, R.raw.beep_error_scraggle);
                        wrongword.start();

                        dh.sw.reset();
                    } else {


                        if (SubmitWord.checkword != null && !(SubmitWord.checkword.isEmpty())) {
                            dh.submitWordCheck = SubmitWord.checkword.substring(0, 2);
                            if ((dh.submitWordCheck.equals("do")) || (dh.submitWordCheck.equals("if"))) {
                                dh.submitWordSearch = SubmitWord.checkword.substring(0, 2) + "1";
                            } else {
                                dh.submitWordSearch = SubmitWord.checkword.substring(0, 2);
                            }
                            is = getResources().openRawResource(
                                    getResources().getIdentifier(dh.submitWordSearch,
                                            "raw", getPackageName()));

                            if ((searcher.search(SubmitWord.checkword, is))
                                    && !(dh.submittedWords.contains(SubmitWord.checkword))) {

                                dh.correctWordGrid.add(indexToStore);

                                correctWordButtonList.addAll(selectedButtonList);

                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(300);

                                dh.submittedWords.add(SubmitWord.checkword);
                                ScoreManager sm = new ScoreManager();
                                dh.scorephase1 = dh.scorephase1 + sm.scoreupdate(SubmitWord.checkword);

                                points_phase1.setText("Score: " + dh.scorephase1);
                                MediaPlayer correctword = MediaPlayer.create(WordGamePhases.this, R.raw.beep_correct_scraggle);
                                correctword.start();

                                dh.arrayList.add(SubmitWord.checkword);
                                // next thing you have to do is check if your adapter has changed
                                adapter.notifyDataSetChanged();

                                dh.sw.reset();

                            } else {

                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(300);
                                MediaPlayer wrongword = MediaPlayer.create(WordGamePhases.this, R.raw.beep_error_scraggle);
                                wrongword.start();

                                dh.sw.reset();

                            }

                        }
                    }

                    dh.previousGridPlayed = -1;
                    previousButton = null;
                    deHighlightSelectedButtons();
                    selectedButtonList.clear();
                }
            }
        });

        if (!restore2) {
            countDownTimer = new MyCountDownTimer(startTime, interval);

            countDownTimer.start();
        }

    }

    public void clearNotAttemptedGrid() {
        for (Integer i : dh.notAttemptedGrid) {

            for (int j = 0; j < 9; j++) {
                Button b = allMyButtons.get((i * 9) + j);
                b.setText("");
            }
        }
    }

    public void clearWrongWordGrid() {
        for (Integer i : dh.wrongWordGrid) {

            for (int j = 0; j < 9; j++) {
                Button b = allMyButtons.get((i * 9) + j);
                b.setText("");
            }
        }
    }

    public void callBlankOut() {

        for (Integer i : dh.correctWordGrid) {

            for (int j = 0; j < 9; j++) {
                Button b = allMyButtons.get((i * 9) + j);

                if (!correctWordButtonList.contains(b)) {
                    b.setText("");
                }

            }
        }
    }


    public void deHighlightSelectedButtons() {
        for (Button b : selectedButtonList) {
            b.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green));
        }
    }

    public void highlightSelectedButton() {
        for (Button b : selectedButtonList) {
            b.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_blue));
        }
    }

    public void setSelectedButtonList(boolean val) {
        for (Button b : selectedButtonList)
            b.setEnabled(val);
    }

    public void checkGridToDisable() {
        for (Integer index : dh.correctWordGrid) {
            for (int i = 0; i < 9; ++i) {
                changeButtonSelection(index, i, false);


            }
        }

        for (Integer index : dh.wrongWordGrid) {
            for (int i = 0; i < 9; ++i) {
                changeButtonSelection(index, i, true);
            }
        }

        for (Integer index : dh.notAttemptedGrid) {
            for (int i = 0; i < 9; ++i) {
                changeButtonSelection(index, i, true);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
      /* Changed the music file */

        mMediaPlayer = MediaPlayer.create(this, R.raw.merge3);
        mMediaPlayer.setVolume(0.5f, 0.5f);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        Log.d("music start", "music =" + dh.music);

        SharedPreferences wordGame1 = getSharedPreferences(PREFS_NAME_WORD_GAME, 0);
        boolean restore = wordGame1.getBoolean("save_shared_preference", false);
        if (restore) {
            loadSavedPreferences();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.stop();

        mMediaPlayer.reset();

        mMediaPlayer.release();

        Log.d("music stop", "music =" + dh.music);

        countDownTimer.cancel();

        savePreferences();

    }


    List<Button> allMyButtons = new ArrayList<Button>();



    public void callfunction(boolean n) {

        if(n){

            InputStream getjumbledword = getResources().openRawResource(R.raw.nineletterwords);
            JumbleWords jw = new JumbleWords();
            WordReader wr = null;
            try {

                wr = new WordReader();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                wr.init(getjumbledword);
            } catch (Exception e) {
                e.printStackTrace();
            }


            int mLargeIds[] = {R.id.large1, R.id.large2, R.id.large3,
                    R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8,
                    R.id.large9,};
            int mSmallIds[] = {R.id.small1, R.id.small2, R.id.small3,
                    R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8,
                    R.id.small9,};

            for (int large = 0; large < 9; large++) {
                View outer = findViewById(mLargeIds[large]);

                String word = wr.getWord();
                String jumbledString[] = jw.JumbledWord(word);

                for (int small = 0; small < 9; small++) {

                    final Button inner = (Button) outer.findViewById
                            (mSmallIds[small]);
                    inner.setText(jumbledString[small]);
                    allMyButtons.add(inner);
                    inner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (dh.phaseNumber == 1) {

                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                //SubmitWord sw = new SubmitWord();
                                dh.sw.setCheckword(inner.getText().toString().charAt(0));
                                v.vibrate(300);

                                int index = allMyButtons.indexOf(inner);
                                DisableAllButton2(index);

                                dh.latestButtonIndex = index;
                                selectedButtonList.add(inner);

                                highlightSelectedButton();
                                setSelectedButtonList(false);
                            }

                            if (dh.phaseNumber == 2) {

                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                                v.vibrate(300);

                                int index = allMyButtons.indexOf(inner);

                                dh.latestButtonIndex = index;
                                dh.gridPlayed = (dh.latestButtonIndex / 9);

                                if ((inner == previousButton) || (dh.gridPlayed == dh.previousGridPlayed)) {

                                    Toast.makeText(WordGamePhases.this, "Not a valid move", Toast.LENGTH_SHORT).show();

                                    //selectedButtonList.clear();
                                    //deHighlightSelectedButtons();

                                } else {

                                    if (!"".equals(inner.getText().toString())) {
                                        dh.sw.setCheckword(inner.getText().toString().charAt(0));
                                        selectedButtonList.add(inner);
                                        highlightSelectedButton();
                                    }
                                }

                                previousButton = inner;
                                dh.previousGridPlayed = dh.gridPlayed;
                            }

                        }
                    });
                }
            }

        }

        else {

            int mLargeIds[] = {R.id.large1, R.id.large2, R.id.large3,
                    R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8,
                    R.id.large9,};
            int mSmallIds[] = {R.id.small1, R.id.small2, R.id.small3,
                    R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8,
                    R.id.small9,};

            for (int large = 0; large < 9; large++) {
                View outer = findViewById(mLargeIds[large]);


                    for (int small = 0; small < 9; small++) {

                        final Button inner = (Button) outer.findViewById
                                (mSmallIds[small]);

                        String property[] = new String[4];
                        property = dh.allButtonList.get((large * 9) + small).split("-");
                        String buttonString = dh.allButtonList.get((large * 9) + small);

                        boolean enabled;

                        if (property[1].equals("1")) {
                            enabled = true;
                        } else {
                            enabled = false;
                        }

                        if (property[0] == " ") {
                            inner.setText("");
                        } else {
                            inner.setText(property[0]);
                        }


                        if (property[2].equals("0")) {
//                            inner.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_blue));
                            inner.setBackgroundResource(R.drawable.button_blue);
                        } else {
//                            inner.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green));
                            inner.setBackgroundResource(R.drawable.button_green);
                        }

                        inner.setEnabled(enabled);

                        allMyButtons.add(inner);

                        if(dh.dhselectedButtonList.contains(buttonString))
                            selectedButtonList.add(inner);

                        if(dh.dhcorrectWordButtonList.contains(buttonString))
                            correctWordButtonList.add(inner);


                        if(dh.dhpreviousbutton.contains(buttonString))
                            previousButton = inner;

                        inner.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (dh.phaseNumber == 1) {

                                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                    //SubmitWord sw = new SubmitWord();
                                    dh.sw.setCheckword(inner.getText().toString().charAt(0));
                                    v.vibrate(300);

                                    int index = allMyButtons.indexOf(inner);
                                    DisableAllButton2(index);

                                    dh.latestButtonIndex = index;
                                    selectedButtonList.add(inner);

                                    highlightSelectedButton();
                                    setSelectedButtonList(false);
                                }

                                if (dh.phaseNumber == 2) {

                                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                                    v.vibrate(300);

                                    int index = allMyButtons.indexOf(inner);

                                    dh.latestButtonIndex = index;
                                    dh.gridPlayed = (dh.latestButtonIndex / 9);

                                    if ((inner == previousButton) || (dh.gridPlayed == dh.previousGridPlayed)) {

                                        Toast.makeText(WordGamePhases.this, "Not a valid move", Toast.LENGTH_SHORT).show();

                                        //selectedButtonList.clear();
                                        //deHighlightSelectedButtons();

                                    } else {

                                        if (!"".equals(inner.getText().toString())) {
                                            dh.sw.setCheckword(inner.getText().toString().charAt(0));
                                            selectedButtonList.add(inner);
                                            highlightSelectedButton();
                                        }
                                    }

                                    previousButton = inner;
                                    dh.previousGridPlayed = dh.gridPlayed;
                                }

                            }
                        });
                    }
                }
            }

    }

    public void DisableAllButton(int i, int j) {
        Button b = allMyButtons.get((i * 9) + j);
        changeAllMyWords(false);

        // Left
        if (j % 3 > 0)
            changeButtonSelection(i, (j - 1), true);

        // Right
        if (j % 3 < 2)
            changeButtonSelection(i, j + 1, true);

        // Up
        if (j >= 3)
            changeButtonSelection(i, j - 3, true);

        // Down
        if (j < 6)
            changeButtonSelection(i, j + 3, true);


        // Right Up diagonal
        if (j == 3 || j == 4 || j == 6 || j == 7)
            changeButtonSelection(i, j - 2, true);

        // Right Down diagonal
        if (j == 0 || j == 1 || j == 3 || j == 4)
            changeButtonSelection(i, j + 4, true);

        // Left Up diagonal
        if (j == 4 || j == 5 || j == 7 || j == 8)
            changeButtonSelection(i, j - 4, true);

        // Left Down diagonal
        if (j == 1 || j == 2 || j == 4 || j == 5)
            changeButtonSelection(i, j + 2, true);
    }

    void changeAllMyWords(boolean val) {
        for (Button b :
                allMyButtons) {
            b.setEnabled(val);
        }
    }

    void changeButtonSelection(int i, int j, boolean val) {
        Button b = allMyButtons.get((i * 9) + j);
        b.setEnabled(val);
    }


    void changeSelectionForGrid(int i, boolean val) {
        i = i / 9;
        for (int j = 0; j < 9; ++j) {
            changeButtonSelection(i,
                    j, val);

        }

    }

    public void DisableAllButton2(int index) {
        int i = index / 9;
        int j = index % 9;


        DisableAllButton(i, j);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // CountDownTimer class
    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }


        @Override
        public void onFinish() {
            if (dh.count == 2) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();

                editor.putInt("scoreFromPhase1", dh.scorephase1);
                editor.commit();

                Intent intent = new Intent(WordGamePhases.this, GameOverActivity.class);
                startActivity(intent);

                dh.scorephase1 = 0;
                finish();
                return;
            } else {

                if (dh.correctWordGrid.isEmpty() || dh.correctWordGrid.size() == 1) {

                    //callanotheractivity;
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putInt("scoreFromPhase1", dh.scorephase1);
                    editor.commit();

                    Intent intent = new Intent(WordGamePhases.this, GameOverActivity.class);
                    startActivity(intent);
                    dh.scorephase1 = 0;
                    finish();
                    return;

                } else {
                    clearNotAttemptedGrid();
                    clearWrongWordGrid();
                    dh.previousGridPlayed = -1;
                    previousButton = null;


                    scraggle_phase1_label.setText("Scraggle Phase 2");

                    if (dh.count == 1) {
                        dh.timerIndicator = 0;
                        dh.count = 2;
                    }
                    dh.phaseNumber = 2;

                    for (int i = 0; i < 9; i++) {

                        for (int j = 0; j < 9; j++) {
                            Button b = allMyButtons.get((i * 9) + j);

                            if (correctWordButtonList.contains(b)) {
                                b.setEnabled(true);
                                b.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green));
                            }

                        }
                    }


                    for (Button b : allMyButtons) {
                        b.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green));
                    }

                    selectedButtonList.clear();
                    correctWordButtonList.clear();

                    Toast.makeText(WordGamePhases.this, "Phase2 Started", Toast.LENGTH_LONG).show();
                    countDownTimer = new MyCountDownTimer(startTime, interval);

                    countDownTimer.start();
                }

            }

        }

        @Override
        public void onTick(long millisUntilFinished) {

            timer_phase1.setText("Seconds Remaining: " + millisUntilFinished / 1000);

            dh.millisRemaining = millisUntilFinished;
            if (millisUntilFinished < 16000 && dh.timerIndicator == 0) {
                Toast.makeText(WordGamePhases.this, "" + millisUntilFinished / 1000 + " seconds remaining", Toast.LENGTH_SHORT).show();
                dh.timerIndicator = 1;
            }
        }

    }

    public void loadSavedPreferences() {

        SharedPreferences wordGame_load = getSharedPreferences(PREFS_NAME_WORD_GAME, 0);

        SharedPreferences.Editor editor_wordGame = wordGame_load.edit();

        allMyButtons.clear();
        previousButton = null;
        correctWordButtonList.clear();
        selectedButtonList.clear();

        dh.music = wordGame_load.getBoolean("save_music_on_off", true);

        dh.scorephase1 = wordGame_load.getInt("save_score", 0);
        dh.phaseNumber = wordGame_load.getInt("save_phase_number", 1);

        if (dh.phaseNumber == 1)
            scraggle_phase1_label.setText("Scraggle Phase 1");
        else
            scraggle_phase1_label.setText("Scraggle Phase 2");


        dh.timerStopped = wordGame_load.getBoolean("save_timer_stopped", false);
        dh.millisRemaining = wordGame_load.getLong("save_millisRemaining", 90000);
        if (dh.timerStopped) {

            timer_phase1.equals("");
            countDownTimer = null;
            countDownTimer = new MyCountDownTimer(dh.millisRemaining, interval);

            timer_phase1.setText("Seconds Remaining: " + dh.millisRemaining / 1000);
            countDownTimer.start();
        } else {
            countDownTimer = new MyCountDownTimer(startTime, interval);
            countDownTimer.start();
        }

        points_phase1.setText("Score: " + dh.scorephase1);

        try {
            dh = (WordGamePhasesDataHolder) ObjectSerializer.deserialize(wordGame_load.getString("save_buttons",
                    ObjectSerializer.serialize(new WordGamePhasesDataHolder())));

            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, dh.arrayList);

            listView.setAdapter(adapter);

            callfunction(false);

//            callfunction(false);

        } catch (Exception e) {
            e.printStackTrace();
            editor_wordGame.clear();
            editor_wordGame.commit();
        }
    }

    public void savePreferences() {

        SharedPreferences wordGame_save = getSharedPreferences(PREFS_NAME_WORD_GAME, 0);
        SharedPreferences.Editor editor_wordGame = wordGame_save.edit();


        int bgColor;
        int enabled;
        String text1;
        int id;
        String combinedButton;
        dh.allButtonList = new ArrayList<String>();
        dh.dhselectedButtonList = new ArrayList<String>();
        dh.dhcorrectWordButtonList = new ArrayList<String>();
        dh.dhpreviousbutton = new ArrayList<String>();

        editor_wordGame.putBoolean("save_music_on_off", dh.music);
        editor_wordGame.putInt("save_phase_number", dh.phaseNumber);
        editor_wordGame.putLong("save_millisRemaining", dh.millisRemaining);
        editor_wordGame.putInt("save_score", dh.scorephase1);

        dh.timerStopped = true;
        editor_wordGame.putBoolean("save_shared_preference", true);

        editor_wordGame.putBoolean("save_timer_stopped", dh.timerStopped);

        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < 9; j++) {
                Button b = allMyButtons.get((i * 9) + j);

                id = b.getId();

                if (b.getBackground().equals(R.drawable.button_blue))
                    bgColor = 0;
                else
                    bgColor = 1;


                if (b.isEnabled()) {
                    enabled = 1;
                } else {
                    enabled = 0;
                }

                text1 = b.getText().toString();
                if (text1.isEmpty()) {
                    text1 = " ";
                }

                combinedButton = text1 + "-" + enabled + "-" + bgColor + "-" + id;
                dh.allButtonList.add(combinedButton);

                if (b == previousButton){
                    dh.dhpreviousbutton.add(combinedButton);
                }

                if (correctWordButtonList.contains(b)){
                    dh.dhcorrectWordButtonList.add(combinedButton);
                }

                if (selectedButtonList.contains(b)){
                    dh.dhselectedButtonList.add(combinedButton);
                }

            }
        }


//        dh.ConvertButtonsToSave(previousButton, selectedButtonList, correctWordButtonList, allMyButtons);
        try {
            editor_wordGame.putString("save_buttons", ObjectSerializer.serialize(dh));
        } catch (Exception e) {
            e.printStackTrace();
            editor_wordGame.clear();
            editor_wordGame.commit();
        }
//        editor_wordGame.apply();
        editor_wordGame.commit();

    }
}
