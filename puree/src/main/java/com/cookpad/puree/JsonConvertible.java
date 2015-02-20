package com.cookpad.puree;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class JsonConvertible {
    public JSONObject toJson(Gson gson) {
        try {
            return new JSONObject(gson.toJson(this));
        } catch (JSONException e) {
            return new JSONObject();
        }
    }
}
