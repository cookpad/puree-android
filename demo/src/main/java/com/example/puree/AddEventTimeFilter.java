package com.example.puree;

import com.google.gson.JsonObject;

import com.cookpad.puree.PureeFilter;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AddEventTimeFilter implements PureeFilter {

    public JsonObject apply(JsonObject jsonLog) {
        jsonLog.addProperty("event_time", System.currentTimeMillis());
        return jsonLog;
    }
}
