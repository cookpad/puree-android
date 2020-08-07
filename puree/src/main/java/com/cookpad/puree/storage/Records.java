package com.cookpad.puree.storage;

import java.util.ArrayList;
import java.util.List;

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

    public List<String> getJsonLogs() {
        List<String> jsonLogs = new ArrayList<>();
        for (Record record : this) {
            jsonLogs.add(record.getJsonLog());
        }
        return jsonLogs;
    }
}
