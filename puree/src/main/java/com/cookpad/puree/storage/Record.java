package com.cookpad.puree.storage;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.database.Cursor;

public class Record {
    private int id;
    private String type;

    private JsonObject jsonLog;

    public int getId() {
        return id;
    }

    public JsonObject getJsonLog() {
        return jsonLog;
    }

    public Record(Cursor cursor, Gson gson) {
        this(
                cursor.getInt(0),
                cursor.getString(1),
                (JsonObject) gson.toJsonTree(cursor.getString(2)));
    }

    public Record(int id, String type, JsonObject jsonLog) {
        this.id = id;
        this.type = type;
        this.jsonLog = jsonLog;
    }
}
