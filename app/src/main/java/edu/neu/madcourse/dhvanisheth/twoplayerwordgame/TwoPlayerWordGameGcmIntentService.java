package edu.neu.madcourse.dhvanisheth.twoplayerwordgame;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;

import edu.neu.madcourse.dhvanisheth.R;
import edu.neu.madcourse.dhvanisheth.communication.GcmBroadcastReceiver;
import edu.neu.madcourse.dhvanisheth.scraggle.WordGamePhasesDataHolder;

/**
 * Created by Dhvani on 3/21/2016.
 */
public class TwoPlayerWordGameGcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private static final String TAG = "TwoPlayerWordGameGcmIntentService";
    public static final String PREFS_TWO_PLAYER_WORD_GAME = "MyPrefsFile_TwoPlayerWordGame";

    public TwoPlayerWordGameGcmIntentService() {
        super("TwoPlayerWordGameGcmIntentService");
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Log.d(TAG, extras.toString());
        if (extras != null) {
            String gameData = extras.getString("dh");
//            Gson gson = new Gson();
//            WordGamePhasesDataHolder ob = gson.fromJson(gameData, WordGamePhasesDataHolder.class);
            this.getSharedPreferences(PREFS_TWO_PLAYER_WORD_GAME, Context.MODE_PRIVATE);

            String message = extras.getString("request");
            String opponentName = extras.getString("opponentName");
            String username = extras.getString("username");
            String gameOn = extras.getString("gameOn");
            String requestAccepted = extras.getString("requestAccepted");
            String userRegId = extras.getString("userRegId");
            String gameData1 = extras.getString("dh");
            String requestRejected = extras.getString("requestRejected");
            String timer = extras.getString("timer");
            String quit = extras.getString("quit");

            if (gameOn != null && gameOn.equals("true")) {
                sendNotificationForGameData(gameData, gameOn);
            } else {

                if (gameOn != null && gameOn.equals("false")) {
                    sendNotificationForQuit(opponentName, gameOn);
                } else {

                    if (message != null && message.equals("request to play")) {
                        sendNotificationForPlayRequest(username, userRegId);
                    } else {


                        if (requestAccepted != null && requestAccepted.equals("request to play accepted")) {
                            sendNotificationForPlayRequestAccepted(opponentName);
                        } else {

                            if (requestRejected != null && requestRejected.equals("request to play rejected")) {
                                sendNotificationForPlayRequestRejected(opponentName);
                            } else {
                                if (timer != null && timer.equals("true")) {
                                    sendNotificationForTimer(gameData, timer);
                                } else {

                                    if (message != null) {
                                        sendNotification(message);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        TwoPlayerWordGameGcmBroadcastReceiver.completeWakefulIntent(intent);
//        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    public void sendNotification(String message) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;
        notificationIntent = new Intent(this,TwoPlayerWordGameSelectPlayer.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra("show_response", "show_response");
        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, TwoPlayerWordGameSelectPlayer.class),PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
//                .setSmallIcon(R.mipmap.ic_launcher)
                .setSmallIcon(R.mipmap.ds_appicon)
                .setContentTitle("Two Player Word Game")
//                .setContentTitle("Received GCM Message")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message).setTicker(message)
                .setAutoCancel(true);
        mBuilder.setContentIntent(intent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public void sendNotificationForPlayRequest(String message, String userRegId) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;
        notificationIntent = new Intent(this,TwoPlayerWordGameAcceptReject.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra("show_response", "show_response");
        notificationIntent.putExtra("opponentName", message);
        notificationIntent.putExtra("opponentRegId", userRegId);
//        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, TwoPlayerWordGameAcceptReject.class),PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
//                .setSmallIcon(R.mipmap.ic_launcher)
                .setSmallIcon(R.mipmap.ds_appicon)
                .setContentTitle("Two Player Word Game Request")
//                .setContentTitle("Received GCM Message")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message).setTicker(message)
                .setAutoCancel(true);
        mBuilder.setContentIntent(intent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


    public void sendNotificationForPlayRequestAccepted(String message) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;
        notificationIntent = new Intent(this, TwoPlayerWordGamePhases.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra("show_response", "show_response");
        notificationIntent.putExtra("opponentName", message);
        notificationIntent.putExtra("myTurn", "yes");
//        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, TwoPlayerWordGameAcceptReject.class),PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        message = "Request Accepted";
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
//                .setSmallIcon(R.mipmap.ic_launcher)
                .setSmallIcon(R.mipmap.ds_appicon)
                .setContentTitle("Two Player Word Game Request Accepted")
//                .setContentTitle("Received GCM Message")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message).setTicker(message)
                .setAutoCancel(true);
        mBuilder.setContentIntent(intent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public void sendNotificationForPlayRequestRejected(String message) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;
        notificationIntent = new Intent(this, TwoPlayerWordGameSelectPlayer.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        notificationIntent.putExtra("show_response", "show_response");
//        notificationIntent.putExtra("opponentName", message);
//        notificationIntent.putExtra("myTurn", "yes");
//        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, TwoPlayerWordGameAcceptReject.class),PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        message = "Request Rejected";
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
//                .setSmallIcon(R.mipmap.ic_launcher)
                .setSmallIcon(R.mipmap.ds_appicon)
                .setContentTitle("Two Player Word Game Request Rejected. Select another player.")
//                .setContentTitle("Received GCM Message")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message).setTicker(message)
                .setAutoCancel(true);
        mBuilder.setContentIntent(intent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


    public void sendNotificationForGameData(String gameData, String gameOn){
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;
        notificationIntent = new Intent(this, TwoPlayerWordGamePhases.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra("show_response", "show_response");
        notificationIntent.putExtra("gameData", gameData);
        notificationIntent.putExtra("gameOn", gameOn);
//        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, TwoPlayerWordGameAcceptReject.class),PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String message = "Your Turn";

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
//                .setSmallIcon(R.mipmap.ic_launcher)
                .setSmallIcon(R.mipmap.ds_appicon)
                .setContentTitle("Two Player Word Game")
//                .setContentTitle("Received GCM Message")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message).setTicker(message)
                .setAutoCancel(true);
        mBuilder.setContentIntent(intent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


    public void sendNotificationForQuit(String opponentName, String gameOn){
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;
        notificationIntent = new Intent(this, TwoPlayerWordGamePhases.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra("show_response", "show_response");
//        notificationIntent.putExtra("gameData", gameData);
        notificationIntent.putExtra("gameOn", gameOn);
//        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, TwoPlayerWordGameAcceptReject.class),PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String message = opponentName + " quit. You won !!!";

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
//                .setSmallIcon(R.mipmap.ic_launcher)
                .setSmallIcon(R.mipmap.ds_appicon)
                .setContentTitle("Two Player Word Game")
//                .setContentTitle("Received GCM Message")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message).setTicker(message)
                .setAutoCancel(true);
        mBuilder.setContentIntent(intent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public void sendNotificationForTimer(String gameData, String timer){
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;
        notificationIntent = new Intent(this, TwoPlayerWordGamePhases.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra("show_response", "show_response");
        notificationIntent.putExtra("gameData", gameData);
        notificationIntent.putExtra("timer", timer);
//        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, TwoPlayerWordGameAcceptReject.class),PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String message = "Your Turn";

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
//                .setSmallIcon(R.mipmap.ic_launcher)
                .setSmallIcon(R.mipmap.ds_appicon)
                .setContentTitle("Two Player Word Game")
//                .setContentTitle("Received GCM Message")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message).setTicker(message)
                .setAutoCancel(true);
        mBuilder.setContentIntent(intent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }



}
