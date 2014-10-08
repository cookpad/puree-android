package com.cookpad.android.puree.handlers;

import org.json.JSONException;
import org.json.JSONObject;

public interface BeforeEmitFilter {
    public static final BeforeEmitFilter DEFAULT = new BeforeEmitFilter() {
        @Override
        public JSONObject call(JSONObject serializedLog) throws JSONException {
            return serializedLog;
        }
    };

    public JSONObject call(JSONObject serializedLog) throws JSONException;
}
