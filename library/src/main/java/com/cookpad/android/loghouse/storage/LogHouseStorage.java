package com.cookpad.android.loghouse.storage;

import org.json.JSONObject;

public interface LogHouseStorage {
    public void insert(JSONObject serializedLog);
    public Records select(int logsPerRequest);
    public void delete(Records records);
}
