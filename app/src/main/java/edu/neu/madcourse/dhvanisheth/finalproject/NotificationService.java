/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.dhvanisheth.finalproject;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.neu.madcourse.dhvanisheth.R;

public class NotificationService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    public static final String PREFS_FINAL_PROJ_END_DATE = "MyPrefsFile_FinalProject_EndDate";

    public NotificationService(){
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        long when = System.currentTimeMillis();         // notification time
//        Notification notification = new Notification(R.drawable.always_stocked_app_icon, "reminder", when);
//        notification.defaults |= Notification.DEFAULT_SOUND;
//        notification.flags |= notification.FLAG_AUTO_CANCEL;
//        Intent notificationIntent = new Intent(this, FinalProjectEditScreen.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent , 0);
////        notification.setLatestEventInfo(getApplicationContext(),
////                "It's about time", "You should open the app now", contentIntent);
//        notification.contentIntent = contentIntent;
//        notification.
//                (getApplicationContext(),
//                "It's about time", "You should open the app now", contentIntent);
//        nm.notify(NOTIF_ID, notification);

//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//            String times = extras.getString("times");
//        }
//        SharedPreferences endDate = getSharedPreferences(PREFS_FINAL_PROJ_END_DATE, 0);
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;
        notificationIntent = new Intent(this,FinalProjectEditScreen.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        notificationIntent.putExtra("show_response", "show_response");
//        notificationIntent.putExtra("opponentName", message);
//        notificationIntent.putExtra("myTurn", "yes");
//        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, TwoPlayerWordGameAcceptReject.class),PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent intent1 = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String message = "Food is getting spoilt";
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
//                .setSmallIcon(R.mipmap.ic_launcher)
                .setSmallIcon(R.mipmap.waste_not_app_icon)
                .setContentTitle("Food Spoilage Alert")
//                .setContentTitle("Received GCM Message")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message).setTicker(message)
                .setAutoCancel(true);
        mBuilder.setContentIntent(intent1);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
