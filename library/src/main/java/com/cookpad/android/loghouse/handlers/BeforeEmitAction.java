package com.cookpad.android.loghouse.handlers;

import org.json.JSONException;
import org.json.JSONObject;

public interface BeforeEmitAction {
    public static final BeforeEmitAction DEFAULT = new BeforeEmitAction() {
        @Override
        public JSONObject call(JSONObject serializedLog) throws JSONException {
            return serializedLog;
        }
    };

    public JSONObject call(JSONObject serializedLog) throws JSONException;
}
