package com.pankaj.facebooklogindemo;

import android.app.Application;

import com.facebook.FacebookSdk;

public class FbApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());


    }
}
