package com.cookpad.android.loghouse.handlers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public interface BeforeInsertAction {
    public static final BeforeInsertAction DEFAULT = new BeforeInsertAction() {
        @Override
        public JSONObject call(JSONObject serializedLog) throws JSONException {
            return serializedLog;
        }
    };

    public JSONObject call(JSONObject serializedLog) throws JSONException;
}
