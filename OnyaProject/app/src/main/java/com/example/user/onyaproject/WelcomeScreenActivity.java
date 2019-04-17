package com.example.user.onyaproject;

import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;

import android.widget.*;

public class WelcomeScreenActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome_screen);
        progressBar=(ProgressBar) findViewById(R.id.progressBarId);
        Thread  thread=new Thread(new Runnable() {
            @Override
            public void run() {
                dowork();
                startApp();
            }
        });
        thread.start();
    }
    public void dowork() {
        for(progress=20;progress<=100;progress=progress+20)
        {
            try {
                Thread.sleep(1000);
                progressBar.setProgress(progress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public void startApp()
    {
        Intent intent=new Intent(WelcomeScreenActivity.this,MainActivity.class);
        startActivity(intent);
        finish();


    }
}

