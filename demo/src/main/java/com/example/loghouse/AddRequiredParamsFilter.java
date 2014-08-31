package com.example.loghouse;

import com.cookpad.android.loghouse.handlers.BeforeInsertFilter;

import org.json.JSONException;
import org.json.JSONObject;

public class AddRequiredParamsFilter implements BeforeInsertFilter {
    @Override
    public JSONObject beforeInsert(JSONObject serializedLog) throws JSONException {
        serializedLog.put("event_time", System.currentTimeMillis());
        return serializedLog;
    }
}
