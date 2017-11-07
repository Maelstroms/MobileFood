package edu.neu.madcourse.dhvanisheth.twoplayerwordgame;

import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dhvani on 3/26/2016.
 */
public class SendMessageNotification {


    private void sendMessage(final String message, final String opponentId) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                List<String> regIds = new ArrayList<String>();
                Map<String, String> msgParams;
                msgParams = new HashMap<>();
                msgParams.put("data.request", "request to play");
                msgParams.put("data.opponentName", message);
                msgParams.put("data.opponentId", opponentId);
                TwoPlayerWordGameGcmNotification gcmNotification = new TwoPlayerWordGameGcmNotification();
                regIds.clear();
                regIds.add(opponentId);
//                gcmNotification.sendNotification(msgParams, regIds,TwoPlayerWordGameSelectPlayer.this);
                return "Message Sent - " + message;

            }

            @Override
            protected void onPostExecute(String msg) {
//                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute(null, null, null);
    }
}
