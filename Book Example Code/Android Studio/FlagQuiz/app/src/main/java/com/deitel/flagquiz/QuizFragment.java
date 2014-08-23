// QuizFragment.java
// Contains the Flag Quiz logic
package com.deitel.flagquiz;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuizFragment extends Fragment 
{
   // String used when logging error messages
   private static final String TAG = "FlagQuiz Activity";

   private static final int FLAGS_IN_QUIZ = 10; 
   
   private List<String> fileNameList; // flag file names
   private List<String> quizCountriesList; // countries in current quiz
   private Set<String> regionsSet; // world regions in current quiz
   private String correctAnswer; // correct country for the current flag
   private int totalGuesses; // number of guesses made
   private int correctAnswers; // number of correct guesses
   private int guessRows; // number of rows displaying guess Buttons
   private SecureRandom random; // used to randomize the quiz
   private Handler handler; // used to delay loading next flag
   private Animation shakeAnimation; // animation for incorrect guess
   
   private TextView questionNumberTextView; // shows current question #
   private ImageView flagImageView; // displays a flag
   private LinearLayout[] guessLinearLayouts; // rows of answer Buttons
   private TextView answerTextView; // displays Correct! or Incorrect!
   
   // configures the QuizFragment when its View is created
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
   {
      super.onCreateView(inflater, container, savedInstanceState);    
      View view = 
         inflater.inflate(R.layout.fragment_quiz, container, false);

      fileNameList = new ArrayList<String>();
      quizCountriesList = new ArrayList<String>();
      random = new SecureRandom(); 
      handler = new Handler(); 

      // load the shake animation that's used for incorrect answers
      shakeAnimation = AnimationUtils.loadAnimation(getActivity(), 
         R.anim.incorrect_shake); 
      shakeAnimation.setRepeatCount(3); // animation repeats 3 times 

      // get references to GUI components
      questionNumberTextView = 
         (TextView) view.findViewById(R.id.questionNumberTextView);
      flagImageView = (ImageView) view.findViewById(R.id.flagImageView);
      guessLinearLayouts = new LinearLayout[3];
      guessLinearLayouts[0] = 
         (LinearLayout) view.findViewById(R.id.row1LinearLayout);
      guessLinearLayouts[1] = 
         (LinearLayout) view.findViewById(R.id.row2LinearLayout);
      guessLinearLayouts[2] = 
         (LinearLayout) view.findViewById(R.id.row3LinearLayout);
      answerTextView = (TextView) view.findViewById(R.id.answerTextView);

      // configure listeners for the guess Buttons
      for (LinearLayout row : guessLinearLayouts)
      {
         for (int column = 0; column < row.getChildCount(); column++) 
         {
            Button button = (Button) row.getChildAt(column);
            button.setOnClickListener(guessButtonListener);
         }
      }  
      
      // set questionNumberTextView's text
      questionNumberTextView.setText(
         getResources().getString(R.string.question, 1, FLAGS_IN_QUIZ));
      return view; // returns the fragment's view for display
   } // end method onCreateView
   
   // update guessRows based on value in SharedPreferences
   public void updateGuessRows(SharedPreferences sharedPreferences)
   {
      // get the number of guess buttons that should be displayed
      String choices = 
         sharedPreferences.getString(MainActivity.CHOICES, null);
      guessRows = Integer.parseInt(choices) / 3;

      // hide all guess button LinearLayouts
      for (LinearLayout layout : guessLinearLayouts)
         layout.setVisibility(View.INVISIBLE);

      // display appropriate guess button LinearLayouts 
      for (int row = 0; row < guessRows; row++) 
         guessLinearLayouts[row].setVisibility(View.VISIBLE);
   }
   
   // update world regions for quiz based on values in SharedPreferences
   public void updateRegions(SharedPreferences sharedPreferences)
   {
      regionsSet = 
         sharedPreferences.getStringSet(MainActivity.REGIONS, null);
   }  

   // set up and start the next quiz 
   public void resetQuiz() 
   {      
      // use AssetManager to get image file names for enabled regions
      AssetManager assets = getActivity().getAssets(); 
      fileNameList.clear(); // empty list of image file names
      
      try 
      {
         // loop through each region
         for (String region : regionsSet) 
         {
            // get a list of all flag image files in this region
            String[] paths = assets.list(region);

            for (String path : paths) 
               fileNameList.add(path.replace(".png", ""));
         } 
      } 
      catch (IOException exception) 
      {
         Log.e(TAG, "Error loading image file names", exception);
      } 
      
      correctAnswers = 0; // reset the number of correct answers made
      totalGuesses = 0; // reset the total number of guesses the user made
      quizCountriesList.clear(); // clear prior list of quiz countries
      
      int flagCounter = 1; 
      int numberOfFlags = fileNameList.size(); 

      // add FLAGS_IN_QUIZ random file names to the quizCountriesList
      while (flagCounter <= FLAGS_IN_QUIZ) 
      {
         int randomIndex = random.nextInt(numberOfFlags); 

         // get the random file name
         String fileName = fileNameList.get(randomIndex);
         
         // if the region is enabled and it hasn't already been chosen
         if (!quizCountriesList.contains(fileName)) 
         {
            quizCountriesList.add(fileName); // add the file to the list
            ++flagCounter;
         } 
      } 

      loadNextFlag(); // start the quiz by loading the first flag
   } // end method resetQuiz

   // after the user guesses a correct flag, load the next flag
   private void loadNextFlag() 
   {
      // get file name of the next flag and remove it from the list
      String nextImage = quizCountriesList.remove(0);
      correctAnswer = nextImage; // update the correct answer
      answerTextView.setText(""); // clear answerTextView 

      // display current question number
      questionNumberTextView.setText(
         getResources().getString(R.string.question, 
            (correctAnswers + 1), FLAGS_IN_QUIZ));

      // extract the region from the next image's name
      String region = nextImage.substring(0, nextImage.indexOf('-'));

      // use AssetManager to load next image from assets folder
      AssetManager assets = getActivity().getAssets(); 

      try
      {
         // get an InputStream to the asset representing the next flag
         InputStream stream = 
            assets.open(region + "/" + nextImage + ".png");
         
         // load the asset as a Drawable and display on the flagImageView
         Drawable flag = Drawable.createFromStream(stream, nextImage);
         flagImageView.setImageDrawable(flag);                       
      } 
      catch (IOException exception)  
      {
         Log.e(TAG, "Error loading " + nextImage, exception);
      } 

      Collections.shuffle(fileNameList); // shuffle file names

      // put the correct answer at the end of fileNameList
      int correct = fileNameList.indexOf(correctAnswer);
      fileNameList.add(fileNameList.remove(correct));

      // add 3, 6, or 9 guess Buttons based on the value of guessRows
      for (int row = 0; row < guessRows; row++) 
      {
         // place Buttons in currentTableRow
         for (int column = 0; 
            column < guessLinearLayouts[row].getChildCount(); column++) 
         { 
            // get reference to Button to configure
            Button newGuessButton = 
               (Button) guessLinearLayouts[row].getChildAt(column);
            newGuessButton.setEnabled(true);

            // get country name and set it as newGuessButton's text
            String fileName = fileNameList.get((row * 3) + column);
            newGuessButton.setText(getCountryName(fileName));
         } 
      } 
      
      // randomly replace one Button with the correct answer
      int row = random.nextInt(guessRows); // pick random row
      int column = random.nextInt(3); // pick random column
      LinearLayout randomRow = guessLinearLayouts[row]; // get the row
      String countryName = getCountryName(correctAnswer);
      ((Button) randomRow.getChildAt(column)).setText(countryName);    
   } // end method loadNextFlag

   // parses the country flag file name and returns the country name
   private String getCountryName(String name)
   {
      return name.substring(name.indexOf('-') + 1).replace('_', ' ');
   } 
   
   // called when a guess Button is touched
   private OnClickListener guessButtonListener = new OnClickListener() 
   {
      @Override
      public void onClick(View v) 
      {
         Button guessButton = ((Button) v); 
         String guess = guessButton.getText().toString();
         String answer = getCountryName(correctAnswer);
         ++totalGuesses; // increment number of guesses the user has made
         
         if (guess.equals(answer)) // if the guess is correct
         {
            ++correctAnswers; // increment the number of correct answers

            // display correct answer in green text
            answerTextView.setText(answer + "!");
            answerTextView.setTextColor(
               getResources().getColor(R.color.correct_answer));

            disableButtons(); // disable all guess Buttons
            
            // if the user has correctly identified FLAGS_IN_QUIZ flags
            if (correctAnswers == FLAGS_IN_QUIZ) 
            {
               // DialogFragment to display quiz stats and start new quiz
               DialogFragment quizResults = 
                  new DialogFragment()
                  {
                     // create an AlertDialog and return it
                     @Override
                     public Dialog onCreateDialog(Bundle bundle)
                     {
                        AlertDialog.Builder builder = 
                           new AlertDialog.Builder(getActivity());
                        builder.setCancelable(false); 
                        
                        builder.setMessage(
                           getResources().getString(R.string.results, 
                           totalGuesses, (1000 / (double) totalGuesses)));
                        
                        // "Reset Quiz" Button                              
                        builder.setPositiveButton(R.string.reset_quiz,
                           new DialogInterface.OnClickListener()                
                           {                                                       
                              public void onClick(DialogInterface dialog, 
                                 int id) 
                              {
                                 resetQuiz();                                      
                              } 
                           } // end anonymous inner class
                        ); // end call to setPositiveButton
                        
                        return builder.create(); // return the AlertDialog
                     } // end method onCreateDialog   
                  }; // end DialogFragment anonymous inner class
               
               // use FragmentManager to display the DialogFragment
               quizResults.show(getFragmentManager(), "quiz results");
            } 
            else // answer is correct but quiz is not over 
            {
               // load the next flag after a 1-second delay
               handler.postDelayed(
                  new Runnable()
                  { 
                     @Override
                     public void run()
                     {
                        loadNextFlag();
                     }
                  }, 2000); // 2000 milliseconds for 2-second delay
            } 
         } 
         else // guess was incorrect  
         {
            flagImageView.startAnimation(shakeAnimation); // play shake

            // display "Incorrect!" in red 
            answerTextView.setText(R.string.incorrect_answer);
            answerTextView.setTextColor(
               getResources().getColor(R.color.incorrect_answer));
            guessButton.setEnabled(false); // disable incorrect answer
         } 
      } // end method onClick
   }; // end answerButtonListener

   // utility method that disables all answer Buttons 
   private void disableButtons()
   {
      for (int row = 0; row < guessRows; row++)
      {
         LinearLayout guessRow = guessLinearLayouts[row];
         for (int i = 0; i < guessRow.getChildCount(); i++)
            guessRow.getChildAt(i).setEnabled(false);
      } 
   } 
} // end class FlagQuiz

     
/*************************************************************************
* (C) Copyright 1992-2014 by Deitel & Associates, Inc. and               *
* Pearson Education, Inc. All Rights Reserved.                           *
*                                                                        *
* DISCLAIMER: The authors and publisher of this book have used their     *
* best efforts in preparing the book. These efforts include the          *
* development, research, and testing of the theories and programs        *
* to determine their effectiveness. The authors and publisher make       *
* no warranty of any kind, expressed or implied, with regard to these    *
* programs or to the documentation contained in these books. The authors *
* and publisher shall not be liable in any event for incidental or       *
* consequential damages in connection with, or arising out of, the       *
* furnishing, performance, or use of these programs.                     *
*************************************************************************/
