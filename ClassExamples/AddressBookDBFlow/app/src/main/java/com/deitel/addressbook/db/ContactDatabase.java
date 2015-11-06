package com.deitel.addressbook.db;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.provider.ContentProvider;

/**
 * Created by danielward on 11/3/15.
 */
@ContentProvider(authority = ContactDatabase.AUTHORITY,
        databaseName = ContactDatabase.NAME,
        baseContentUri = ContactDatabase.BASE_CONTENT_URI)
@Database(name = ContactDatabase.NAME, version = ContactDatabase.VERSION)
public class ContactDatabase {

    public static final String NAME = "Contacts";

    public static final String AUTHORITY = "edu.uno.csci.4661.addressbook";
    public static final String BASE_CONTENT_URI = "content://";

    public static final int VERSION = 1;
}
