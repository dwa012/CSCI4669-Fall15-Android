package com.deitel.addressbook.models;

import android.net.Uri;
import android.util.Log;

import com.deitel.addressbook.db.ContactDatabase;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.annotation.provider.ContentUri;
import com.raizlabs.android.dbflow.annotation.provider.TableEndpoint;
import com.raizlabs.android.dbflow.structure.provider.BaseSyncableProviderModel;
import com.raizlabs.android.dbflow.structure.provider.ContentUtils;

import java.net.URI;

/**
 * Created by danielward on 11/3/15.
 */
@Table(databaseName = ContactDatabase.NAME,insertConflict = ConflictAction.REPLACE)
@TableEndpoint(name = Contact.NAME, contentProviderName = "ContactDatabase")
public class Contact extends BaseSyncableProviderModel<Contact> implements ParseConvertible {

    public static final String NAME = "Contact";
    public static final String NULL_PLACEHOLDER = "-!&|`@";

    @ContentUri(path = NAME, type = ContentUri.ContentType.VND_MULTIPLE + NAME)
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContentUtils.buildUri(ContactDatabase.AUTHORITY), NAME);

    @Column
    @PrimaryKey(autoincrement = true)
    public
    long id;

    @Column
    @Unique
    public String parseId;

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

    @Override
    public ParseObject toParseObject() throws ParseException {

        ParseObject temp = new ParseObject(NAME);

        if (parseId != null && !("null".equals(parseId))) {
            temp = new ParseQuery<>(NAME).get(this.parseId);
        }

        temp.put("name",    this.name == null ? NULL_PLACEHOLDER : this.name);
        temp.put("phone",   this.phone == null ? NULL_PLACEHOLDER : this.phone);
        temp.put("street",  this.street == null ? NULL_PLACEHOLDER : this.street);
        temp.put("city",    this.city == null ? NULL_PLACEHOLDER : this.city);
        temp.put("email",   this.email == null ? NULL_PLACEHOLDER : this.email);
        temp.put("state",   this.state == null ? NULL_PLACEHOLDER : this.state);
        temp.put("zip",     this.zip == null ? NULL_PLACEHOLDER : this.zip);
        temp.put("local_id",this.id);

        return temp;
    }

    @Override
    public void fromParseObject(ParseObject object) {
        this.id      = object.getLong("local_id");
        this.name    = NULL_PLACEHOLDER.equals(object.getString("name")) ? NULL_PLACEHOLDER : object.getString("name");
        this.phone   = NULL_PLACEHOLDER.equals(object.getString("phone")) ? NULL_PLACEHOLDER : object.getString("phone");
        this.street  = NULL_PLACEHOLDER.equals(object.getString("street")) ? NULL_PLACEHOLDER : object.getString("street");
        this.city    = NULL_PLACEHOLDER.equals(object.getString("city")) ? NULL_PLACEHOLDER : object.getString("city");
        this.email   = NULL_PLACEHOLDER.equals(object.getString("email")) ? NULL_PLACEHOLDER : object.getString("email");
        this.state   = NULL_PLACEHOLDER.equals(object.getString("state")) ? NULL_PLACEHOLDER : object.getString("state");
        this.zip     = NULL_PLACEHOLDER.equals(object.getString("zip")) ? NULL_PLACEHOLDER : object.getString("zip");
        this.parseId = object.getObjectId();
    }
}
