// AddEditFragment.java
// Allows user to add a new contact or edit an existing one
package com.deitel.addressbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class AddEditFragment extends Fragment
{
   // callback method implemented by MainActivity  
   public interface AddEditFragmentListener
   {
      // called after edit completed so contact can be redisplayed
      public void onAddEditCompleted(long rowID);
   }
   
   private AddEditFragmentListener listener; 
   
   private long rowID; // database row ID of the contact
   private Bundle contactInfoBundle; // arguments for editing a contact

   // EditTexts for contact information
   private EditText nameEditText;
   private EditText phoneEditText;
   private EditText emailEditText;
   private EditText streetEditText;
   private EditText cityEditText;
   private EditText stateEditText;
   private EditText zipEditText;

   // set AddEditFragmentListener when Fragment attached   
   @Override
   public void onAttach(Activity activity)
   {
      super.onAttach(activity);
      listener = (AddEditFragmentListener) activity; 
   }

   // remove AddEditFragmentListener when Fragment detached
   @Override
   public void onDetach()
   {
      super.onDetach();
      listener = null; 
   }
   
   // called when Fragment's view needs to be created
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
   {
      super.onCreateView(inflater, container, savedInstanceState);    
      setRetainInstance(true); // save fragment across config changes
      setHasOptionsMenu(true); // fragment has menu items to display
      
      // inflate GUI and get references to EditTexts
      View view = 
         inflater.inflate(R.layout.fragment_add_edit, container, false);
      nameEditText = (EditText) view.findViewById(R.id.nameEditText);
      phoneEditText = (EditText) view.findViewById(R.id.phoneEditText);
      emailEditText = (EditText) view.findViewById(R.id.emailEditText);
      streetEditText = (EditText) view.findViewById(R.id.streetEditText);
      cityEditText = (EditText) view.findViewById(R.id.cityEditText);
      stateEditText = (EditText) view.findViewById(R.id.stateEditText);
      zipEditText = (EditText) view.findViewById(R.id.zipEditText);

      contactInfoBundle = getArguments(); // null if creating new contact

      if (contactInfoBundle != null)
      {
         rowID = contactInfoBundle.getLong(MainActivity.ROW_ID);
         nameEditText.setText(contactInfoBundle.getString("name"));  
         phoneEditText.setText(contactInfoBundle.getString("phone"));  
         emailEditText.setText(contactInfoBundle.getString("email"));  
         streetEditText.setText(contactInfoBundle.getString("street"));  
         cityEditText.setText(contactInfoBundle.getString("city"));  
         stateEditText.setText(contactInfoBundle.getString("state"));  
         zipEditText.setText(contactInfoBundle.getString("zip"));  
      } 
      
      // set Save Contact Button's event listener 
      Button saveContactButton = 
         (Button) view.findViewById(R.id.saveContactButton);
      saveContactButton.setOnClickListener(saveContactButtonClicked);
      return view;
   }

   // responds to event generated when user saves a contact
   OnClickListener saveContactButtonClicked = new OnClickListener() 
   {
      @Override
      public void onClick(View v) 
      {
         if (nameEditText.getText().toString().trim().length() != 0)
         {
            // AsyncTask to save contact, then notify listener 
            AsyncTask<Object, Object, Object> saveContactTask = 
               new AsyncTask<Object, Object, Object>() 
               {
                  @Override
                  protected Object doInBackground(Object... params) 
                  {
                     saveContact(); // save contact to the database
                     return null;
                  } 
      
                  @Override
                  protected void onPostExecute(Object result) 
                  {
                     // hide soft keyboard
                     InputMethodManager imm = (InputMethodManager) 
                        getActivity().getSystemService(
                           Context.INPUT_METHOD_SERVICE);
                     imm.hideSoftInputFromWindow(
                        getView().getWindowToken(), 0);

                     listener.onAddEditCompleted(rowID);
                  } 
               }; // end AsyncTask
               
            // save the contact to the database using a separate thread
            saveContactTask.execute((Object[]) null); 
         } 
         else // required contact name is blank, so display error dialog
         {
            DialogFragment errorSaving = 
               new DialogFragment()
               {
                  @Override
                  public Dialog onCreateDialog(Bundle savedInstanceState)
                  {
                     AlertDialog.Builder builder = 
                        new AlertDialog.Builder(getActivity());
                     builder.setMessage(R.string.error_message);
                     builder.setPositiveButton(R.string.ok, null);                     
                     return builder.create();
                  }               
               };
            
            errorSaving.show(getFragmentManager(), "error saving contact");
         } 
      } // end method onClick
   }; // end OnClickListener saveContactButtonClicked

   // saves contact information to the database
   private void saveContact() 
   {
      // get DatabaseConnector to interact with the SQLite database
      DatabaseConnector databaseConnector = 
         new DatabaseConnector(getActivity());

      if (contactInfoBundle == null)
      {
         // insert the contact information into the database
         rowID = databaseConnector.insertContact(
            nameEditText.getText().toString(),
            phoneEditText.getText().toString(), 
            emailEditText.getText().toString(), 
            streetEditText.getText().toString(),
            cityEditText.getText().toString(), 
            stateEditText.getText().toString(), 
            zipEditText.getText().toString());
      } 
      else
      {
         databaseConnector.updateContact(rowID,
            nameEditText.getText().toString(),
            phoneEditText.getText().toString(), 
            emailEditText.getText().toString(), 
            streetEditText.getText().toString(),
            cityEditText.getText().toString(), 
            stateEditText.getText().toString(), 
            zipEditText.getText().toString());
      }
   } // end method saveContact
} // end class AddEditFragment


/**************************************************************************
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
 **************************************************************************/
