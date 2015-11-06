package com.deitel.addressbook.models;

import android.net.Uri;

import com.deitel.addressbook.db.ContactDatabase;
import com.deitel.addressbook.db.ContactDatabase$Provider;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.provider.ContentUri;
import com.raizlabs.android.dbflow.annotation.provider.TableEndpoint;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.provider.BaseProviderModel;
import com.raizlabs.android.dbflow.structure.provider.BaseSyncableProviderModel;
import com.raizlabs.android.dbflow.structure.provider.ContentUtils;

/**
 * Created by danielward on 11/3/15.
 */
@Table(databaseName = ContactDatabase.NAME)
@TableEndpoint(name = Contact.NAME, contentProviderName = "ContactDatabase")
public class Contact extends BaseSyncableProviderModel<Contact> {

    public static final String NAME = "Contact";

    @ContentUri(path = NAME, type = ContentUri.ContentType.VND_MULTIPLE + NAME)
    public static final Uri CONTENT_URI = ContentUtils.buildUri(ContactDatabase.AUTHORITY);

    @Column
    @PrimaryKey(autoincrement = true)
    public
    long id;

    @Column
    public String name;

    @Column
    public String phone;

    @Column
    public String email;

    @Column
    public String street;

    @Column
    public String city;

    @Column
    public String state;

    @Column
    public String zip;

    @Override
    public Uri getDeleteUri() {
        return Contact.CONTENT_URI;
    }

    @Override
    public Uri getInsertUri() {
        return Contact.CONTENT_URI;
    }

    @Override
    public Uri getUpdateUri() {
        return Contact.CONTENT_URI;
    }

    @Override
    public Uri getQueryUri() {
        return Contact.CONTENT_URI;
    }
}
