package com.cookpad.puree;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class JsonConvertible {
    public JSONObject toJson(JsonStringifier jsonStringifier) {
        try {
            return new JSONObject(jsonStringifier.toJson(this));
        } catch (JSONException e) {
            return new JSONObject();
        }
    }
}
