package edu.neu.madcourse.dhvanisheth.twoplayerwordgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dhvani on 3/21/2016.
 */
public class TwoPlayerWordGameRemoteClient {

    private static final String MyPREFERENCES = "MyPrefs" ;
    private static final String FIREBASE_DB = "https://vivid-torch-7665.firebaseio.com/user-list-two-player";
    private static final String TAG = "TwoPlayerWordGameRemoteClient";
    private static boolean isDataChanged = false;
    private Context mContext;
    private HashMap<String, String> fireBaseData = new HashMap<String, String>();
    private List<String> playerList = new ArrayList<String>();
    ArrayAdapter<String> adapter;


    public TwoPlayerWordGameRemoteClient(Context mContext)
    {
        this.mContext = mContext;
        Firebase.setAndroidContext(mContext);

    }


    public void saveValue(String key, String value) {
        Firebase ref = new Firebase(FIREBASE_DB);
        Firebase usersRef = ref.child(key);
        usersRef.setValue(value, new Firebase.CompletionListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Log.d(TAG, "Data could not be saved. " + firebaseError.getMessage());
                } else {
                    Log.d(TAG, "Data saved successfully.");
                }
            }
        });
    }

    public void saveValueUser(String key, String name, Boolean available, String regId) {
        Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/");
        Firebase usersRef = ref.child("users").child(key);
        TwoPlayerWordGameUser user = new TwoPlayerWordGameUser(name, available, regId);
//        usersRef.setValue(user);
        usersRef.setValue(user, new Firebase.CompletionListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Log.d(TAG, "Data could not be saved. " + firebaseError.getMessage());
                } else {
                    Log.d(TAG, "Data saved successfully.");
                }
            }
        });
    }

    public boolean isDataFetched()
    {
        return isDataChanged;
    }

    public String getValue(String key)
    {
        return fireBaseData.get(key);
    }

    @SuppressLint("LongLogTag")
    public void fetchValue(String key) {

        Log.d(TAG, "Get Value for Key - " + key);
//        Firebase ref = new Firebase(FIREBASE_DB + key);//preiously this was used
//        Firebase ref = new Firebase(FIREBASE_DB);


        final String keyToBeSearched = key;

        Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/user-list-two-player");

        Query queryRef = ref.orderByKey();
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // snapshot contains the key and value
                isDataChanged = true;
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if(keyToBeSearched.equals(postSnapshot.getKey())){
//                        fireBaseData.put(postSnapshot.getKey(), postSnapshot.getValue(String.class));

                    }
                    else{
                        playerList.add(postSnapshot.getKey());
                    }
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.getMessage());
                Log.e(TAG, firebaseError.getDetails());
            }
        });
    }

    public List<String> getPlayerList() {
        return playerList;
    }

    public void deleteValue (String key){

        final String keyToBeSearched = key;

        final Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/user-list-two-player");

        Query queryRef = ref.orderByKey();
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // snapshot contains the key and value
                isDataChanged = true;
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if(keyToBeSearched.equals(postSnapshot.getKey())){
                        ref.child(keyToBeSearched).removeValue();
                        break;
                    }
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.getMessage());
                Log.e(TAG, firebaseError.getDetails());
            }
        });

    }

    public void removeRegId(final String playerName) {
        final Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/user-list-two-player");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(playerName)) {
                        ref.child(playerName).removeValue();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                if (firebaseError != null) {
                    Log.e("ERROR", firebaseError.getMessage());
                }
            }
        });
    }

    public void removeUser(final String playerName) {
        Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/");
        final Firebase usersRef = ref.child("users").child(playerName);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usersRef.removeValue();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    if (snapshot.getKey().equals(playerName)) {
//                        usersRef.child(playerName).removeValue();
//                        break;
//                    }
//                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                if (firebaseError != null) {
                    Log.e("ERROR", firebaseError.getMessage());
                }
            }
        });
    }

    public void saveScore(final String key, final String name, final int score, final String dateTime) {
        Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/");

        final TwoPlayerWordGameLeaderboard lb = new TwoPlayerWordGameLeaderboard(name, score, dateTime);

        final Firebase usersRef = ref.child("leaderboard").child(key);

        ref.child("leaderboard").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {

                    usersRef.setValue(lb);
                } else {
                    Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/leaderboard");

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                TwoPlayerWordGameLeaderboard lb1 =
                                        snapshot.getValue(TwoPlayerWordGameLeaderboard.class);

                                if (snapshot.getKey().equals(name)) {

                                    if (lb1.getScore() < score) {

//                                    Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/");
//
//                                    final Firebase usersRef1 = ref.child("leaderboard").child(key);
//
//                                    TwoPlayerWordGameLeaderboard lb2 = new TwoPlayerWordGameLeaderboard(name, score, dateTime);

                                        usersRef.setValue(lb);
                                        break;

                                    }

                                } else {
                                    continue;
                                }

                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            if (firebaseError != null) {
                                Log.e("ERROR", firebaseError.getMessage());
                            }
                        }
                    });

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }


    public void updateStatus(final String key, final String name, final boolean available) {
        Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    TwoPlayerWordGameUser user = snapshot.getValue(TwoPlayerWordGameUser.class);

                    if (snapshot.getKey().equals(name)) {

                        String regId = user.getRegId();

                        Firebase ref1 = new Firebase("https://vivid-torch-7665.firebaseio.com/");

                        final TwoPlayerWordGameUser user1 = new TwoPlayerWordGameUser(name, available, regId);

                        final Firebase usersRef1 = ref1.child("users").child(key);

                        usersRef1.setValue(user1);

                        break;
                    }
                    else{
                        continue;
                    }


                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                if (firebaseError != null) {
                    Log.e("ERROR", firebaseError.getMessage());
                }
            }
        });


    }



    }
