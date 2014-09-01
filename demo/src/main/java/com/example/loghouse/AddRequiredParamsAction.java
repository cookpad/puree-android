package com.example.loghouse;

import com.cookpad.android.loghouse.handlers.BeforeInsertAction;

import org.json.JSONException;
import org.json.JSONObject;

public class AddRequiredParamsAction implements BeforeInsertAction {
    @Override
    public JSONObject beforeInsert(JSONObject serializedLog) throws JSONException {
        serializedLog.put("event_time", System.currentTimeMillis());
        return serializedLog;
    }
}
