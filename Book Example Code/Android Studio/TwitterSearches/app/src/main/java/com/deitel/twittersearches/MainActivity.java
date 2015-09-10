// MainActivity.java
// Manages your favorite Twitter searches for easy  
// access and display in the device's web browser
package com.deitel.twittersearches;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity
{
   // name of SharedPreferences XML file that stores the saved searches 
   private static final String SEARCHES = "searches";
   
   private EditText queryEditText; // EditText where user enters a query
   private EditText tagEditText; // EditText where user tags a query
   private SharedPreferences savedSearches; // user's favorite searches
   private ArrayList<String> tags; // list of tags for saved searches
   private RecyclerView recyclerView;
   private SearchesAdapter adapter;
   // called when MainActivity is first created
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // get references to the EditTexts  
      queryEditText = (EditText) findViewById(R.id.queryEditText);
      tagEditText = (EditText) findViewById(R.id.tagEditText);
      
      // get the SharedPreferences containing the user's saved searches 
      savedSearches = getSharedPreferences(SEARCHES, MODE_PRIVATE); 

      // store the saved tags in an ArrayList then sort them
      tags = new ArrayList<String>(savedSearches.getAll().keySet());
      Collections.sort(tags, String.CASE_INSENSITIVE_ORDER);

      recyclerView = (RecyclerView) findViewById(R.id.list);

      // create ArrayAdapter and use it to bind tags to the ListView
      adapter = new SearchesAdapter();
      recyclerView.setAdapter(adapter);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));

      // register listener to save a new or edited search 
      ImageButton saveButton = 
         (ImageButton) findViewById(R.id.saveButton);
      saveButton.setOnClickListener(saveButtonListener);

   } // end method onCreate

   // saveButtonListener saves a tag-query pair into SharedPreferences
   public OnClickListener saveButtonListener = new OnClickListener() 
   {
      @Override
      public void onClick(View v) 
      {
         // create tag if neither queryEditText nor tagEditText is empty
         if (queryEditText.getText().length() > 0 &&
            tagEditText.getText().length() > 0)
         {
            addTaggedSearch(queryEditText.getText().toString(), 
               tagEditText.getText().toString());
            queryEditText.setText(""); // clear queryEditText
            tagEditText.setText(""); // clear tagEditText
            
            ((InputMethodManager) getSystemService(
               Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
               tagEditText.getWindowToken(), 0);  
         } 
         else // display message asking user to provide a query and a tag
         {
            // create a new AlertDialog Builder
            AlertDialog.Builder builder = 
               new AlertDialog.Builder(MainActivity.this);

            // set dialog's message to display
            builder.setMessage(R.string.missingMessage);
            
            // provide an OK button that simply dismisses the dialog
            builder.setPositiveButton(R.string.OK, null); 
            
            // create AlertDialog from the AlertDialog.Builder
            AlertDialog errorDialog = builder.create();
            errorDialog.show(); // display the modal dialog
         } 
      } // end method onClick
   }; // end OnClickListener anonymous inner class

   // add new search to the save file, then refresh all Buttons
   private void addTaggedSearch(String query, String tag)
   {
      // get a SharedPreferences.Editor to store new tag/query pair
      SharedPreferences.Editor preferencesEditor = savedSearches.edit();
      preferencesEditor.putString(tag, query); // store current search
      preferencesEditor.apply(); // store the updated preferences
      
      // if tag is new, add to and sort tags, then display updated list
      if (!tags.contains(tag))
      {
         tags.add(tag); // add new tag
         Collections.sort(tags, String.CASE_INSENSITIVE_ORDER);
         adapter.notifyDataSetChanged(); // rebind tags to ListView
      }
   } 
   
   // itemClickListener launches a web browser to display search results
   OnClickListener itemClickListener = new OnClickListener()
   {
      @Override
      public void onClick(View v) {
         // get query string and create a URL representing the search
         String tag = ((TextView) v.findViewById(R.id.textView)).getText().toString();
         String urlString = getString(R.string.searchURL) +
                 Uri.encode(savedSearches.getString(tag, ""), "UTF-8");

         // create an Intent to launch a web browser
         Intent webIntent = new Intent(Intent.ACTION_VIEW,
                 Uri.parse(urlString));

         startActivity(webIntent); // launches web browser to view results
      }

   }; // end itemClickListener declaration
   
   // itemLongClickListener displays a dialog allowing the user to delete 
   // or edit a saved search
   View.OnLongClickListener itemLongClickListener =
      new View.OnLongClickListener()
      {
         @Override
         public boolean onLongClick(View v) {
            // get the tag that the user long touched
            final String tag = ((TextView) v.findViewById(R.id.textView)).getText().toString();

            // create a new AlertDialog
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(MainActivity.this);

            // set the AlertDialog's title
            builder.setTitle(
                    getString(R.string.shareEditDeleteTitle, tag));

            // set list of items to display in dialog
            builder.setItems(R.array.dialog_items,
                    new DialogInterface.OnClickListener()
                    {
                       // responds to user touch by sharing, editing or
                       // deleting a saved search
                       @Override
                       public void onClick(DialogInterface dialog, int which)
                       {
                          switch (which)
                          {
                             case 0: // share
                                shareSearch(tag);
                                break;
                             case 1: // edit
                                // set EditTexts to match chosen tag and query
                                tagEditText.setText(tag);
                                queryEditText.setText(
                                        savedSearches.getString(tag, ""));
                                break;
                             case 2: // delete
                                deleteSearch(tag);
                                break;
                          }
                       }
                    } // end DialogInterface.OnClickListener
            ); // end call to builder.setItems

            // set the AlertDialog's negative Button
            builder.setNegativeButton(getString(R.string.cancel),
                    new DialogInterface.OnClickListener()
                    {
                       // called when the "Cancel" Button is clicked
                       public void onClick(DialogInterface dialog, int id)
                       {
                          dialog.cancel(); // dismiss the AlertDialog
                       }
                    }
            ); // end call to setNegativeButton

            builder.create().show(); // display the AlertDialog
            return true;
         }
      }; // end OnItemLongClickListener declaration

   // allows user to choose an app for sharing a saved search's URL
   private void shareSearch(String tag)
   {
      // create the URL representing the search
      String urlString = getString(R.string.searchURL) +
         Uri.encode(savedSearches.getString(tag, ""), "UTF-8");

      // create Intent to share urlString
      Intent shareIntent = new Intent();
      shareIntent.setAction(Intent.ACTION_SEND);
      shareIntent.putExtra(Intent.EXTRA_SUBJECT,
              getString(R.string.shareSubject));
      shareIntent.putExtra(Intent.EXTRA_TEXT,
              getString(R.string.shareMessage, urlString));
      shareIntent.setType("text/plain");
      
      // display apps that can share text
      startActivity(Intent.createChooser(shareIntent,
              getString(R.string.shareSearch)));
   }

   // deletes a search after the user confirms the delete operation
   private void deleteSearch(final String tag)
   {
      // create a new AlertDialog
      AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
      
      // set the AlertDialog's message
      confirmBuilder.setMessage(
         getString(R.string.confirmMessage, tag));

      // set the AlertDialog's negative Button
      confirmBuilder.setNegativeButton( getString(R.string.cancel), 
         new DialogInterface.OnClickListener() 
         {
            // called when "Cancel" Button is clicked
            public void onClick(DialogInterface dialog, int id) 
            {
               dialog.cancel(); // dismiss dialog
            } 
         } 
      ); // end call to setNegativeButton
      
      // set the AlertDialog's positive Button
      confirmBuilder.setPositiveButton(getString(R.string.delete), 
         new DialogInterface.OnClickListener() 
         {
            // called when "Cancel" Button is clicked
            public void onClick(DialogInterface dialog, int id) 
            {
               tags.remove(tag); // remove tag from tags
               
               // get SharedPreferences.Editor to remove saved search
               SharedPreferences.Editor preferencesEditor = 
                  savedSearches.edit();                   
               preferencesEditor.remove(tag); // remove search
               preferencesEditor.apply(); // saves the changes

               // rebind tags ArrayList to ListView to show updated list
               adapter.notifyDataSetChanged();                    
            }
         } // end OnClickListener
      ); // end call to setPositiveButton

      confirmBuilder.create().show(); // display AlertDialog    
   } // end method deleteSearch

   // Create the basic adapter extending from RecyclerView.Adapter
   // Note that we specify the custom ViewHolder which gives us access to our views
   public class SearchesAdapter extends RecyclerView.Adapter<ViewHolder> {

      // Usually involves inflating a layout from XML and returning the holder
      @Override
      public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         Context context = parent.getContext();
         LayoutInflater inflater = LayoutInflater.from(context);

         // Inflate the custom layout
         View contactView = inflater.inflate(R.layout.list_item, parent, false);

         contactView.setOnClickListener(itemClickListener);
         contactView.setOnLongClickListener(itemLongClickListener);

         // Return a new holder instance
         ViewHolder viewHolder = new ViewHolder(contactView);
         return viewHolder;
      }

      // Involves populating data into the item through holder
      @Override
      public void onBindViewHolder(ViewHolder viewHolder, int position) {
         // Get the data model based on position
         String search = tags.get(position);

         // Set item views based on the data model
         TextView textView = viewHolder.textView;
         textView.setText(search);
      }

      // Return the total count of items
      @Override
      public int getItemCount() {
         return tags.size();
      }
   }

   // Provide a direct reference to each of the views within a data item
   // Used to cache the views within the item layout for fast access
   public static class ViewHolder extends RecyclerView.ViewHolder {
      // Your holder should contain a member variable
      // for any view that will be set as you render a row
      public TextView textView;

      // We also create a constructor that accepts the entire item row
      // and does the view lookups to find each subview
      public ViewHolder(View itemView) {
         // Stores the itemView in a public final member variable that can be used
         // to access the context from any ViewHolder instance.
         super(itemView);

         textView = (TextView) itemView.findViewById(R.id.textView);
      }
   }

} // end class MainActivity


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