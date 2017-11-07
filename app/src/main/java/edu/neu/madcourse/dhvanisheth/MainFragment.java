/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.dhvanisheth;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MainFragment extends Fragment {

   private AlertDialog mDialog;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View rootView =
            inflater.inflate(R.layout.fragment_main, container, false);
      // Handle buttons here...
      View tictactoeButton = rootView.findViewById(R.id.tictactoe_button);
      View genearateerrorButton = rootView.findViewById(R.id.generateerror_button);
      View aboutmeButton = rootView.findViewById(R.id.aboutme_button);
      View quitButton = rootView.findViewById(R.id.quit_button);
       View dictionaryButton = rootView.findViewById(R.id.dictionary_button);
       View wordgameButton = rootView.findViewById(R.id.word_game_button);
       View communicationButton = rootView.findViewById(R.id.communication_button);
       View twoPlayerWordGameButton = rootView.findViewById(R.id.two_player_word_game_button);
       View trickiestPartButton = rootView.findViewById(R.id.trickiest_part_button);
       View finalProjectButton = rootView.findViewById(R.id.final_project_button);

      tictactoeButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent intent = new Intent(getActivity(), MainActivity2.class);
            getActivity().startActivity(intent);
         }
      });


      aboutmeButton.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View view) {
              Intent intent = new Intent(getActivity(), AboutMeActivity.class);
              getActivity().startActivity(intent);

          }
      });

       quitButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Exit the app", true);
            getActivity().startActivity(intent);
         }
      });

       genearateerrorButton.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {

               Integer i = null;
               i.doubleValue();
           }
       });

       dictionaryButton.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getActivity(), DictionaryActivity.class);
               getActivity().startActivity(intent);
           }
       });

       wordgameButton.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {

               Intent intent = new Intent(getActivity(), edu.neu.madcourse.dhvanisheth.scraggle.MainActivity.class);
               getActivity().startActivity(intent);
           }
       });

       communicationButton.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {

               Intent intent = new Intent(getActivity(), edu.neu.madcourse.dhvanisheth.communication.CommunicationStartGame.class);
               getActivity().startActivity(intent);
           }
       });

       twoPlayerWordGameButton.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {

               Intent intent = new Intent(getActivity(), edu.neu.madcourse.dhvanisheth.twoplayerwordgame.TwoPlayerWordGameMainActivity.class);
               getActivity().startActivity(intent);
           }
       });

       trickiestPartButton.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {

               Intent intent = new Intent(getActivity(), edu.neu.madcourse.dhvanisheth.trickiestpart.TrickiestPartMainActivity.class);
//               Intent intent = new Intent(getActivity(), edu.neu.madcourse.dhvanisheth.trickiestpart.MicTestActivity.class);
               getActivity().startActivity(intent);
           }
       });

       finalProjectButton.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {

               Intent intent = new Intent(getActivity(), edu.neu.madcourse.dhvanisheth.
                       finalproject.FinalProjectAppNameAndDescription.class);
               getActivity().startActivity(intent);
           }
       });

      return rootView;
   }



   @Override
   public void onPause() {
      super.onPause();

      // Get rid of the about dialog if it's still up
      if (mDialog != null)
         mDialog.dismiss();
   }
}
