package com.example.puree;

import com.example.puree.logs.PureeConfigurator;

import android.app.Application;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        PureeConfigurator.configure(this);
    }
}
