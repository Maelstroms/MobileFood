package edu.neu.madcourse.dhvanisheth.finalproject;

import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.dhvanisheth.scraggle.SavableButton;
import edu.neu.madcourse.dhvanisheth.scraggle.SubmitWord;


public class FinalProjectItemListDataHolder implements Serializable{

    public ArrayList<String> myItems;
    public ArrayList<String> numberOfDays;

}