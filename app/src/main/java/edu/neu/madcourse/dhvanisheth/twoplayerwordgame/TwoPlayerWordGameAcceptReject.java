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
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.dhvanisheth.R;

public class TwoPlayerWordGameAcceptReject extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile_GameOver";
    public static final String PREFS_NAME_WORD_GAME = "MyPrefsFile_WordGame";

    Button yes;
    Button no;

    Context context;

    TwoPlayerWordGameRemoteClient tpwgRemoteClient;

    public static final String PREFS_TWO_PLAYER_WORD_GAME = "MyPrefsFile_TwoPlayerWordGame";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_STORED_YES_NO = "stored_name_yes_no";
    private static final String PROPERTY_STORED_NAME = "stored_name";
    private static final String PROPERTY_OPPONENT_NAME = "opponent_name";
    private static final String PROPERTY_OPPONENT_REG_ID = "opponent_reg_id";

    CheckInternetConnectivity check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        check = new CheckInternetConnectivity();

        context = getApplicationContext();

        setContentView(R.layout.twoplayerwordgame_approve_reject_request);

        tpwgRemoteClient = new TwoPlayerWordGameRemoteClient(this);

        TextView displayName = (TextView) findViewById(R.id.tpwg_display);

        yes = (Button) findViewById(R.id.tpwg_yes);
        no = (Button) findViewById(R.id.tpwg_no);

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();

//        String opponentName = intent.getStringExtra("opponentName");

        final String opponentName = extras.getString("opponentName");
        final String opponentRegId = extras.getString("opponentRegId");

//        String opponentName = intent.getExtras().getString("opponentName");
        final SharedPreferences prefs = getSharedPreferences(PREFS_TWO_PLAYER_WORD_GAME, Context.MODE_PRIVATE);
//                int appVersion = getAppVersion(context);
//                Log.i(TAG, "Saving regId on app version " + appVersion);
        final SharedPreferences.Editor editor = prefs.edit();
        final String username = prefs.getString(PROPERTY_STORED_NAME, "");

        displayName.setText(opponentName);

        /* store data in shared pref - player name, reg id, opponent name and reg id, whose turn it is */
        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(check.CheckConnectivity(context)) {
                    sendMessage("", opponentRegId);
                }
                else{
                    Toast.makeText(context, "Can't proceed. No Internet connection available right now.",Toast.LENGTH_LONG).show();
                }

                updateSharedPrefs(editor, opponentName, opponentRegId);


            }

        });

        /* quit the screen */
        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(check.CheckConnectivity(context)) {
                    tpwgRemoteClient.updateStatus(opponentName, opponentName, true);
                    tpwgRemoteClient.updateStatus(username, username, true);
                    sendMessageForReject("", opponentRegId);
                }
                else{
                    Toast.makeText(context, "Can't proceed. No Internet connection available right now.",Toast.LENGTH_LONG).show();
                }
                finish();
                return;
            }

        });

    }

    private void sendMessage(final String message, final String opponentId) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                List<String> regIds = new ArrayList<String>();
                Map<String, String> msgParams;
                msgParams = new HashMap<>();
                msgParams.put("data.requestAccepted", "request to play accepted");
//                msgParams.put("data.opponentName", message);
//                msgParams.put("data.opponentId", opponentId);
                TwoPlayerWordGameGcmNotification gcmNotification = new TwoPlayerWordGameGcmNotification();
                regIds.clear();
                regIds.add(opponentId);
                gcmNotification.sendNotification(msgParams, regIds,TwoPlayerWordGameAcceptReject.this);
                return "Message Sent - " + message;

            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }.execute(null, null, null);
    }

    private void sendMessageForReject(final String message, final String opponentId) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                List<String> regIds = new ArrayList<String>();
                Map<String, String> msgParams;
                msgParams = new HashMap<>();
                msgParams.put("data.requestRejected", "request to play rejected");
//                msgParams.put("data.opponentName", message);
//                msgParams.put("data.opponentId", opponentId);
                TwoPlayerWordGameGcmNotification gcmNotification = new TwoPlayerWordGameGcmNotification();
                regIds.clear();
                regIds.add(opponentId);
                gcmNotification.sendNotification(msgParams, regIds,TwoPlayerWordGameAcceptReject.this);
                return "Message Sent - " + message;

            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }.execute(null, null, null);
    }

    public void updateSharedPrefs(SharedPreferences.Editor editor, String opponentName, String opponentId){

        editor.putString(PROPERTY_OPPONENT_NAME, opponentName);
        editor.putString(PROPERTY_OPPONENT_REG_ID, opponentId);
        editor.commit();
    }
}