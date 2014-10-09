package com.example.puree.logs;

import android.content.Context;

import com.cookpad.android.puree.Puree;
import com.cookpad.android.puree.PureeConfiguration;
import com.cookpad.android.puree.handlers.BeforeEmitFilter;
import com.cookpad.android.puree.plugins.OutBufferedLogcat;
import com.cookpad.android.puree.plugins.OutLogcat;
import com.example.puree.AddEventTimeFilter;
import com.example.puree.logs.plugins.OutBufferedDisplay;
import com.example.puree.logs.plugins.OutDisplay;

public class PureeConfigurator {
    public static void configure(Context context) {
        Puree.initialize(buildConf(context));
    }

    public static PureeConfiguration buildConf(Context context) {
        BeforeEmitFilter addEventTimeFilter = new AddEventTimeFilter();
        return new PureeConfiguration.Builder(context)
                .registerOutput(new OutLogcat(), addEventTimeFilter)
                .registerOutput(new OutBufferedLogcat(), addEventTimeFilter)
                .registerOutput(new OutDisplay())
                .registerOutput(new OutBufferedDisplay(), addEventTimeFilter)
                .build();
    }
}
