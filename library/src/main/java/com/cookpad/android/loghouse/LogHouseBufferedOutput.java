package com.cookpad.android.loghouse;

import org.json.JSONObject;

import java.util.List;

public class LogHouseBufferedOutput extends LogHouseOutput {
    @Override
    public boolean emit(List<JSONObject> serializedLogs) {
        return true;
    }
}
