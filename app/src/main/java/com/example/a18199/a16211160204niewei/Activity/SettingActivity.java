package com.example.a18199.a16211160204niewei.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;


import com.example.a18199.a16211160204niewei.R;
import com.example.a18199.a16211160204niewei.SettingFragment;
import com.example.a18199.a16211160204niewei.Utils.SPUtils;
import com.example.a18199.a16211160204niewei.Utils.ToastUtil;

public class SettingActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {

        init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        getFragmentManager().beginTransaction().replace(R.id.setting_fragment, new SettingFragment()).commit();

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
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.anim, R.anim.in);
    }
}
