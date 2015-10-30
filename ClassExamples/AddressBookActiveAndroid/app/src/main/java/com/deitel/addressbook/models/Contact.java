package com.deitel.addressbook.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by danielward on 10/29/15.
 */
@Table(name = "contacts")
public class Contact extends Model {

    @Column(name = "name")
    public String name;

    @Column(name = "phone")
    public String phone;

    @Column(name = "email")
    public String email;

    @Column(name = "street")
    public String street;

    @Column(name = "city")
    public String city;

    @Column(name = "state")
    public String state;

    @Column(name = "zip")
    public String zip;

}
