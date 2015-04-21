package com.example.girish.ayanasample;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmMessageHandler extends IntentService {
    String mes;
    private Handler handler;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
// The getMessageType() intent parameter must be the intent you received
// in your BroadcastReceiver.
        //String messageType = gcm.getMessageType(intent);
        // mes = extras.getString("title");
        if(extras.containsKey("url")){
            Intent shady = new Intent (getApplicationContext(),ImageViewer.class);
            shady.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shady.putExtra("url",extras.getString("url"));
            shady.putExtra("askerid",extras.getString("askerid"));
            shady.putExtra("helperid",extras.getString("helperid"));
            startActivity(shady);
        }
        else if(extras.containsKey("x")){
            Intent shady = new Intent (getApplicationContext(),ServiceFloating.class);
            shady.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            double x = Double.parseDouble(extras.getString("x"));
            double y = Double.parseDouble(extras.getString("y"));
            Log.e("GCM X",":"+x);
            Log.e("GCM Y",":"+y);
            String helperid = extras.getString("helperid");
            String askerid = extras.getString("askerid");
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putFloat("x",(float)x);
            editor.putFloat("y",(float)y);
            editor.putString("helperid", helperid);
            editor.putString("askerid",askerid);
            editor.apply();
            stopService(shady);
            startService(shady);
        }
        else {
            Intent shady = new Intent (getApplicationContext(),RUReadyActivity.class);
            shady.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shady.putExtra("askerid",extras.getString("askerid"));
            shady.putExtra("helperid",extras.getString("helperid"));
            startActivity(shady);
        }
        showToast();
        //Log.i("GCM", "Received : (" + messageType + ") " + extras.getString("title"));
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    public void showToast() {
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Thank you for tickling me!", Toast.LENGTH_LONG).show();
            }
        });
    }
}