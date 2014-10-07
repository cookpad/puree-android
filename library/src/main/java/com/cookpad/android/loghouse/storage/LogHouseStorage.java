package com.cookpad.android.loghouse.storage;

import org.json.JSONObject;

public interface LogHouseStorage {
    public void insert(String type, JSONObject serializedLog);
    public Records select(String type, int logsPerRequest);
    public void delete(Records records);
    public void clear();
    public void dump();
}
