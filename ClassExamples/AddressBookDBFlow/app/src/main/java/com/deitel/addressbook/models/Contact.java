package com.deitel.addressbook.models;

import com.deitel.addressbook.db.ContactDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by danielward on 11/3/15.
 */
@Table(databaseName = ContactDatabase.NAME)
public class Contact extends BaseModel {

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

}
