package com.example.a18199.a16211160204niewei.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.a18199.a16211160204niewei.R;

public class LaunchActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // getSupportActionBar().hide();
        setContentView(R.layout.layout_launch);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim, R.anim.in);
                finish();
            }
        }, 1000);
    }
}

