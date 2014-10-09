package com.example.puree;

import com.cookpad.android.puree.PureeFilter;

import org.json.JSONException;
import org.json.JSONObject;

public class AddEventTimeFilter implements PureeFilter {
    public JSONObject call(JSONObject serializedLog) throws JSONException {
        serializedLog.put("event_time", System.currentTimeMillis());
        return serializedLog;
    }
}
