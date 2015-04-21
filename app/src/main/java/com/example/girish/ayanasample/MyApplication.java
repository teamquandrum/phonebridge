package com.example.girish.ayanasample;

import android.app.Application;

import com.parse.Parse;

public class MyApplication extends Application {
	
	@Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "gVqvo5FtmQxbpwwnbFYDr30kneCkTF63tlneJsms", "YkamtU7H99sHz2ayczjAS4c9CHUsjB8N2XNCyLST");
    }

}
