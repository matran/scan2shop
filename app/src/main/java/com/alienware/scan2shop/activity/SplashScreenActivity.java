package com.alienware.scan2shop.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.alienware.scan2shop.R;

/**
 * Created by henry cheruiyot on 2/5/2018.
 */
public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_layout);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent localIntent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                startActivity(localIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
