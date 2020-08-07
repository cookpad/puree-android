package com.cookpad.puree.storage;

public class Record {

    private final int id;

    private final String type;

    private final String jsonLog;

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getJsonLog() {
        return jsonLog;
    }

    public Record(int id, String type, String jsonLog) {
        this.id = id;
        this.type = type;
        this.jsonLog = jsonLog;
    }
}
