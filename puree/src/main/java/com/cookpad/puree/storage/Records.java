package com.cookpad.puree.storage;

import com.google.gson.JsonArray;

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

    public JsonArray getJsonLogs() {
        JsonArray jsonLogs = new JsonArray();
        for (Record record : this) {
            jsonLogs.add(record.getJsonLog());
        }
        return jsonLogs;
    }
}
