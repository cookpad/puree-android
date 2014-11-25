package com.cookpad.puree;

import org.json.JSONException;
import org.json.JSONObject;

public interface PureeFilter {
    public JSONObject apply(JSONObject jsonLog) throws JSONException;
}
