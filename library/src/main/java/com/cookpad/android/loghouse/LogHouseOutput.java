package com.cookpad.android.loghouse;

import org.json.JSONObject;

import java.util.List;

public abstract class LogHouseOutput {
    public abstract boolean emit(List<JSONObject> serializedLogs);
}
