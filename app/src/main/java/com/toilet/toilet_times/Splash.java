package com.toilet.toilet_times;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import com.toilet.toilet_times.network.DataTransport;

public class Splash extends Activity {

    private final int SPLASH_DISPLAY_LENGHT = 3000;            //set your time here......

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash.this, MainActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);

        /* Create account and store user ID if none exists */
        final SharedPreferences p = getSharedPreferences("prefs", MODE_PRIVATE);
        int uid = p.getInt("userId", -1);
        if (uid == -1) {
            new Thread() {
                public void run() {
                    int result = DataTransport.createNewUser();
                    p.edit().putInt("userId", result).commit();
                }
            }.start();
        }
    }
}

