package com.example.puree;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.cookpad.puree.PureeFilter;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AddEventTimeFilter implements PureeFilter {

    public String apply(String jsonLog) {
        JsonObject jsonObject = new JsonParser().parse(jsonLog).getAsJsonObject();
        jsonObject.addProperty("event_time", System.currentTimeMillis());
        return jsonObject.toString();
    }
}
