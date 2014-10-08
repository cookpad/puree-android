package com.example.puree;

import android.app.Application;
import android.content.Context;

import com.cookpad.android.puree.LogHouse;
import com.cookpad.android.puree.LogHouseConfiguration;
import com.cookpad.android.puree.plugins.OutBufferedLogcat;
import com.cookpad.android.puree.plugins.OutLogcat;
import com.example.puree.logs.plugins.OutBufferedDisplay;
import com.example.puree.logs.plugins.OutDisplay;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        LogHouse.initialize(buildConfiguration(this));
    }

    public static LogHouseConfiguration buildConfiguration(Context context) {
        return new LogHouseConfiguration.Builder(context)
                .beforeEmitAction(new AddRequiredParamsFilter())
                .registerOutput(new OutLogcat())
                .registerOutput(new OutBufferedLogcat())
                .registerOutput(new OutDisplay())
                .registerOutput(new OutBufferedDisplay())
                .build();
    }
}
