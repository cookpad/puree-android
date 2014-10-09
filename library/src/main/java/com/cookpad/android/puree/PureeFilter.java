package com.cookpad.android.puree;

import org.json.JSONException;
import org.json.JSONObject;

public interface PureeFilter {
    public JSONObject call(JSONObject serializedLog) throws JSONException;
}
