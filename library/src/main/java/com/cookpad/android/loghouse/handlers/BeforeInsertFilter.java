package com.cookpad.android.loghouse.handlers;

import org.json.JSONException;
import org.json.JSONObject;

public interface BeforeInsertFilter {
    public JSONObject beforeInsert(JSONObject serializedLog) throws JSONException;
}
