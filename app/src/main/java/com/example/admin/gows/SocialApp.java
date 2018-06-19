package com.example.admin.gows;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by ADMIN on 1/18/2018.
 */

public class SocialApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}