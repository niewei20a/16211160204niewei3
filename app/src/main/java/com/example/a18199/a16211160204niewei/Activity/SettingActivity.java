package com.example.a18199.a16211160204niewei.Activity;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;


import com.example.a18199.a16211160204niewei.R;
import com.example.a18199.a16211160204niewei.Tab.SettingFragment;
import com.example.a18199.a16211160204niewei.Utils.SPUtils;

public class SettingActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        Toolbar toolbar =  findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitle("设置");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.setting_FrameLayout, new SettingFragment()).commit();

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void init() {
        String font_size = SPUtils.getData("font", "");
        switch (font_size) {
            case "medium":
                setTheme(R.style.Default_TextSize_Middle);
                break;
            case "small":
                setTheme(R.style.Default_TextSize_Small);
                break;
            case "big":
                setTheme(R.style.Default_TextSize_Big);
                break;
            default:
                setTheme(R.style.Default_TextSize_Middle);
                break;
        }
        String day_night = SPUtils.getData("theme", "");
        if (day_night.equals("day") || day_night.equals("")) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.finish();
        return super.onKeyDown(keyCode, event);
    }
}
