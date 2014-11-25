package com.example.puree;

import com.cookpad.puree.PureeFilter;

import org.json.JSONException;
import org.json.JSONObject;

public class AddEventTimeFilter implements PureeFilter {
    public JSONObject apply(JSONObject jsonLog) throws JSONException {
        jsonLog.put("event_time", System.currentTimeMillis());
        return jsonLog;
    }
}
