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

    /**
     *
     * @param start start index of the subList (inclusive).
     * @param end end index of the subList, (exclusive).
     * @return a subList view of this list starting from {@code start}
     *         (inclusive), and ending with {@code end} (exclusive)
     */
    public Records getSubList(int start, int end){
        ArrayList<Record> list = new ArrayList<>(this.subList(start, end));
        Records subListRecords = new Records();
        subListRecords.addAll(list);
        return subListRecords;
    }

    public JsonArray getJsonLogs() {
        JsonArray jsonLogs = new JsonArray();
        for (Record record : this) {
            jsonLogs.add(record.getJsonLog());
        }
        return jsonLogs;
    }
}
