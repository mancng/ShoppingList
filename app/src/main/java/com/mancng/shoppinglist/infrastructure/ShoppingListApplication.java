package com.mancng.shoppinglist.infrastructure;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.client.Firebase;
import com.mancng.shoppinglist.live.Module;
import com.squareup.otto.Bus;

public class ShoppingListApplication extends Application  {

    private Bus bus;

    public ShoppingListApplication() {
        bus = new Bus();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Module.Register(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

    }

    public Bus getBus() {
        return bus;
    }
}
