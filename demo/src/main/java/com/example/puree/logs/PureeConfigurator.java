package com.example.puree.logs;

import android.content.Context;

import com.cookpad.puree.Puree;
import com.cookpad.puree.PureeConfiguration;
import com.cookpad.puree.PureeFilter;
import com.cookpad.puree.plugins.OutBufferedLogcat;
import com.cookpad.puree.plugins.OutLogcat;
import com.example.puree.AddEventTimeFilter;
import com.example.puree.logs.plugins.OutDisplay;

public class PureeConfigurator {
    public static void configure(Context context) {
        Puree.initialize(buildConf(context));
    }

    public static PureeConfiguration buildConf(Context context) {
        PureeFilter addEventTimeFilter = new AddEventTimeFilter();
        PureeConfiguration conf = new PureeConfiguration.Builder(context)
                .source(ClickLog.class).to(new OutDisplay())
                .source(ClickLog.class).filter(addEventTimeFilter).to(new OutBufferedLogcat())
                .source(PvLog.class).filter(addEventTimeFilter).to(new OutLogcat())
//                .registerOutput(new OutDisplay(), new SamplingFilter(0.5F)) // you can sampling logs
                .build();
        conf.printMapping();
        return conf;
    }
}
