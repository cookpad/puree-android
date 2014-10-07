package com.example.loghouse;

import com.cookpad.android.loghouse.handlers.BeforeEmitFilter;

import org.json.JSONException;
import org.json.JSONObject;

public class AddRequiredParamsFilter implements BeforeEmitFilter {
    @Override
    public JSONObject call(JSONObject serializedLog) throws JSONException {
        serializedLog.put("event_time", System.currentTimeMillis());
        return serializedLog;
    }
}
