package com.cookpad.android.puree.handlers;

import org.json.JSONObject;

import java.util.List;

public abstract class AfterFlushFilter implements PureeFilter {
    public abstract void call(String type, List<JSONObject> serializedLogs);
}
