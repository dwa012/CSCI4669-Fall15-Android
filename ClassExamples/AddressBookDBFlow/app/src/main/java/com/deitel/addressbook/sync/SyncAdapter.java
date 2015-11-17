package com.deitel.addressbook.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.deitel.addressbook.db.ContactDatabase;
import com.deitel.addressbook.models.Contact;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.provider.ContentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d("addressbook", " a sync occurred");

        // ----------------------------
        // Upload the new contacts

        List<Contact> dbContacts = new Select().from(Contact.class).queryList();
        List<ParseObject> parseObjects = new ArrayList<>(dbContacts.size());
        for (Contact contact : dbContacts) {
            try {
                parseObjects.add(contact.toParseObject());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        try {
            ParseObject.saveAll(parseObjects);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //----------------------
        // GET THE NEW CONTACTS

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Contact.NAME);

        try {
            List<ParseObject> objects = query.find();
            List<Contact> contacts = new ArrayList<>(objects.size());

            for (ParseObject object: objects) {
                Contact temp = new Contact();
                temp.fromParseObject(object);
                contacts.add(temp);

                if (temp.id > 0) {
                    ContentUtils.update(mContentResolver, Contact.CONTENT_URI, temp);
                } else {
                    ContentUtils.insert(mContentResolver, Contact.CONTENT_URI, temp);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}