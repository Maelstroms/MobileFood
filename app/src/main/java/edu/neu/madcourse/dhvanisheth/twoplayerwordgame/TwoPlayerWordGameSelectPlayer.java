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
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.dhvanisheth.R;


public class TwoPlayerWordGameSelectPlayer extends Activity {

    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;
    ListView playerList;
    Context context;
    TwoPlayerWordGameRemoteClient tpwgRemoteClient;
    public static final String PREFS_TWO_PLAYER_WORD_GAME = "MyPrefsFile_TwoPlayerWordGame";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_STORED_YES_NO = "stored_name_yes_no";
    private static final String PROPERTY_STORED_NAME = "stored_name";
    private static final String PROPERTY_OPPONENT_NAME = "opponent_name";
    private static final String PROPERTY_OPPONENT_REG_ID = "opponent_reg_id";

    Timer timer;
    TimerTask timerTask;
    static final String TAG = "Player Selection";
    public List<String> listOfPlayers;
    public HashMap<String, String> opponentNameAndId = new HashMap<String, String>();
    public boolean gotList = false;
    TextView displaySynch;
    IntentFilter intentFilter;
    TextView displayOpponentName;

    final Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/user-list-two-player");

    LinearLayout rlayout1;
    LinearLayout rlayout2;

    CheckInternetConnectivity check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twoplayerwordgame_player_selection);

        rlayout1 = (LinearLayout) findViewById(R.id.tpwg_choose_player);
        rlayout1.setVisibility(rlayout1.VISIBLE);

        rlayout2 = (LinearLayout) findViewById(R.id.tpwg_waiting_for_approval);
        rlayout2.setVisibility(rlayout2.GONE);

        playerList = (ListView) findViewById(R.id.tpwg_player_list);

        displayOpponentName = (TextView) findViewById(R.id.tpwg_displayOpponentName);

        arrayList = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);

        playerList.setAdapter(adapter);

        context = getApplicationContext();

        intentFilter = new IntentFilter();

        check = new CheckInternetConnectivity();

        tpwgRemoteClient = new TwoPlayerWordGameRemoteClient(this);

        final SharedPreferences prefs = getSharedPreferences(PREFS_TWO_PLAYER_WORD_GAME, Context.MODE_PRIVATE);
        boolean already_registered = prefs.getBoolean(PROPERTY_STORED_YES_NO, false);
        final String username = prefs.getString(PROPERTY_STORED_NAME, "");
        final String usernameRegId = prefs.getString(PROPERTY_REG_ID, "");
//        displaySynch = (TextView) findViewById(R.id.tpwg_display3);

        if (check.CheckConnectivity(context)) {

//            getPlayerList(username);
            getPlayerListFromUser(username);

        } else {
            Toast.makeText(context, "Can't proceed. No Internet connection available right now.", Toast.LENGTH_LONG).show();
        }


//        intentFilter.setPriority(999);

        playerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String opponentName = (String) playerList.getItemAtPosition(position);
                String opponentRegId = opponentNameAndId.get(opponentName);

                //                LinearLayout rlayout1 = (LinearLayout) findViewById(R.id.tpwg_choose_player);
                rlayout1.setVisibility(rlayout1.GONE);

//                LinearLayout rlayout2 = (LinearLayout) findViewById(R.id.tpwg_waiting_for_approval);
                rlayout2.setVisibility(rlayout2.VISIBLE);

                displayOpponentName.setText(opponentName);

                if (check.CheckConnectivity(context)) {
                    sendMessage(opponentName, opponentRegId, username, usernameRegId);
                    tpwgRemoteClient.updateStatus(opponentName, opponentName, false);
                    tpwgRemoteClient.updateStatus(username, username, false);
                }
                else{
                    Toast.makeText(context, "Can't proceed. No Internet connection available right now.",Toast.LENGTH_LONG).show();
                }

                SharedPreferences.Editor editor_tpwg = prefs.edit();
                editor_tpwg.putBoolean("sentRequest", true);
                editor_tpwg.putString(PROPERTY_OPPONENT_NAME, opponentName);
                editor_tpwg.putString(PROPERTY_OPPONENT_REG_ID, opponentRegId);
                editor_tpwg.commit();

            }
        });

    }


    private void getPlayerList(final String playerName) {

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> players = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(playerName)) {
                        continue;
                    }
                    players.add(snapshot.getKey());
                    opponentNameAndId.put(snapshot.getKey(), snapshot.getValue(String.class));
                }
                for (int i = 0; i < players.size(); i++) {
                    arrayList.add(players.get(i));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                if (firebaseError != null) {
                    Log.e("ERROR", firebaseError.getMessage());
                }
            }
        });
    }

    private void getPlayerListFromUser(final String playerName) {
        Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/users");
//        final Firebase usersRef = ref.child("users").child(playerName);
//        final Firebase usersRef = ref.child("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> players = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    TwoPlayerWordGameUser user = snapshot.getValue(TwoPlayerWordGameUser.class);

                    if (snapshot.getKey().equals(playerName)) {
                        continue;
                    }
                    if (user.getAvailable()) {
                        players.add(user.getFullName());
                    }
                    opponentNameAndId.put(user.getFullName(), user.getRegId());
                }
                for (int i = 0; i < players.size(); i++) {
                    arrayList.add(players.get(i));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                if (firebaseError != null) {
                    Log.e("ERROR", firebaseError.getMessage());
                }
            }
        });
    }

    private void sendMessage(final String opponentName, final String opponentId,
                             final String username, final String userRegId) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                List<String> regIds = new ArrayList<String>();
                Map<String, String> msgParams;
                msgParams = new HashMap<>();
                msgParams.put("data.request", "request to play");
                msgParams.put("data.opponentName", opponentName);
                msgParams.put("data.opponentId", opponentId);
                msgParams.put("data.username", username);
                msgParams.put("data.userRegId", userRegId);
                TwoPlayerWordGameGcmNotification gcmNotification = new TwoPlayerWordGameGcmNotification();
                regIds.clear();
                regIds.add(opponentId);
                gcmNotification.sendNotification(msgParams, regIds, TwoPlayerWordGameSelectPlayer.this);
                return "Message Sent - " + opponentName;

            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute(null, null, null);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


}