package com.example.loghouse;

import android.app.Application;

import com.cookpad.android.loghouse.LogHouseManager;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        LogHouseManager.initialize(this);
    }
}
