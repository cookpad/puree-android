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
        if (conf.isTest()) {
            storage.clean();
        }

        for (Class<? extends LogHouseOutput> outputType : conf.getOutputTypes()) {
            try {
                LogHouseOutput output = outputType.newInstance();
                output.initialize(conf, storage);
                output.initialize(conf, storage);
                outputs.add(output);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Unable to create new instance: " + outputType.getSimpleName());
            }
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
}
