package com.deitel.addressbook.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by danielward on 11/3/15.
 */
@Database(name = ContactDatabase.NAME, version = ContactDatabase.VERSION)
public class ContactDatabase {

    public static final String NAME = "Contacts";

    public static final int VERSION = 1;
}
