package com.cookpad.android.loghouse.storage;

import org.json.JSONObject;

import java.util.List;

public interface LogHouseStorage {
    public void insert(JSONObject serializedLog);
    public List<JSONObject> select();
    public void delete();
}
