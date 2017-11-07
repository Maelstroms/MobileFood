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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.dhvanisheth.R;
import edu.neu.madcourse.dhvanisheth.communication.CommunicationConstants;


public class TwoPlayerWordGameMainActivity extends Activity {
    MediaPlayer mMediaPlayer;

    private AlertDialog mDialog_acknowledgements;
    private AlertDialog mDialog_instructions;
    public static final String PREFS_TWO_PLAYER_WORD_GAME = "MyPrefsFile_TwoPlayerWordGame";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_STORED_YES_NO = "stored_name_yes_no";
    private static final String PROPERTY_STORED_NAME = "stored_name";
    public static final String PREFS_NAME_WORD_GAME_TPWG = "MyPrefsFile_WordGame_TPWG";

    CheckInternetConnectivity check;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final String TAG = "Two Player Word Game";
    TextView mDisplay;
    EditText mMessage;
    GoogleCloudMessaging gcm;
    Context context;
    String regid;
    TwoPlayerWordGameRemoteClient tpwgRemoteClient;
    EditText user_name;
    Button register_button;
    Button acknowledgements_register_button;
    Button acknowledgements_main_button;
    Button unregister_button;
    Button quit_register_button;
    Button quit_main_button;
    Button new_game_main_button;
    Button resume_game_main_button;
    Button leaderboard_main_button;
    Button instructions_main_button;

    final Handler handler = new Handler();
    Timer timer;
    TimerTask timerTask;

    String reg_device;
    String username;

   // ...

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

       setContentView(R.layout.twoplayerwordgame_activity_main);

       user_name = (EditText) findViewById(R.id.tpwg_user_name);
       register_button = (Button) findViewById(R.id.tpwg_register_button);
       unregister_button = (Button) findViewById(R.id.tpwg_main_unregister_button);
       acknowledgements_register_button = (Button) findViewById(R.id.tpwg_acknowledgements_button);
       acknowledgements_main_button = (Button) findViewById(R.id.tpwg_main_acknowledgements_button);

       new_game_main_button = (Button) findViewById(R.id.tpwg_main_new_game_button);

       resume_game_main_button = (Button) findViewById(R.id.tpwg_main_resume_game_button);

       leaderboard_main_button = (Button) findViewById(R.id.tpwg_main_leaderboard_button);

       instructions_main_button = (Button) findViewById(R.id.tpwg_main_instructions_button);

       quit_main_button = (Button) findViewById(R.id.tpwg_main_quit_button);

       quit_register_button = (Button) findViewById(R.id.tpwg_quit_button);


       gcm = GoogleCloudMessaging.getInstance(this);
       context = getApplicationContext();
       check = new CheckInternetConnectivity();

//        removeRegistrationId(getApplicationContext());
       SharedPreferences prefs = getTwoPlayerWordGamePreferences(context);
//        int appVersion = getAppVersion(context);
       boolean already_registered = prefs.getBoolean(PROPERTY_STORED_YES_NO, false);
       username = prefs.getString(PROPERTY_STORED_NAME, "");

       if (already_registered){
           LinearLayout rlayout1 = (LinearLayout) findViewById(R.id.tpwg_register_activity);
           rlayout1.setVisibility(rlayout1.GONE);
       }
       else{
           LinearLayout rlayout2 = (LinearLayout) findViewById(R.id.tpwg_main_activity);
           rlayout2.setVisibility(rlayout2.GONE);
           SharedPreferences wordGame1 = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
           boolean restore = wordGame1.getBoolean("save_shared_preference", false);

           if (restore) {
               resume_game_main_button.setEnabled(true);
               new_game_main_button.setEnabled(false);
           }
           else{
               resume_game_main_button.setEnabled(false);
               new_game_main_button.setEnabled(true);
           }
       }

       tpwgRemoteClient = new TwoPlayerWordGameRemoteClient(this);



       acknowledgements_register_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               LayoutInflater inflater = LayoutInflater.from(TwoPlayerWordGameMainActivity.this);
               View view1 = inflater.inflate(R.layout.twoplayerwordgame_acknowledgements, null);
               TextView tpwg_register_acknowledgements = (TextView) view.findViewById(R.id.tpwg_acknowledgements);
               AlertDialog.Builder builder = new AlertDialog.Builder(TwoPlayerWordGameMainActivity.this);
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

       acknowledgements_main_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               LayoutInflater inflater = LayoutInflater.from(TwoPlayerWordGameMainActivity.this);
               View view1 = inflater.inflate(R.layout.twoplayerwordgame_acknowledgements, null);
               TextView tpwg_main_acknowledgements = (TextView) view.findViewById(R.id.tpwg_acknowledgements);
               AlertDialog.Builder builder = new AlertDialog.Builder(TwoPlayerWordGameMainActivity.this);
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

       instructions_main_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               LayoutInflater inflater = LayoutInflater.from(TwoPlayerWordGameMainActivity.this);
               View view1 = inflater.inflate(R.layout.twoplayerwordgame_instructions, null);
               TextView tpwg_instructions = (TextView) view.findViewById(R.id.tpwg_instructions);
               AlertDialog.Builder builder = new AlertDialog.Builder(TwoPlayerWordGameMainActivity.this);
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

       quit_main_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               SharedPreferences wordGame = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
               SharedPreferences.Editor editor_wordGame = wordGame.edit();
               editor_wordGame.clear();
               editor_wordGame.commit();
               finish();
               return;
           }
       });

       quit_register_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
               return;
           }
       });

       leaderboard_main_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (check.CheckConnectivity(context)) {
                   Intent intent = new Intent(TwoPlayerWordGameMainActivity.this, TwoPlayerWordGameLeaderboardScreen.class);
                   startActivity(intent);
               }
               else {
                   Toast.makeText(context, "Can't proceed. No Internet connection available right now.", Toast.LENGTH_LONG).show();
               }

           }
       });

       resume_game_main_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (check.CheckConnectivity(context)) {
                   Intent intent = new Intent(TwoPlayerWordGameMainActivity.this, TwoPlayerWordGamePhases.class);
                   intent.putExtra("Resume Game", true);
                   startActivity(intent);
               }
               else {
                   Toast.makeText(context, "Can't proceed. No Internet connection available right now.", Toast.LENGTH_LONG).show();
               }
           }
       });


       new_game_main_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

//               SharedPreferences wordGame = getSharedPreferences(PREFS_TWO_PLAYER_WORD_GAME, 0);
//               SharedPreferences.Editor editor_wordGame = wordGame.edit();
//               editor_wordGame.clear();
//               editor_wordGame.commit();


               if (check.CheckConnectivity(context)) {
                   Intent intent = new Intent(TwoPlayerWordGameMainActivity.this, TwoPlayerWordGameSelectPlayer.class);
               startActivity(intent);
           }
           else {
               Toast.makeText(context, "Can't proceed. No Internet connection available right now.", Toast.LENGTH_LONG).show();
           }
           }
       });

       register_button.setOnClickListener(new View.OnClickListener() {


           @Override
           public void onClick(View view) {

               if(check.CheckConnectivity(context)) {
                   if (checkPlayServices()) {
                       regid = getRegistrationId(context);
                       if (TextUtils.isEmpty(regid)) {
                           registerInBackground();
                       }
                   }
               }
               else{
                   Toast.makeText(context, "Can't proceed. No Internet connection available right now.", Toast.LENGTH_LONG).show();
               }
           }
       });

       unregister_button.setOnClickListener(new View.OnClickListener() {


           @Override
           public void onClick(View view) {

               if(check.CheckConnectivity(context)) {

                   unregister();

               }
               else{
                   Toast.makeText(context, "Can't proceed. No Internet connection available right now.",Toast.LENGTH_LONG).show();
               }
           }
       });

   }

    private SharedPreferences getTwoPlayerWordGamePreferences(Context context) {
//        return getSharedPreferences(TwoPlayerWordGameMainActivity.class.getSimpleName(),
//                Context.MODE_PRIVATE);

        return getSharedPreferences(PREFS_TWO_PLAYER_WORD_GAME, Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getTwoPlayerWordGamePreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        Log.i(TAG, String.valueOf(registeredVersion));
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(CommunicationConstants.GCM_SENDER_ID);

                    // implementation to store and keep track of registered devices here
                    msg = "Device registered, registration ID=" + regid;
                    //sendRegistrationIdToBackend(regid);
                    //storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
//				Log.d(TAG, msg);
//				mDisplay.append(msg + "\n");
                sendRegistrationIdToBackend(regid);
                storeRegistrationId(context, regid);
                Toast.makeText(TwoPlayerWordGameMainActivity.this, "Click on Two Player Word Game button again", Toast.LENGTH_SHORT).show();
                finish();
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend(String regid) {
        // Your implementation here.
        String userName = user_name.getText().toString();
        tpwgRemoteClient.saveValue(userName, regid);
        tpwgRemoteClient.saveValueUser(userName, userName, true, regid);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getTwoPlayerWordGamePreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.putBoolean(PROPERTY_STORED_YES_NO, true);
        editor.putString(PROPERTY_STORED_NAME, user_name.getText().toString());
        editor.commit();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void unregister() {
        Log.d(CommunicationConstants.TAG, "UNREGISTER USERID: " + regid);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    msg = "Sent unregistration";
                    gcm.unregister();
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                removeRegistrationIdFromBackend(username);
//                removeRegId(username);
//                removeUser(username);
                removeRegistrationId(getApplicationContext());
//				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//				((TextView) findViewById(R.id.communication_display))
//						.setText(regid);
                finish();
            }
        }.execute();
    }

    private void removeRegistrationIdFromBackend(String username) {
        // Your implementation here.
//        tpwgRemoteClient.deleteValue(username);
        tpwgRemoteClient.removeRegId(username);
        tpwgRemoteClient.removeUser(username);
    }

//    private void removeRegId(final String playerName) {
//        final Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/user-list-two-player");
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    if (snapshot.getKey().equals(playerName)) {
//                        ref.child(playerName).removeValue();
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                if (firebaseError != null) {
//                    Log.e("ERROR", firebaseError.getMessage());
//                }
//            }
//        });
//    }
//
//    private void removeUser(final String playerName) {
//        Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/");
//        final Firebase usersRef = ref.child("users").child(playerName);
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                usersRef.removeValue();
////                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
////                    if (snapshot.getKey().equals(playerName)) {
////                        usersRef.child(playerName).removeValue();
////                        break;
////                    }
////                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                if (firebaseError != null) {
//                    Log.e("ERROR", firebaseError.getMessage());
//                }
//            }
//        });
//    }

    private void removeRegistrationId(Context context) {
        final SharedPreferences prefs = getTwoPlayerWordGamePreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(CommunicationConstants.TAG, "Removig regId on app version "
                + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PROPERTY_REG_ID);
        editor.remove(PROPERTY_APP_VERSION);
        editor.remove(PROPERTY_STORED_NAME);
        editor.remove(PROPERTY_STORED_YES_NO);
        editor.commit();
        regid = null;
    }

    public void startTimer(String key, String message) {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask(key, message);
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        // The values can be adjusted depending on the performance
//        timer.schedule(timerTask, 5000, 1000);
        timer.schedule(timerTask, 5000, 200);
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask(final String key, final String message) {
        timerTask = new TimerTask() {
            public void run() {
                Log.d(TAG, "isDataFetched >>>>" + tpwgRemoteClient.isDataFetched());
                if(tpwgRemoteClient.isDataFetched())
                {
//                    handler.post(new Runnable() {
//
//                        public void run() {
//                            Log.d(TAG, "Value >>>>" + remoteClient.getValue(key));
//                            Toast.makeText(CommunicationStartGame.this, "Value   " + remoteClient.getValue(key), Toast.LENGTH_SHORT).show();
//                        }
//                    });

                    reg_device = tpwgRemoteClient.getValue(key);

//                    sendMessage(message, other_player_name.getText().toString());

                    stoptimertask();
                }

            }
        };
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
        SharedPreferences wordGame1 = getSharedPreferences(PREFS_NAME_WORD_GAME_TPWG, 0);
        boolean restore = wordGame1.getBoolean("save_shared_preference", false);

        if (restore) {
            resume_game_main_button.setEnabled(true);
            new_game_main_button.setEnabled(false);
        }
        else{
            resume_game_main_button.setEnabled(false);
            new_game_main_button.setEnabled(true);
        }

    }

}
