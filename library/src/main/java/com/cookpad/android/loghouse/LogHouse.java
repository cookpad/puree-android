package com.cookpad.android.loghouse;

import com.cookpad.android.loghouse.storage.LogHouseDbHelper;
import com.cookpad.android.loghouse.storage.LogHouseStorage;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LogHouse {
    private static Gson gson;
    private static LogHouseStorage storage;
    private static List<LogHouseOutput> outputs = new ArrayList<>();

    public static void initialize(LogHouseConfiguration conf) {
        gson = conf.getGson();

        storage = new LogHouseDbHelper(conf.getApplicationContext());

        for (LogHouseOutput output : conf.getOutputs()) {
            output.initialize(conf, storage);
            output.initialize(conf, storage);
            outputs.add(output);
        }
    }

    public static void in(SerializableLog log) {
        in(log.type(), log.toJSON(gson));
    }

    private static void in(String type, JSONObject serializedLog) {
        for (LogHouseOutput output : outputs) {
            if (output.type().equals(type)) {
                output.start(serializedLog);
            }
        }
    }

    public static void dump() {
        storage.dump();
    }

    public static void clear() {
        storage.clear();
    }
}
