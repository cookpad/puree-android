package com.example.puree.logs;

import com.cookpad.puree.JsonConvertible;
import com.cookpad.puree.JsonStringifier;
import com.cookpad.puree.Puree;
import com.cookpad.puree.PureeConfiguration;
import com.cookpad.puree.PureeFilter;
import com.cookpad.puree.plugins.OutBufferedLogcat;
import com.cookpad.puree.plugins.OutLogcat;
import com.example.puree.AddEventTimeFilter;
import com.example.puree.logs.filters.SamplingFilter;
import com.example.puree.logs.plugins.OutDisplay;
import com.google.gson.Gson;

import android.content.Context;

public class PureeConfigurator {
    public static void configure(Context context) {
        Puree.initialize(buildConf(context));
    }

    public static PureeConfiguration buildConf(Context context) {
        PureeFilter addEventTimeFilter = new AddEventTimeFilter();
        PureeFilter samplingFilter = new SamplingFilter(1.0f);
        PureeConfiguration conf = new PureeConfiguration.Builder(context)
                .register(ClickLog.class, new OutDisplay().withFilters(addEventTimeFilter))
                .register(ClickLog.class,
                        new OutBufferedLogcat().withFilters(addEventTimeFilter, samplingFilter))
                .register(PvLog.class, new OutLogcat().withFilters(addEventTimeFilter))
                .jsonStringifier(new JsonStringifier() {
                    private final Gson gson = new Gson();
                    @Override
                    public String toJson(JsonConvertible jsonConvertible) {
                        return gson.toJson(jsonConvertible);
                    }
                })
                .build();
        conf.printMapping();
        return conf;
    }
}
