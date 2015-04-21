package com.example.girish.ayanasample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    private final int SPLASH_DISPLAY_LENGHT = 2000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(icicle);

        setContentView(R.layout.activity_main);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                File f = new File(
                        "/data/data/com.example.girish.ayanasample/shared_prefs/MyPrefs.xml");
                if (f.exists()) {
                    Intent mainIntent = new Intent(MainActivity.this,QAActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }
                else {
                    Intent mainIntent = new Intent(MainActivity.this,RegActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }


            }
        }, SPLASH_DISPLAY_LENGHT);
    }

}
