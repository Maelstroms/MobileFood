package edu.neu.madcourse.dhvanisheth.scraggle;

import android.graphics.drawable.Drawable;
import android.widget.Button;

import java.io.Serializable;

import edu.neu.madcourse.dhvanisheth.R;

/**
 * Created by Dhvani on 3/11/2016.
 */
public class SavableButton implements Serializable {

    public int bgColor;
    public boolean enabled;
    public String text1;
    public int id;

    public static SavableButton convertToSavableButton(Button b){

        SavableButton sb = new SavableButton();
        sb.id = b.getId();

        if (b.getBackground().equals(R.drawable.button_blue))
            sb.bgColor = 0;
        else
            sb.bgColor = 1;


        sb.enabled = b.isEnabled();

        sb.text1 = b.getText().toString();
        return sb;
    }


    // Copy over details from SavableButton to Button.
    public static Button convertFromSavableButton(SavableButton sb, Button b){

        b.setId(sb.id);
        // Copy Color from SavableButton to Button
        if (sb.bgColor == 0)
            b.setBackgroundResource(R.drawable.button_blue);
        else
            b.setBackgroundResource(R.drawable.button_green);


        // Cope Enabled from SavableButton to Button
        b.setEnabled(sb.enabled);

        // Copy Text from SavableButton to Button.
        b.setText(sb.text1);

        return b;
    }

}
