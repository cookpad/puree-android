package com.cookpad.puree.storage;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class Record {
    private int id;
    private String type;
    private JSONObject jsonLog;

    public int getId() {
        return id;
    }

    public JSONObject getJsonLog() {
        return jsonLog;
    }

    public Record(Cursor cursor) throws JSONException {
        this(
                cursor.getInt(0),
                cursor.getString(1),
                new JSONObject(cursor.getString(2)));
    }

    public Record(int id, String type, JSONObject jsonLog) {
        this.id = id;
        this.type = type;
        this.jsonLog = jsonLog;
    }
}
