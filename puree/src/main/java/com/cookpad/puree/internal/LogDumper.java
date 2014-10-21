package com.cookpad.puree.internal;

import android.util.Log;

import com.cookpad.puree.storage.Records;

import org.json.JSONException;

public class LogDumper {
    private static final String TAG = LogDumper.class.getSimpleName();

    public static void outLogcat(Records records) {
        Log.d(TAG, buildMessage(records));
    }

    public static String buildMessage(Records records) {
        try {
            switch (records.size()) {
                case 0:
                    return "No records in Puree's buffer";
                case 1:
                    return "1 record in Puree's buffer" + "\n"
                            + records.getSerializedLogs().get(0);
                default:
                    StringBuilder builder = new StringBuilder();
                    builder.append(records.size() + " records in Puree's buffer\n");
                    for (int i = 0; i < records.size(); i++) {
                        builder.append(records.getSerializedLogs().get(0)).append("\n");
                    }
                    return builder.substring(0, builder.length() - 1);
            }
        } catch (JSONException e) {
            return e.getMessage();
        }
    }
}
