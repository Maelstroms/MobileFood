package edu.neu.madcourse.dhvanisheth.communication;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;


/**
 * Created by derylrodrigues on 3/4/16.
 */
public class RemoteClient {

    private static final String MyPREFERENCES = "MyPrefs" ;
    private static final String FIREBASE_DB = "https://vivid-torch-7665.firebaseio.com/user-list";
    private static final String TAG = "RemoteClient";
    private static boolean isDataChanged = false;
    private Context mContext;
    private HashMap<String, String> fireBaseData = new HashMap<String, String>();


    public RemoteClient(Context mContext)
    {
        this.mContext = mContext;
        Firebase.setAndroidContext(mContext);

    }


    public void saveValue(String key, String value) {
        Firebase ref = new Firebase(FIREBASE_DB);
        Firebase usersRef = ref.child(key);
        usersRef.setValue(value, new Firebase.CompletionListener() {
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

    public void fetchValue(String key) {

        Log.d(TAG, "Get Value for Key - " + key);
//        Firebase ref = new Firebase(FIREBASE_DB + key);//preiously this was used
//        Firebase ref = new Firebase(FIREBASE_DB);


        final String keyToBeSearched = key;

        Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/user-list");

        Query queryRef = ref.orderByKey();
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // snapshot contains the key and value
                isDataChanged = true;
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if(keyToBeSearched.equals(postSnapshot.getKey())){
                        fireBaseData.put(postSnapshot.getKey(), postSnapshot.getValue(String.class));

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.getMessage());
                Log.e(TAG, firebaseError.getDetails());
            }
        });
    }


    public void deleteValue (String key){

        final String keyToBeSearched = key;

        final Firebase ref = new Firebase("https://vivid-torch-7665.firebaseio.com/user-list");

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

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.getMessage());
                Log.e(TAG, firebaseError.getDetails());
            }
        });


    }

}


