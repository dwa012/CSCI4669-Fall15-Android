// ContactListFragment.java
// Displays the list of contact names
package com.deitel.addressbook;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.deitel.addressbook.models.Contact;
import com.deitel.addressbook.util.AsyncTaskLoaderExample;

import java.util.List;

public class ContactListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Contact>>
{

   // callback methods implemented by MainActivity
   public interface ContactListFragmentListener
   {
      // called when user selects a contact
      public void onContactSelected(long rowID);

      // called when user decides to add a contact
      public void onAddContact();
   }
   
   private ContactListFragmentListener listener; 
   
   private ListView contactListView; // the ListActivity's ListView

   // set ContactListFragmentListener when fragment attached   
   @Override
   public void onAttach(Activity activity)
   {
      super.onAttach(activity);
      listener = (ContactListFragmentListener) activity;
   }

   // remove ContactListFragmentListener when Fragment detached
   @Override
   public void onDetach()
   {
      super.onDetach();
      listener = null;
   }

   // called after View is created
   @Override
   public void onViewCreated(View view, Bundle savedInstanceState)
   {
      super.onViewCreated(view, savedInstanceState);
      setRetainInstance(true); // save fragment across config changes
      setHasOptionsMenu(true); // this fragment has menu items to display

      // set text to display when there are no contacts
      setEmptyText(getResources().getString(R.string.no_contacts));

      // get ListView reference and configure ListView
      contactListView = getListView(); 
      contactListView.setOnItemClickListener(viewContactListener);      
      contactListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      getLoaderManager().initLoader(0, new Bundle(), this);
   }

   // responds to the user touching a contact's name in the ListView
   OnItemClickListener viewContactListener = new OnItemClickListener() 
   {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id)
      {
          Contact contact = (Contact) parent.getItemAtPosition(position);
         listener.onContactSelected(contact.getId()); // pass selection to MainActivity
      } 
   }; // end viewContactListener

   // display this fragment's menu items
   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
   {
      super.onCreateOptionsMenu(menu, inflater);
      inflater.inflate(R.menu.fragment_contact_list_menu, menu);
   }

   // handle choice from options menu
   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {
      switch (item.getItemId())
      {
         case R.id.action_add:
            listener.onAddContact();
            return true;
      }
      
      return super.onOptionsItemSelected(item); // call super's method
   }

   @Override
   public Loader<List<Contact>> onCreateLoader(int id, Bundle args) {
      return new AsyncTaskLoaderExample<List<Contact>>(getActivity()) {
          @Override
          public List<Contact> loadInBackground() {
              return new Select().from(Contact.class).orderBy("name asc").execute();
          }
      };
   }

   @Override
   public void onLoadFinished(Loader<List<Contact>> loader, List<Contact> data) {
       setListAdapter(new ContactAdapter(getActivity(), android.R.layout.simple_list_item_1, data));
   }

   @Override
   public void onLoaderReset(Loader<List<Contact>> loader) {
       setListAdapter(null);
   }

   // update data set
   public void updateContactList()
   {
      getLoaderManager().restartLoader(0, new Bundle(), this);
   }


    public class ContactAdapter extends ArrayAdapter<Contact> {
        int resourceId;

        Context context;

        public ContactAdapter(Context context, int resource, List<Contact> objects) {
            super(context, resource, objects);

            this.context = context;
            this.resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
// assign the view we are converting to a local variable
            View v = convertView;

            // first check to see if the view is null. if so, we have to inflate it.
            // to inflate it basically means to render, or show, the view.
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(android.R.layout.simple_list_item_1, null);
            }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
            Contact c = getItem(position);

            TextView textView = (TextView) v.findViewById(android.R.id.text1);
            textView.setText(c.name);

            return v;
        }
    }

} // end class ContactListFragment


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
