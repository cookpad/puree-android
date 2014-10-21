package com.cookpad.android.puree;

import org.json.JSONException;
import org.json.JSONObject;

public interface PureeFilter {
    public JSONObject apply(JSONObject serializedLog) throws JSONException;
}
