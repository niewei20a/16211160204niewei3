package com.example.a18199.a16211160204niewei.Application;

import android.content.Context;

import com.example.a18199.a16211160204niewei.Utils.SPUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

public class AppNews extends LitePalApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
        Connector.getDatabase();

        SPUtils.getInstance(this, "users");
        if (SPUtils.getData("isfirst", true)) {
            SPUtils.putData("color", "white");
            SPUtils.putData("font", "medium");
            SPUtils.putData("theme", "day");
            SPUtils.putData("picture", "have");
            SPUtils.putData("isfirst", false);
        }
    }

}
