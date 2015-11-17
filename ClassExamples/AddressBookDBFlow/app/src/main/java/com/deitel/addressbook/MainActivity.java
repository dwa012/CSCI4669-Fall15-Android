// MainActivity.java
// Hosts Address Book app's fragments
package com.deitel.addressbook;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.deitel.addressbook.db.ContactDatabase;
import com.deitel.addressbook.models.Contact;
import com.parse.ParseObject;
import com.raizlabs.android.dbflow.structure.provider.ContentUtils;

public class MainActivity extends Activity
        implements ContactListFragment.ContactListFragmentListener,
        DetailsFragment.DetailsFragmentListener,
        AddEditFragment.AddEditFragmentListener
{
    // keys for storing row ID in Bundle passed to a fragment
    public static final String ROW_ID = "row_id";

    ContactListFragment contactListFragment; // displays contact list
    private Account account;

    // display ContactListFragment when MainActivity first loads
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // return if Activity is being restored, no need to recreate GUI
        if (savedInstanceState != null)
            return;

        // check whether layout contains fragmentContainer (phone layout);
        // ContactListFragment is always displayed
        if (findViewById(R.id.fragmentContainer) != null)
        {
            // create ContactListFragment
            contactListFragment = new ContactListFragment();

            // add the fragment to the FrameLayout
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, contactListFragment);
            transaction.commit(); // causes ContactListFragment to display
        }

        try {
            this.account = CreateSyncAccount(this);
            Log.d("addressbook", "Account name: " + this.account.name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // called when MainActivity resumes
    @Override
    protected void onResume()
    {
        super.onResume();

        // if contactListFragment is null, activity running on tablet,
        // so get reference from FragmentManager
        if (contactListFragment == null)
        {
            contactListFragment =
                    (ContactListFragment) getFragmentManager().findFragmentById(
                            R.id.contactListFragment);
        }
    }

    // display DetailsFragment for selected contact
    @Override
    public void onContactSelected(long rowID)
    {
        if (findViewById(R.id.fragmentContainer) != null) // phone
            displayContact(rowID, R.id.fragmentContainer);
        else // tablet
        {
            getFragmentManager().popBackStack(); // removes top of back stack
            displayContact(rowID, R.id.rightPaneContainer);
        }
    }

    // display a contact
    private void displayContact(long rowID, int viewID)
    {
        DetailsFragment detailsFragment = new DetailsFragment();

        // specify rowID as an argument to the DetailsFragment
        Bundle arguments = new Bundle();
        arguments.putLong(ROW_ID, rowID);
        detailsFragment.setArguments(arguments);

        // use a FragmentTransaction to display the DetailsFragment
        FragmentTransaction transaction =
                getFragmentManager().beginTransaction();
        transaction.replace(viewID, detailsFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes DetailsFragment to display
    }

    // display the AddEditFragment to add a new contact
    @Override
    public void onAddContact()
    {
        if (findViewById(R.id.fragmentContainer) != null)
            displayAddEditFragment(R.id.fragmentContainer, null);
        else
            displayAddEditFragment(R.id.rightPaneContainer, null);
    }

    // display fragment for adding a new or editing an existing contact
    private void displayAddEditFragment(int viewID, Bundle arguments)
    {
        AddEditFragment addEditFragment = new AddEditFragment();

        if (arguments != null) // editing existing contact
            addEditFragment.setArguments(arguments);

        // use a FragmentTransaction to display the AddEditFragment
        FragmentTransaction transaction =
                getFragmentManager().beginTransaction();
        transaction.replace(viewID, addEditFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes AddEditFragment to display
    }

    // return to contact list when displayed contact deleted
    @Override
    public void onContactDeleted()
    {
        getFragmentManager().popBackStack(); // removes top of back stack

        if (findViewById(R.id.fragmentContainer) == null) // tablet
            contactListFragment.updateContactList();
    }

    // display the AddEditFragment to edit an existing contact
    @Override
    public void onEditContact(Bundle arguments)
    {
        if (findViewById(R.id.fragmentContainer) != null) // phone
            displayAddEditFragment(R.id.fragmentContainer, arguments);
        else // tablet
            displayAddEditFragment(R.id.rightPaneContainer, arguments);
    }

    // update GUI after new contact or updated contact saved
    @Override
    public void onAddEditCompleted(long rowID)
    {
        getFragmentManager().popBackStack(); // removes top of back stack

        if (findViewById(R.id.fragmentContainer) == null) // tablet
        {
            getFragmentManager().popBackStack(); // removes top of back stack
            contactListFragment.updateContactList(); // refresh contacts

            // on tablet, display contact that was just added or edited
            displayContact(rowID, R.id.rightPaneContainer);
        }
    }

    public void refreshData() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        ContentResolver.requestSync(account, ContactDatabase.AUTHORITY, settingsBundle);
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) throws Exception {

        String ACCOUNT_TYPE = "4661.cs.uno.edu";

        // Create the account type and default account
        Account newAccount = new Account("dummyaccount", ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            // we need to enable the syncing.
            ContentResolver.setIsSyncable(newAccount, ContactDatabase.AUTHORITY, 1);
            ContentResolver.setSyncAutomatically(newAccount, ContactDatabase.AUTHORITY, true);

        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */

            Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);

            if (accounts.length > 0) {
                newAccount = accounts[0];
            } else {
                throw new Exception("Could not add account");
            }
        }

        return newAccount;
    }
}


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
