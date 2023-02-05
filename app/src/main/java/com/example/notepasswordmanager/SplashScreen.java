package com.example.notepasswordmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    TextView tv1;
    TextView tv2;
    private static int Splash_timeout = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashintent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(splashintent);
                finish();
            }
        }, Splash_timeout);
        Animation myanimation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.animation2);
        tv1.startAnimation(myanimation);
        Animation myanimation2 = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.animation1);
        tv2.startAnimation(myanimation2);
    }

}