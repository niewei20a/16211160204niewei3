package com.example.a18199.a16211160204niewei.Application;

import com.example.a18199.a16211160204niewei.Utils.SPUtils;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

public class AppNews extends LitePalApplication {
    private String font;
    private String color;
    private boolean picture;
    private boolean day;
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
        Connector.getDatabase();
        SPUtils.getInstance(this,"users");

        if(SPUtils.getData("isfirst",true)) {
            SPUtils.putData("color", "white");
            SPUtils.putData("font", "medium");
            SPUtils.putData("theme", "day");
            SPUtils.putData("picture", "have");
            SPUtils.putData("isfirst", false);
        }
    }
}
