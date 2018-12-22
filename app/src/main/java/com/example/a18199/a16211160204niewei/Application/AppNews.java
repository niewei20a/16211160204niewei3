package com.example.a18199.a16211160204niewei.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

public class AppNews extends LitePalApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        Connector.getDatabase();
    }
}
