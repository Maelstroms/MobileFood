/***
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

import com.google.gson.Gson;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.neu.madcourse.dhvanisheth.R;
import edu.neu.madcourse.dhvanisheth.SearchFile;
import edu.neu.madcourse.dhvanisheth.scraggle.JumbleWords;
import edu.neu.madcourse.dhvanisheth.scraggle.ObjectSerializer;
import edu.neu.madcourse.dhvanisheth.scraggle.ScoreManager;
import edu.neu.madcourse.dhvanisheth.scraggle.SubmitWord;
import edu.neu.madcourse.dhvanisheth.scraggle.WordGamePhasesDataHolder;
import edu.neu.madcourse.dhvanisheth.scraggle.WordReader;

public class TwoPlayerWordGamePhases extends Activity {

    WordGamePhasesDataHolder dh = new WordGamePhasesDataHolder();
    String FileData = null;

    private MyCountDownTimer countDownTimer;

    final SearchFile searcher = SearchFile.getInstance();

//    private boolean timerStopped = false;
//
//    long millisRemaining;
//
//    int timerIndicator = 0;

    boolean submitWordSuccess;
    boolean newYN;
    private BroadcastReceiver mReceiver;

    TextView timer_player1;

    TextView points_player1;

    View music_on_off_phase1;

    ListView listView;

//    int gridPlayed;
//    int previousGridPlayed;

    TextView scraggle_phase1_label;
    TextView tpwg_phase_player2_score;
    TextView tpwg_phase_player2_time_rem;
    View submit_word_phase1;

    InputStream is;

    IntentFilter gcmFilter;

    private String score;

//    String submitWordCheck;
//
//    String submitWordSearch;

//    BackgroundSound mBackgroundSound = new BackgroundSound();

    String storeDH;

    private final long startTime = 90000;
    private final long interval = 1000;

    //    int scorephase1 = 0;
//
//    SubmitWord sw = new SubmitWord();
//
//    int count = 1;
//    Integer indexToBlank = 0;
    public static final String PREFS_NAME_TPWG = "MyPrefsFile_GameOver_TPWG";

    public static final String PREFS_NAME_WORD_GAME_TPWG = "MyPrefsFile_WordGame_TPWG";

//    public static final String KEY_RESTORE = "key_restore";

    public static final String PREFS_TWO_PLAYER_WORD_GAME = "MyPrefsFile_TwoPlayerWordGame";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_STORED_YES_NO = "stored_name_yes_no";
    private static final String PROPERTY_STORED_NAME = "stored_name";
    private static final String PROPERTY_OPPONENT_NAME = "opponent_name";
    private static final String PROPERTY_OPPONENT_REG_ID = "opponent_reg_id";

    TextView userNameDisplay;
    TextView opponentNameDisplay;


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

    boolean myTurn;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private AccelerometerSensorDetection mAccelerometerDetector;

    String gameOnFromIntent;
    String gameDataFromIntent;

    String timer;
    boolean isTimerTrue;
    String timerFromIntent;

    boolean sendNotificationForPhase2;

    Context context;
    CheckInternetConnectivity check;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences wordGame2 = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
        boolean restore2 = wordGame2.getBoolean("save_shared_preference", false);

        SharedPreferences prefs1 = getSharedPreferences(PREFS_TWO_PLAYER_WORD_GAME, Context.MODE_PRIVATE);
        String opponentName = prefs1.getString(PROPERTY_OPPONENT_NAME, "");
        String userName = prefs1.getString(PROPERTY_STORED_NAME, "");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        timerFromIntent = "false";
        gameOnFromIntent = extras.getString("gameOn");
        gameDataFromIntent = extras.getString("gameData");
        timerFromIntent = extras.getString("timer");

       myTurn = true;

        timer = "false";

        isTimerTrue = false;

        sendNotificationForPhase2 = false;

        final TwoPlayerWordGameRemoteClient tpwg_RemoteClient =
                new TwoPlayerWordGameRemoteClient(this);

        context = getApplicationContext();
        check = new CheckInternetConnectivity();


        if(restore2)
            newYN = false;
        else
            newYN = true;

        setContentView(R.layout.twoplayerwordgame_game_phases);

        if (!restore2) {

            for (int i = 0; i < 9; ++i)
                dh.notAttemptedGrid.add(i);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        listView = (ListView) findViewById(R.id.tpwg_phase_list_words);

        if (!restore2) {
            dh.arrayList = new ArrayList<String>();

            dh.arrayList = new ArrayList<String>();
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, dh.arrayList);

            listView.setAdapter(adapter);

            setTitle("Two Player Word Game");


            if(gameOnFromIntent != null && gameOnFromIntent.equals("true")){

            }
            else {
                callfunction(newYN); //call callfunction(true) here
            }
        }

        submit_word_phase1 = findViewById(R.id.tpwg_phase_submit_button);
        final View resume_button_phase1 = findViewById(R.id.tpwg_resume_button);
        View quit_button_phase1 = findViewById(R.id.tpwg_phase_quit_button);
        music_on_off_phase1 = findViewById(R.id.tpwg_phase_music_on_off);
        final View pause_button_phase1 = findViewById(R.id.tpwg_phase_pause_button);
        tpwg_phase_player2_time_rem = (TextView) findViewById(R.id.tpwg_phase_player2_time_rem);


        timer_player1 = (TextView) findViewById(R.id.tpwg_phase_player1_time_rem);
        points_player1 = (TextView) findViewById(R.id.tpwg_phase_player1_score);
        scraggle_phase1_label = (TextView) findViewById(R.id.tpwg_phase_label);

        tpwg_phase_player2_score = (TextView) findViewById(R.id.tpwg_phase_player2_score);

        userNameDisplay = (TextView) findViewById(R.id.tpwg_phase_player1_name);
        opponentNameDisplay = (TextView) findViewById(R.id.tpwg_phase_player2_name);


        userNameDisplay.setText(userName);
        opponentNameDisplay.setText(opponentName);

        resume_button_phase1.setEnabled(false);


//        if (gameOnFromIntent!=null && gameOnFromIntent.equals("true")){
//
//            loadFromNotification(gameDataFromIntent, gameOnFromIntent);
//        }

        if ((gameOnFromIntent!=null && gameOnFromIntent.equals("true")) ||
                (gameOnFromIntent!=null && gameOnFromIntent.equals("false"))){

            loadFromNotification(gameDataFromIntent, gameOnFromIntent);
        }

        music_on_off_phase1.setOnClickListener(new View.OnClickListener() {

            Button music_button = (Button) findViewById(R.id.tpwg_phase_music_on_off);

            @Override
            public void onClick(View view) {

                if (dh.music) {
                    mMediaPlayer.pause();
                    music_button.setText("Music On");
                    dh.music = false;

                } else {

                    mMediaPlayer = MediaPlayer.create(TwoPlayerWordGamePhases.this, R.raw.merge3);
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

                    timer_player1.equals("");
                    countDownTimer = null;
                    countDownTimer = new MyCountDownTimer(dh.millisRemaining, interval);
                    countDownTimer.start();
                }


            }


        });


        quit_button_phase1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (check.CheckConnectivity(context)) {

                    SharedPreferences prefs = getSharedPreferences(PREFS_TWO_PLAYER_WORD_GAME, Context.MODE_PRIVATE);
                    String opponentRegId = prefs.getString(PROPERTY_OPPONENT_REG_ID, "");
                    String opponentName = prefs.getString(PROPERTY_OPPONENT_NAME, "");
                    String userName = prefs.getString(PROPERTY_STORED_NAME, "");


                /* send message that the other player quit */
                    String quit = "true";
                    sendMessageForQuit(quit, opponentRegId, userName);


                /* handle firebase - leaderboard and online */

                    int score = dh.scorephase1;
                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.US).format(new Date());
                    tpwg_RemoteClient.saveScore(userName, userName, score, timeStamp);
                    tpwg_RemoteClient.updateStatus(userName, userName, true);
                    tpwg_RemoteClient.updateStatus(opponentName, opponentName, true);


                    SharedPreferences.Editor editor_prefs = prefs.edit();
//                    editor_prefs.remove(PROPERTY_OPPONENT_NAME);
//                    editor_prefs.remove(PROPERTY_OPPONENT_REG_ID);

                    SharedPreferences wordGame = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
                    SharedPreferences.Editor editor_wordGame = wordGame.edit();
//                    editor_wordGame.clear();
//                    editor_wordGame.commit();

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME_TPWG, 0);
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putInt("scoreFromPhase1", dh.scorephase1);
                    editor.putString("userName", userName);
                    editor.putString("opponentName", opponentName);
                    String opponentScore = tpwg_phase_player2_score.getText().toString();
                    editor.putString("opponentScore", opponentScore);
                    editor.commit();

                    Intent intent = new Intent(TwoPlayerWordGamePhases.this, TwoPlayerWordGameActivityGameOver.class);
                    startActivity(intent);

                    finish();
                    return;

                }
                else {
                    Toast.makeText(context, "Can't proceed. No Internet connection available right now.", Toast.LENGTH_LONG).show();
                }

            }

        });


        dh.submittedWords = new ArrayList<String>();

        submit_word_phase1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (dh.phaseNumber == 1) {

                        int indexToStore = (dh.latestButtonIndex / 9);
                        dh.indexToBlank = indexToStore;

                    if (check.CheckConnectivity(context)) {
                        if (dh.notAttemptedGrid.contains(indexToStore))
                            dh.notAttemptedGrid.remove(dh.notAttemptedGrid.indexOf(indexToStore));

                        if (SubmitWord.checkword.length() == 1) {
                            dh.wrongWordGrid.add(indexToStore);

                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(300);
                            MediaPlayer wrongword = MediaPlayer.create(TwoPlayerWordGamePhases.this, R.raw.beep_error_scraggle);
                            wrongword.start();

                            dh.sw.reset();

                            changeSelectionForGrid(dh.latestButtonIndex, true);
                            setSelectedButtonList(true);
                            deHighlightSelectedButtons();
                            submitWordSuccess = false;
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
                                    points_player1.setText("" + dh.scorephase1);
                                    MediaPlayer correctword = MediaPlayer.create(TwoPlayerWordGamePhases
                                            .this, R.raw.beep_correct_scraggle);
                                    correctword.start();
                                    dh.arrayList.add(SubmitWord.checkword);
                                    // next thing you have to do is check if your adapter has changed
                                    adapter.notifyDataSetChanged();
                                    dh.sw.reset();
                                    callBlankOut();
                                    submitWordSuccess = true;

                                } else {
                                    dh.wrongWordGrid.add(indexToStore);

                                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                    v.vibrate(300);
                                    MediaPlayer wrongword = MediaPlayer.create(TwoPlayerWordGamePhases
                                            .this, R.raw.beep_error_scraggle);
                                    wrongword.start();

                                    dh.sw.reset();

                                    changeSelectionForGrid(dh.latestButtonIndex, true);
                                    setSelectedButtonList(true);
                                    deHighlightSelectedButtons();
                                    submitWordSuccess = false;
                                }
                            }
                            else{
                                submitWordSuccess = false;
                            }
                        }

                        checkGridToDisable();
                        selectedButtonList.clear();

//                    if((submitWordSuccess) && (!isTimerTrue)) {
//                        //new code for tpwg
//                        submit_word_phase1.setEnabled(false);
//                        myTurn = false;
//                        saveAndSendPreferences();
//                    }

                        String tpwg_player2_time_rem = tpwg_phase_player2_time_rem.getText().toString();
                        if((submitWordSuccess) && !(tpwg_player2_time_rem.equals("1"))) {
                            //new code for tpwg
                            submit_word_phase1.setEnabled(false);
                            myTurn = false;
                            saveAndSendPreferences();
                        }
                    }
                    else {

                        checkGridToDisable();

                        dh.sw.reset();

                        changeSelectionForGrid(dh.latestButtonIndex, true);
                        setSelectedButtonList(true);
                        deHighlightSelectedButtons();
                        selectedButtonList.clear();
                        submitWordSuccess = false;
                        Toast.makeText(context, "Can't proceed. No Internet connection available right now.", Toast.LENGTH_LONG).show();
                    }


                }

                if (dh.phaseNumber == 2) {
                    int indexToStore = (dh.latestButtonIndex / 9);
                    dh.indexToBlank = indexToStore;

                    if (check.CheckConnectivity(context)) {

                        if (SubmitWord.checkword.length() == 1) {
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(300);
                            MediaPlayer wrongword = MediaPlayer.create(TwoPlayerWordGamePhases
                                    .this, R.raw.beep_error_scraggle);
                            wrongword.start();

                            dh.sw.reset();
                            submitWordSuccess = false;
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

                                    points_player1.setText("" + dh.scorephase1);
                                    MediaPlayer correctword = MediaPlayer.create(TwoPlayerWordGamePhases
                                            .this, R.raw.beep_correct_scraggle);
                                    correctword.start();

                                    dh.arrayList.add(SubmitWord.checkword);
                                    // next thing you have to do is check if your adapter has changed
                                    adapter.notifyDataSetChanged();

                                    dh.sw.reset();

                                    submitWordSuccess = true;

                                } else {

                                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                    v.vibrate(300);
                                    MediaPlayer wrongword = MediaPlayer.create(TwoPlayerWordGamePhases
                                            .this, R.raw.beep_error_scraggle);
                                    wrongword.start();

                                    dh.sw.reset();

                                    submitWordSuccess = false;

                                }

                            }
                            else{
                                submitWordSuccess = false;
                            }
                        }

                        dh.previousGridPlayed = -1;
                        previousButton = null;
                        deHighlightSelectedButtons();
                        selectedButtonList.clear();

//                        if((submitWordSuccess) && (!isTimerTrue)) {
//                            submit_word_phase1.setEnabled(false);
//                            myTurn = false;
//                            saveAndSendPreferences();
//                        }

                        String tpwg_player2_time_rem = tpwg_phase_player2_time_rem.getText().toString();
                        if((submitWordSuccess) && !(tpwg_player2_time_rem.equals("1"))) {
                            //new code for tpwg
                            submit_word_phase1.setEnabled(false);
                            myTurn = false;
                            saveAndSendPreferences();
                        }



                    }
                    else {

                        dh.sw.reset();

                        submitWordSuccess = false;

                        dh.previousGridPlayed = -1;
                        previousButton = null;
                        deHighlightSelectedButtons();
                        selectedButtonList.clear();


                        Toast.makeText(context, "Can't proceed. No Internet connection available right now.", Toast.LENGTH_LONG).show();
                    }


                }
            }
        });

        if (!restore2) {
            countDownTimer = new MyCountDownTimer(startTime, interval);

            countDownTimer.start();
        }


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccelerometerDetector = new AccelerometerSensorDetection();
        mAccelerometerDetector.setOnShakeListener(new AccelerometerSensorDetection.OnShakeListener() {

            int countForShake = 0;
            @Override
            public void onShake(int count) {

                if (check.CheckConnectivity(context)) {

                    if(myTurn) {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(100);
                        myTurn = false;
                        submit_word_phase1.setEnabled(false);
                        saveAndSendPreferences();
                    }
                }
                else {
                    Toast.makeText(context, "Can't proceed. No Internet connection available right now.", Toast.LENGTH_LONG).show();
                }

            }
        });

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
        //proper code
        getBroadcastReceiver();
      /* Changed the music file */

        mSensorManager.registerListener(mAccelerometerDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);

        mMediaPlayer = MediaPlayer.create(this, R.raw.merge3);
        mMediaPlayer.setVolume(0.5f, 0.5f);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        Log.d("music start", "music =" + dh.music);

        SharedPreferences wordGame1 = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
        boolean restore = wordGame1.getBoolean("save_shared_preference", false);

//        if (restore) {
//            loadSavedPreferences();
//        }

        if (restore && gameOnFromIntent == null) {
            loadSavedPreferences();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        //proper code
        this.unregisterReceiver(mReceiver);

        mSensorManager.unregisterListener(mAccelerometerDetector);
        mMediaPlayer.stop();

        mMediaPlayer.reset();

        mMediaPlayer.release();

        Log.d("music stop", "music =" + dh.music);

        countDownTimer.cancel();

        savePreferences();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
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

            dh.words = new ArrayList<>();

            int mLargeIds[] = {R.id.large1, R.id.large2, R.id.large3,
                    R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8,
                    R.id.large9,};
            int mSmallIds[] = {R.id.small1, R.id.small2, R.id.small3,
                    R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8,
                    R.id.small9,};

            for (int large = 0; large < 9; large++) {
                View outer = findViewById(mLargeIds[large]);

                String word = wr.getWord();
                dh.words.add(large, word);
                String jumbledString[] = jw.JumbledWord(word);

                for (int small = 0; small < 9; small++) {

                    final Button inner = (Button) outer.findViewById
                            (mSmallIds[small]);
                    inner.setText(jumbledString[small]);
                    allMyButtons.add(inner);




//                    SharedPreferences wordGame_save = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
//                    myTurn = wordGame_save.getBoolean("my_Turn", false);


                    inner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(myTurn){
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

                                    Toast.makeText(TwoPlayerWordGamePhases
                                            .this, "Not a valid move", Toast.LENGTH_SHORT).show();
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
                        }//for myTurn
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

                    if (dh.dhselectedButtonList.contains(buttonString))
                        selectedButtonList.add(inner);

                    if (dh.dhcorrectWordButtonList.contains(buttonString))
                        correctWordButtonList.add(inner);


                    if (dh.dhpreviousbutton.contains(buttonString))
                        previousButton = inner;

//                    SharedPreferences wordGame_save = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
//                    myTurn = wordGame_save.getBoolean("my_Turn", false);

                    inner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(myTurn){
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

                                    Toast.makeText(TwoPlayerWordGamePhases.this, "Not a valid move",
                                            Toast.LENGTH_SHORT).show();

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

                        }//for myTurn

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

                String timeRemPlayer1 = timer_player1.getText().toString();
                String timeRemPlayer2 = tpwg_phase_player2_time_rem.getText().toString();
                if ((timeRemPlayer1.equals("1")) && (timeRemPlayer2.equals("1"))) {

                    SharedPreferences prefs = getSharedPreferences(PREFS_TWO_PLAYER_WORD_GAME, Context.MODE_PRIVATE);
                    String opponentRegId = prefs.getString(PROPERTY_OPPONENT_REG_ID, "");
                    String opponentName = prefs.getString(PROPERTY_OPPONENT_NAME, "");
                    String userName = prefs.getString(PROPERTY_STORED_NAME, "");


                /* send message that the other player quit */
                    String quit = "true";
                    sendMessageForQuit(quit, opponentRegId, userName);


                /* handle firebase - leaderboard and online */

                    int score = dh.scorephase1;
                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.US).format(new Date());

                    TwoPlayerWordGameRemoteClient tpwg_RemoteClient2 =
                            new TwoPlayerWordGameRemoteClient(TwoPlayerWordGamePhases.this);
                    tpwg_RemoteClient2.saveScore(userName, userName, score, timeStamp);
                    tpwg_RemoteClient2.updateStatus(userName, userName, true);
                    tpwg_RemoteClient2.updateStatus(opponentName, opponentName, true);


                    SharedPreferences.Editor editor_prefs = prefs.edit();
//                    editor_prefs.remove(PROPERTY_OPPONENT_NAME);
//                    editor_prefs.remove(PROPERTY_OPPONENT_REG_ID);

                    SharedPreferences wordGame = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
                    SharedPreferences.Editor editor_wordGame = wordGame.edit();
//                    editor_wordGame.clear();
//                    editor_wordGame.commit();

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME_TPWG, 0);
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putInt("scoreFromPhase1", dh.scorephase1);
                    editor.putString("userName", userName);
                    editor.putString("opponentName", opponentName);
                    String opponentScore = tpwg_phase_player2_score.getText().toString();
                    editor.putString("opponentScore", opponentScore);
                    editor.commit();

                    Intent intent = new Intent(TwoPlayerWordGamePhases.this, TwoPlayerWordGameActivityGameOver.class);
                    startActivity(intent);

                    dh.scorephase1 = 0;
                    finish();
                    return;
                }

                else{
                    if (timeRemPlayer1.equals("1")){
                        timer = "true";
                        myTurn = false;
                        submit_word_phase1.setEnabled(false);
                        saveAndSendPreferences();
                    }
                }




            } else {

                String timeRemPlayer1 = timer_player1.getText().toString();
                String timeRemPlayer2 = tpwg_phase_player2_time_rem.getText().toString();
                if ((timeRemPlayer1.equals("1")) && (timeRemPlayer2.equals("1"))) {

                if (dh.correctWordGrid.isEmpty() || dh.correctWordGrid.size() == 1) {

                    //callanotheractivity;
//                    SharedPreferences settings = getSharedPreferences(PREFS_NAME_TPWG, 0);
//                    SharedPreferences.Editor editor = settings.edit();
//
//                    editor.putInt("scoreFromPhase1", dh.scorephase1);
//                    editor.commit();
//
//                    Intent intent = new Intent(TwoPlayerWordGamePhases.this,
//                            TwoPlayerWordGameActivityGameOver.class);
//                    startActivity(intent);
//                    dh.scorephase1 = 0;
//                    finish();
//                    return;


                    SharedPreferences prefs = getSharedPreferences(PREFS_TWO_PLAYER_WORD_GAME, Context.MODE_PRIVATE);
                    String opponentRegId = prefs.getString(PROPERTY_OPPONENT_REG_ID, "");
                    String opponentName = prefs.getString(PROPERTY_OPPONENT_NAME, "");
                    String userName = prefs.getString(PROPERTY_STORED_NAME, "");


                /* send message that the other player quit */
                    String quit = "true";
                    sendMessageForQuit(quit, opponentRegId, userName);


                /* handle firebase - leaderboard and online */

                    int score = dh.scorephase1;
                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.US).format(new Date());

                    final TwoPlayerWordGameRemoteClient tpwg_RemoteClient1 =
                            new TwoPlayerWordGameRemoteClient(TwoPlayerWordGamePhases.this);

                    tpwg_RemoteClient1.saveScore(userName, userName, score, timeStamp);
                    tpwg_RemoteClient1.updateStatus(userName, userName, true);
                    tpwg_RemoteClient1.updateStatus(opponentName, opponentName, true);


//                    SharedPreferences.Editor editor_prefs = prefs.edit();
//                    editor_prefs.remove(PROPERTY_OPPONENT_NAME);
//                    editor_prefs.remove(PROPERTY_OPPONENT_REG_ID);

                    SharedPreferences wordGame = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
                    SharedPreferences.Editor editor_wordGame = wordGame.edit();
//                    editor_wordGame.clear();
//                    editor_wordGame.commit();

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME_TPWG, 0);
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putInt("scoreFromPhase1", dh.scorephase1);
                    editor.putString("userName", userName);
                    editor.putString("opponentName", opponentName);
                    String opponentScore = tpwg_phase_player2_score.getText().toString();
                    editor.putString("opponentScore", opponentScore);
                    editor.commit();

                    Intent intent = new Intent(TwoPlayerWordGamePhases.this, TwoPlayerWordGameActivityGameOver.class);
                    startActivity(intent);

                    finish();
                    return;

                } else {

                    timer = "false";
                    isTimerTrue = false;
                    tpwg_phase_player2_time_rem.setText("" + startTime / interval);
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

                    Toast.makeText(TwoPlayerWordGamePhases.this, "Phase2 Started", Toast.LENGTH_LONG).show();
                    countDownTimer = new MyCountDownTimer(startTime, interval);

                    countDownTimer.start();
                }
            }
                else{
                    if (timeRemPlayer1.equals("1")){
                        timer = "true";
                        myTurn = false;
                        submit_word_phase1.setEnabled(false);
                        saveAndSendPreferences();
                    }
                }

            }

        }

        @Override
        public void onTick(long millisUntilFinished) {

            timer_player1.setText("" + millisUntilFinished / interval);

            dh.millisRemaining = millisUntilFinished;
            if (millisUntilFinished < 16000 && dh.timerIndicator == 0) {
                Toast.makeText(TwoPlayerWordGamePhases.this,
                        "" + millisUntilFinished / interval + " seconds remaining", Toast.LENGTH_SHORT).show();
                dh.timerIndicator = 1;
            }
        }

    }

    public void loadSavedPreferences() {

        SharedPreferences wordGame_load = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
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
        dh.millisRemaining = wordGame_load.getLong("save_millisRemaining", startTime);
        if (dh.timerStopped) {

            timer_player1.equals("");
            countDownTimer = null;
            countDownTimer = new MyCountDownTimer(dh.millisRemaining, interval);

            timer_player1.setText("" + dh.millisRemaining / interval);
            countDownTimer.start();
        } else {
            countDownTimer = new MyCountDownTimer(startTime, interval);
            countDownTimer.start();
        }

        points_player1.setText("" + dh.scorephase1);

        try {
            dh = (WordGamePhasesDataHolder) ObjectSerializer.deserialize(wordGame_load.getString("save_buttons",
                    ObjectSerializer.serialize(new WordGamePhasesDataHolder())));

            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, dh.arrayList);

            listView.setAdapter(adapter);

            callfunction(false);

        } catch (Exception e) {
            e.printStackTrace();
            editor_wordGame.clear();
            editor_wordGame.commit();
        }
    }

    public void savePreferences() {

        SharedPreferences wordGame_save = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
        SharedPreferences.Editor editor_wordGame = wordGame_save.edit();


        editor_wordGame.putBoolean("my_Turn", myTurn);
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

        try {
            editor_wordGame.putString("save_buttons", ObjectSerializer.serialize(dh));
        } catch (Exception e) {
            e.printStackTrace();
            editor_wordGame.clear();
            editor_wordGame.commit();
        }
        editor_wordGame.commit();
    }


    private void getBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter("com.google.android.c2dm.intent.RECEIVE");
        intentFilter.setPriority(1);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // handle intent

                Bundle extras = intent.getExtras();

                final String turn = extras.getString("myTurn");
                String gameOnFromIntent = extras.getString("gameOn");
                String gameDataFromIntent = extras.getString("dh");

                timerFromIntent = "false";
                timerFromIntent = extras.getString("timer");
//                if(gameOnFromIntent!=null && gameOnFromIntent.equals("true")){
//                    myTurn = true;
//                    submit_word_phase1.setEnabled(true);
//                }
                loadFromNotification(gameDataFromIntent, gameOnFromIntent);
                abortBroadcast();
            }
        };
        this.registerReceiver(mReceiver, intentFilter);
    }

    private void sendMessage(final String dhObject, final String opponentRegId) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                List<String> regIds = new ArrayList<>();
                Map<String, String> msgParams;
                msgParams = new HashMap<>();
                msgParams.put("data.dh", dhObject);
                msgParams.put("data.gameOn", "true");
                msgParams.put("data.opponentRegId", opponentRegId);
                msgParams.put("data.timer", timer);
                String turn;
                if(myTurn){
                    turn = "true";
                } else{
                    turn = "false";
                }
                msgParams.put("data.turn", turn);
                TwoPlayerWordGameGcmNotification gcmNotification = new TwoPlayerWordGameGcmNotification();
                regIds.clear();
                regIds.add(opponentRegId);
                gcmNotification.sendNotification(msgParams, regIds, TwoPlayerWordGamePhases.this);
                return "Message Sent - ";
            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }


    public void loadFromNotification(String gameDataFromIntent, String gameOnFromIntent){

        if(gameOnFromIntent.equals("false")){

            SharedPreferences prefs = getSharedPreferences(PREFS_TWO_PLAYER_WORD_GAME, Context.MODE_PRIVATE);
            String opponentRegId = prefs.getString(PROPERTY_OPPONENT_REG_ID, "");
            String opponentName = prefs.getString(PROPERTY_OPPONENT_NAME, "");
            String userName = prefs.getString(PROPERTY_STORED_NAME, "");


            SharedPreferences.Editor editor_prefs = prefs.edit();
//            editor_prefs.remove(PROPERTY_OPPONENT_NAME);
//            editor_prefs.remove(PROPERTY_OPPONENT_REG_ID);

            TwoPlayerWordGameRemoteClient tpwg_RemoteClient_Score = new TwoPlayerWordGameRemoteClient(this);

            int score = dh.scorephase1;

            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.US).format(new Date());

            tpwg_RemoteClient_Score.saveScore(userName, userName, score, timeStamp);

            SharedPreferences wordGame = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
            dh.scorephase1 = wordGame.getInt("save_score", 0);

            SharedPreferences settings = getSharedPreferences(PREFS_NAME_TPWG, 0);
            SharedPreferences.Editor editor = settings.edit();

            SharedPreferences.Editor editor_wordGame = wordGame.edit();
//            editor_wordGame.clear();
//            editor_wordGame.commit();

            editor.putInt("scoreFromPhase1", dh.scorephase1);
            editor.putString("userName", userName);
            editor.putString("opponentName", opponentName);
            String opponentScore = tpwg_phase_player2_score.getText().toString();
            editor.putString("opponentScore", opponentScore);
            editor.commit();

            Intent intent = new Intent(TwoPlayerWordGamePhases.this, TwoPlayerWordGameActivityGameOver.class);
            startActivity(intent);

            finish();
            return;

        }
        else {

            SharedPreferences wordGame_load = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
            Gson gson = new Gson();
            WordGamePhasesDataHolder ob = gson.fromJson(gameDataFromIntent, WordGamePhasesDataHolder.class);

//            if((timerFromIntent != null) && (timerFromIntent.equals("true"))){
//                isTimerTrue = true;
//            }
//            else{
//                isTimerTrue = false;
//            }

            myTurn = true;
            submit_word_phase1.setEnabled(true);
            allMyButtons.clear();
            previousButton = null;
            correctWordButtonList.clear();
            selectedButtonList.clear();

            dh.count = ob.count;
            dh.scorephase1 = wordGame_load.getInt("save_score", 0);
            dh.phaseNumber = ob.phaseNumber;

            if (dh.phaseNumber == 1)
                scraggle_phase1_label.setText("Scraggle Phase 1");
            else
                scraggle_phase1_label.setText("Scraggle Phase 2");

            tpwg_phase_player2_score.setText("" + ob.scorephase1);
            tpwg_phase_player2_time_rem.setText("" + ob.millisRemaining / interval);

            dh.correctWordGrid = ob.correctWordGrid;
            dh.wrongWordGrid = ob.wrongWordGrid;
            dh.notAttemptedGrid = ob.notAttemptedGrid;
            dh.latestButtonIndex = ob.latestButtonIndex;
            dh.arrayList = ob.arrayList;
            dh.indexToBlank = ob.indexToBlank;
            dh.allButtonList = ob.allButtonList;
            dh.dhcorrectWordButtonList = ob.dhcorrectWordButtonList;
            dh.dhselectedButtonList = ob.dhselectedButtonList;
            dh.submittedWords = ob.submittedWords;
            dh.dhpreviousbutton = ob.dhpreviousbutton;

            dh.millisRemaining = wordGame_load.getLong("save_millisRemaining", startTime);
            if (dh.phaseNumber == 1) {

//        dh.timerStopped = wordGame_load.getBoolean("save_timer_stopped", false);
//                dh.millisRemaining = wordGame_load.getLong("save_millisRemaining", startTime);
//        if (dh.timerStopped) {
//
                if (dh.millisRemaining == startTime) {

                } else {


                    timer_player1.equals("");
                    countDownTimer = null;
                    countDownTimer = new MyCountDownTimer(dh.millisRemaining, interval);

                    timer_player1.setText("" + dh.millisRemaining / interval);
                    countDownTimer.start();
                }
            }
        else {
                String getTimer = timer_player1.getText().toString();
                if (getTimer.equals("1")){
                    countDownTimer = new MyCountDownTimer(startTime, interval);
                    countDownTimer.start();
                }
                else{
                    timer_player1.equals("");
                    countDownTimer = null;
                    countDownTimer = new MyCountDownTimer(dh.millisRemaining, interval);

                    timer_player1.setText("" + dh.millisRemaining / interval);
                    countDownTimer.start();
                }

        }

            points_player1.setText("" + dh.scorephase1);

//        try {
//            dh = (WordGamePhasesDataHolder) ObjectSerializer.deserialize(wordGame_load.getString("save_buttons",
//                    ObjectSerializer.serialize(new WordGamePhasesDataHolder())));

            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, dh.arrayList);

            listView.setAdapter(adapter);

            callfunction(false);
        }

    }

    public void saveAndSendPreferences(){

        countDownTimer.cancel();
        timer_player1.equals("");
//        countDownTimer = null;
        int bgColor;
        int enabled;
        String text1;
        int id;
        String combinedButton;
        dh.allButtonList = new ArrayList<String>();
        dh.dhselectedButtonList = new ArrayList<String>();
        dh.dhcorrectWordButtonList = new ArrayList<String>();
        dh.dhpreviousbutton = new ArrayList<String>();

//        editor_wordGame.putBoolean("save_music_on_off", dh.music);
//        editor_wordGame.putInt("save_phase_number", dh.phaseNumber);
//        editor_wordGame.putLong("save_millisRemaining", dh.millisRemaining);
//        editor_wordGame.putInt("save_score", dh.scorephase1);

//        dh.timerStopped = true;
//        editor_wordGame.putBoolean("save_shared_preference", true);
//
//        editor_wordGame.putBoolean("save_timer_stopped", dh.timerStopped);

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
        String json = "";
        Gson gson = new Gson();
        json =  gson.toJson(dh);
        SharedPreferences prefs = getSharedPreferences(PREFS_TWO_PLAYER_WORD_GAME, Context.MODE_PRIVATE);
        String opponentRegId = prefs.getString(PROPERTY_OPPONENT_REG_ID, "");
        SharedPreferences wordGame_save = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
        SharedPreferences.Editor editor_wordGame = wordGame_save.edit();
        editor_wordGame.putLong("save_millisRemaining", dh.millisRemaining);
        editor_wordGame.putInt("save_score", dh.scorephase1);
        editor_wordGame.putBoolean("my_Turn", myTurn);
        editor_wordGame.commit();

        /* before sending check for internet connectivity */
        sendMessage(json, opponentRegId);
    }

    private void sendMessageForQuit(final String quit, final String opponentRegId,
                                    final String userName) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                List<String> regIds = new ArrayList<>();
                Map<String, String> msgParams;
                msgParams = new HashMap<>();
//                msgParams.put("data.dh", dhObject);
                msgParams.put("data.gameOn", "false");
                msgParams.put("data.opponentRegId", opponentRegId);
                msgParams.put("data.quit", quit);
                msgParams.put("data.opponentName", userName);
//                String turn;
//                if(myTurn){
//                    turn = "true";
//                } else{
//                    turn = "false";
//                }
//                msgParams.put("data.turn", turn);
                TwoPlayerWordGameGcmNotification gcmNotification = new TwoPlayerWordGameGcmNotification();
                regIds.clear();
                regIds.add(opponentRegId);
                gcmNotification.sendNotification(msgParams, regIds, TwoPlayerWordGamePhases.this);
                return "Message Sent - ";
            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

//    private void sendMessageForTimerExpires(final String timer, final String opponentRegId) {
//
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                List<String> regIds = new ArrayList<>();
//                Map<String, String> msgParams;
//                msgParams = new HashMap<>();
////                msgParams.put("data.dh", dhObject);
////                msgParams.put("data.gameOn", "true");
//                msgParams.put("data.opponentRegId", opponentRegId);
//                msgParams.put("data.timer", "true");
////                msgParams.put("data.opponentName", opponentName);
////                String turn;
////                if(myTurn){
////                    turn = "true";
////                } else{
////                    turn = "false";
////                msgParams.put("data.turn", turn);
//                TwoPlayerWordGameGcmNotification gcmNotification = new TwoPlayerWordGameGcmNotification();
//                regIds.clear();
//                regIds.add(opponentRegId);
//                gcmNotification.sendNotification(msgParams, regIds, TwoPlayerWordGamePhases.this);
//                return "Message Sent - ";
//            }
//
//            @Override
//            protected void onPostExecute(String msg) {
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//            }
//        }.execute();
//    }



}


