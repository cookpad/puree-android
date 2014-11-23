package com.example.puree.logs;

import android.content.Context;

import com.cookpad.puree.Puree;
import com.cookpad.puree.PureeConfiguration;
import com.cookpad.puree.PureeFilter;
import com.cookpad.puree.plugins.OutBufferedLogcat;
import com.cookpad.puree.plugins.OutLogcat;
import com.example.puree.AddEventTimeFilter;
import com.example.puree.logs.filters.SamplingFilter;
import com.example.puree.logs.plugins.OutBufferedDisplay;
import com.example.puree.logs.plugins.OutDisplay;

public class PureeConfigurator {
    public static void configure(Context context) {
        Puree.initialize(buildConf(context));
    }

    public static PureeConfiguration buildConf(Context context) {
        PureeFilter addEventTimeFilter = new AddEventTimeFilter();
        return new PureeConfiguration.Builder(context)
                .registerOutput(new OutLogcat(), addEventTimeFilter)
                .registerOutput(new OutBufferedLogcat(), addEventTimeFilter)
                .registerOutput(new OutDisplay(), new SamplingFilter(0.5F))
                .registerOutput(new OutBufferedDisplay())
                .build();
    }
}
