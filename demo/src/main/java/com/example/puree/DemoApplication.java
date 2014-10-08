package com.example.puree;

import android.app.Application;
import android.content.Context;

import com.cookpad.android.puree.Puree;
import com.cookpad.android.puree.PureeConfiguration;
import com.cookpad.android.puree.plugins.OutBufferedLogcat;
import com.cookpad.android.puree.plugins.OutLogcat;
import com.example.puree.logs.plugins.OutBufferedDisplay;
import com.example.puree.logs.plugins.OutDisplay;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        Puree.initialize(buildConfiguration(this));
    }

    public static PureeConfiguration buildConfiguration(Context context) {
        return new PureeConfiguration.Builder(context)
                .beforeEmitAction(new AddRequiredParamsFilter())
                .registerOutput(new OutLogcat())
                .registerOutput(new OutBufferedLogcat())
                .registerOutput(new OutDisplay())
                .registerOutput(new OutBufferedDisplay())
                .build();
    }
}
