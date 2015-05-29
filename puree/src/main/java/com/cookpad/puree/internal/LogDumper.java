package com.cookpad.puree.internal;

import com.cookpad.puree.Key;
import com.cookpad.puree.PureeFilter;
import com.cookpad.puree.outputs.PureeOutput;
import com.cookpad.puree.storage.Records;

import org.json.JSONException;

import android.util.Log;

import java.util.List;
import java.util.Map;

public class LogDumper {
    private static final String TAG = LogDumper.class.getSimpleName();

    public static void out(Records records) {
        try {
            switch (records.size()) {
                case 0:
                    Log.d(TAG, "No records in Puree's buffer");
                    return;
                case 1:
                    Log.d(TAG, "1 record in Puree's buffer" + "\n"
                            + records.getJsonLogs().get(0));
                default:
                    StringBuilder builder = new StringBuilder();
                    builder.append(records.size() + " records in Puree's buffer\n");
                    for (int i = 0; i < records.size(); i++) {
                        builder.append(records.getJsonLogs().get(0)).append("\n");
                    }
                    Log.d(TAG, builder.substring(0, builder.length() - 1));
            }
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public static void out(Map<Key, List<PureeOutput>> sourceOutputMap) {
        Log.i(TAG, "# SOURCE -> FILTER... -> OUTPUT");
        for (Key key : sourceOutputMap.keySet()) {
            StringBuilder builder;
            for (PureeOutput output : sourceOutputMap.get(key)) {
                builder = new StringBuilder(key.getId());
                for (PureeFilter filter : output.getFilters()) {
                    builder.append(" -> ").append(filter.getClass().getSimpleName());
                }
                builder.append(" -> ").append(output.getClass().getSimpleName());
                Log.i(TAG, builder.toString());
            }
        }
    }
}
