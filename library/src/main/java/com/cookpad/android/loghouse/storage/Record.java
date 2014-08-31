package com.cookpad.android.loghouse.storage;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class Record {
    private int id;
    private JSONObject serializedLog;

    public int getId() {
        return id;
    }

    public JSONObject getSerializedLog() {
        return serializedLog;
    }

    public Record(Cursor cursor) throws JSONException {
        this(cursor.getInt(0), new JSONObject(cursor.getString(1)));
    }

    public Record(int id, JSONObject serializedLog) {
        this.id = id;
        this.serializedLog = serializedLog;
    }
}
