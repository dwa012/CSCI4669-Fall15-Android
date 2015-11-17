package com.deitel.addressbook;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by danielward on 11/3/15.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);

        // ----------------------------------------
        // parse.com initialization

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "3wSsQcPJIAdnBXsY12AejkriV3SZLLIB50Sbg9cr", "iKQl7X2XxjJAspn6khttMKLZaWbeJinu8WmUt23O");

        // create a new user for this app install instance
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

}
