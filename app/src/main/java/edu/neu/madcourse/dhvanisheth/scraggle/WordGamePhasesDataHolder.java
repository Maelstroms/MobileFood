package edu.neu.madcourse.dhvanisheth.scraggle;

import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class WordGamePhasesDataHolder implements Serializable{

    public int phaseNumber = 1;
    public List<Integer> correctWordGrid = new ArrayList<Integer>();
    public List<Integer> wrongWordGrid = new ArrayList<Integer>();
    public List<Integer> notAttemptedGrid = new ArrayList<Integer>();
    public int latestButtonIndex = 0;
    public SavableButton sbpreviousbutton;
    public List<SavableButton> sbselectedButtonList = new ArrayList<SavableButton>();
    public List<SavableButton> sbcorrectWordButtonList = new ArrayList<SavableButton>();

    public List<SavableButton> sballMyButtons = new ArrayList<SavableButton>();

    // Convert from Button to SavableButton
    public void ConvertButtonsToSave(Button previousButton,
                                     List<Button> selectedButtonList,
                                     List<Button> correctWordButtonList, List<Button> allMyButtons) {

        if (previousButton != null)
            sbpreviousbutton = SavableButton.convertToSavableButton(previousButton);


        if(selectedButtonList != null)
            for (Button b : selectedButtonList)
                sbselectedButtonList.add(SavableButton.convertToSavableButton(b));


        if (correctWordButtonList != null)
            for (Button b : correctWordButtonList)
                sbcorrectWordButtonList.add(SavableButton.convertToSavableButton(b));

        if (allMyButtons != null)
            for (Button b : allMyButtons)
                sballMyButtons.add(SavableButton.convertToSavableButton(b));

    }



    // Copy all properties of SavableButtons into Buttons.
    public void ConvertToButtons(Button previousButton,
                                 List<Button> selectedButtonList,
                                 List<Button> correctWordButtonList, List<Button> allMyButtons) {

        // Get all My Buttons.
        if (allMyButtons != null)
            for(int i = 0; i < allMyButtons.size(); ++i)
                SavableButton.convertFromSavableButton(
                        sballMyButtons.get(i),
                        allMyButtons.get(i));




/*        if (previousButton != null)
            SavableButton.convertFromSavableButton(sbpreviousbutton, previousButton);
        else {
            int sbPrev = -1;
            for (int i = 0; i < allMyButtons.size(); ++i)
                if(allMyButtons.get(i).getId() == sbpreviousbutton.id) {
                    sbPrev = i;
                    break;
                }
            previousButton = allMyButtons.get(sbPrev);
            SavableButton.convertFromSavableButton(sbpreviousbutton, previousButton);
        }*/



        /*if (selectedButtonList != null)
            for(int i = 0; i < selectedButtonList.size(); ++i)
                SavableButton.convertFromSavableButton(
                        this.selectedButtonList.get(i),
                        selectedButtonList.get(i));

        if (correctWordButtonList != null)
            for(int i = 0; i < allMyButtons.size(); ++i)
                SavableButton.convertFromSavableButton(
                        this.correctWordButtonList.get(i),
                        correctWordButtonList.get(i));*/



    }


    public List<String> arrayList;

    public boolean music = true;

    public boolean timerStopped = false;

    public long millisRemaining;

    public int timerIndicator = 0;

    public int gridPlayed;
    public int previousGridPlayed;

    public String submitWordCheck;

    public String submitWordSearch;

    public int scorephase1 = 0;

    public SubmitWord sw = new SubmitWord();

    public int count = 1;
    public Integer indexToBlank = 0;


    public List<String> allButtonList;
    public List<String> dhpreviousbutton ;
    public List<String> dhcorrectWordButtonList;
    public List<String> dhselectedButtonList;
    public List<String> submittedWords;

    public List<String> words;
}