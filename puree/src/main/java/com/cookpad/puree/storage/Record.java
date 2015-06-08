package com.cookpad.puree.storage;

import com.google.gson.JsonObject;

public class Record {

    private final int id;

    private final String type;

    private final JsonObject jsonLog;

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public JsonObject getJsonLog() {
        return jsonLog;
    }

    public Record(int id, String type, JsonObject jsonLog) {
        this.id = id;
        this.type = type;
        this.jsonLog = jsonLog;
    }
}
