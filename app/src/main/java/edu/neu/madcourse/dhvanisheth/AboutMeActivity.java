/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.dhvanisheth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class AboutMeActivity extends Activity {

    TelephonyManager mngr;
    String imeinumber;
    // ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        /* Getting IMEI Number */
        mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imeinumber = mngr.getDeviceId();
        TextView txtView = (TextView) findViewById(R.id.imeiNumberTextView);
        txtView.setText(imeinumber);

    }

}
