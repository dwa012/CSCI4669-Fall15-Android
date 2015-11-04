package com.deitel.addressbook;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by danielward on 11/3/15.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }

}
