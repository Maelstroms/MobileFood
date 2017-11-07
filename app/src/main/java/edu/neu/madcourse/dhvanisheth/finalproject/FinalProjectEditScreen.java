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
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import edu.neu.madcourse.dhvanisheth.R;

public class FinalProjectEditScreen extends Activity {

    ListView editListView;
    Button editDiscardButton;
    Button editAcceptButton;
    MyEditScreenItemListAdapter myAdapter;
    FinalProjectItemListDataHolder itemListDataHolder = new FinalProjectItemListDataHolder();
    public static final String PREFS_FINAL_PROJ_FIRST_SCREEN = "MyPrefsFile_FinalProject_FirstScreen";
    private BroadcastReceiver mReceiver;
    Context context;
    ArrayList<Integer> noOfDays;
    String hourSharedPrefs;
    String minSharedPrefs;
    String daysSharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finalproject_edit_screen);

        editListView = (ListView) findViewById(R.id.finalProjEditListView);
        editDiscardButton = (Button) findViewById(R.id.finalProjEditDiscardButton);
        editAcceptButton = (Button) findViewById(R.id.finalProjEditAcceptButton);

        final SharedPreferences prefs = getSharedPreferences(PREFS_FINAL_PROJ_FIRST_SCREEN, 0);
        hourSharedPrefs = prefs.getString("hourSharedPrefs", "12");
        minSharedPrefs = prefs.getString("minSharedPrefs", "00");
        daysSharedPrefs = prefs.getString("daysSharedPrefs", "7");
        final SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String saveList = prefs.getString("saveList", "");
        FinalProjectItemListDataHolder dh = gson.fromJson(saveList,
                FinalProjectItemListDataHolder.class);
        itemListDataHolder.myItems = dh.myItems;

        itemListDataHolder.myItems.removeAll(Arrays.asList(null,""," "));

//        for (int i = 0; i < copyMyItems.size(); i++) {
//            if (copyMyItems.get(i) != null &&
//                    !copyMyItems.get(i).isEmpty()
//                    && !copyMyItems.get(i).equals(" ")) {
//
//                // do nothing
//            } else {
//                itemListDataHolder.myItems.remove(i);
//            }
//        }

        itemListDataHolder.numberOfDays = new ArrayList<String>();
        for (int i = 0; i < itemListDataHolder.myItems.size(); i++) {
            itemListDataHolder.numberOfDays.add(i, daysSharedPrefs);
        }

        myAdapter = new MyEditScreenItemListAdapter(this, R.layout.finalproject_edit_screen_item,
                itemListDataHolder.myItems, itemListDataHolder.numberOfDays);
        editListView.setAdapter(myAdapter);

        context = getApplicationContext();

        noOfDays = new ArrayList<Integer>();


        editDiscardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                editor.remove("saveList");
                editor.remove("save");
                editor.commit();
                itemListDataHolder.myItems.clear();
                itemListDataHolder.numberOfDays.clear();
                myAdapter.notifyDataSetChanged();
                Intent intent = new Intent(FinalProjectEditScreen.this, FinalProjectMainActivity.class);
                startActivity(intent);

            }
        });


        editAcceptButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                for (int i = 0; i < itemListDataHolder.numberOfDays.size(); i++) {
                    noOfDays.add(Integer.parseInt(itemListDataHolder.numberOfDays.get(i)));
                }

                Collections.sort(noOfDays);
                Integer day = noOfDays.get(noOfDays.size() - 1);


                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, day);
                Date newDate = c.getTime();


                String defaultDate = null;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                defaultDate = sdf.format(Calendar.getInstance().getTime());
                String dateSharedPrefs = prefs.getString("dateToCompare", defaultDate);

                Date date1 = null;
                try {
                    date1 = sdf.parse(dateSharedPrefs);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

//                Calendar cal = Calendar.getInstance();
//                cal.setTime(date1);


                int compare = newDate.compareTo(date1);
                if(compare == 1){
                    defaultDate = sdf.format(newDate);
                    editor.putString("dateToCompare", defaultDate);
                    editor.commit();
                }


                //TODO- write code for canceling alarm


                AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                Calendar calendar = Calendar.getInstance();

                calendar.setTimeInMillis(System.currentTimeMillis());
                // keep this code for final project
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourSharedPrefs));
                calendar.set(Calendar.MINUTE, Integer.parseInt(minSharedPrefs));

                // below code is only for testing
//                calendar.set(Calendar.HOUR_OF_DAY, 6);
//                calendar.set(Calendar.MINUTE, 28);

                Intent intent = new Intent(context, AlarmReceiver.class);

//                PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                //one time alarm
//                alarms.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                //setting repeating alarm
                alarms.cancel(pendingIntent);
                alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
//                alarms.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                        1000 * 60 * 1, pendingIntent);


                editor.remove("saveList");
                editor.remove("save");
                editor.commit();
                itemListDataHolder.myItems.clear();
                itemListDataHolder.numberOfDays.clear();
                myAdapter.notifyDataSetChanged();
                Intent newIntent = new Intent(FinalProjectEditScreen.this, FinalProjectLastScreen.class);
                startActivity(newIntent);
                finish();
                return;
            }
        });


    }



//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        SharedPreferences prefs = getSharedPreferences(PREFS_FINAL_PROJ_FIRST_SCREEN, 0);
//        boolean restore = prefs.getBoolean("saveFromSecondScreen", false);
//        if (restore) {
//            loadSavedPreferences();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        savePreferences();
//
//    }
//
//    public void loadSavedPreferences(){
//
//        SharedPreferences prefs = getSharedPreferences(PREFS_FINAL_PROJ_FIRST_SCREEN, 0);
//        Gson gson = new Gson();
//        String saveList = prefs.getString("saveListSecondScreen", "");
//        FinalProjectItemListDataHolder dh = gson.fromJson(saveList, FinalProjectItemListDataHolder.class);
//        itemListDataHolder.myItems = dh.myItems;
//        itemListDataHolder.numberOfDays = dh.numberOfDays;
//        myAdapter = new MyEditScreenItemListAdapter(this, R.layout.finalproject_edit_screen_item,
//                itemListDataHolder.myItems, itemListDataHolder.numberOfDays);
//        editListView.setAdapter(myAdapter);
//
//    }
//
//    public void savePreferences(){
//
//        String json = "";
//        Gson gson = new Gson();
//        json =  gson.toJson(itemListDataHolder);
//        SharedPreferences prefs = getSharedPreferences(PREFS_FINAL_PROJ_FIRST_SCREEN, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        Boolean saveFromSecondScreen;
//        saveFromSecondScreen = true;
//        editor.putString("saveListSecondScreen",json);
//        editor.putBoolean("saveFromSecondScreen", saveFromSecondScreen);
//        editor.commit();
//
//    }
//
//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();
//
//    }


}
