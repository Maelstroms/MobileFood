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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import edu.neu.madcourse.dhvanisheth.R;

public class FinalProjectLastScreen extends Activity {

    TextView line1;
    TextView line2;
    TextView line3;
    ImageButton close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finalproject_last_screen);

        line1 = (TextView) findViewById(R.id.finalProjLastScreenLine1);
        line2 = (TextView) findViewById(R.id.finalProjLastScreenLine2);
        line3 = (TextView) findViewById(R.id.finalProjLastScreenLine3);
        close = (ImageButton) findViewById(R.id.finalProjLastScreenClose);

        close.setImageResource(R.drawable.ic_close_black_18dp);


        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
                return;

            }
        });


    }

}
