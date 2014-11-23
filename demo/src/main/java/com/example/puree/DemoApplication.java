package com.example.puree;

import android.app.Application;

import com.cookpad.puree.Puree;
import com.example.puree.logs.PureeConfigurator;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        PureeConfigurator.configure(this);
        Puree.flush();
    }
}
