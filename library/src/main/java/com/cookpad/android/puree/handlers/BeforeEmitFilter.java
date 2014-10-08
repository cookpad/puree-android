package com.cookpad.android.puree.handlers;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BeforeEmitFilter implements PureeFilter {
    public abstract JSONObject call(JSONObject serializedLog) throws JSONException;
}
