package com.cookpad.android.loghouse;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Log {
    public JSONObject toJSON(Gson gson) {
        try {
            return new JSONObject(gson.toJson(this));
        } catch (JSONException e) {
            return new JSONObject();
        }
    }
}
