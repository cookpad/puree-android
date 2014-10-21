package com.cookpad.puree.storage;

import org.json.JSONArray;

import java.util.ArrayList;

public class Records extends ArrayList<Record> {

    public String getIdsAsString() {
        if (isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Record record : this) {
            builder.append(record.getId()).append(',');
        }
        return builder.substring(0, builder.length() - 1);
    }

    public JSONArray getSerializedLogs() {
        JSONArray serializedLogs = new JSONArray();
        for (Record record : this) {
            serializedLogs.put(record.getSerializedLog());
        }
        return serializedLogs;
    }
}
