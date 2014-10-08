package com.cookpad.android.puree.storage;

import org.json.JSONObject;

public interface PureeStorage {
    public void insert(String type, JSONObject serializedLog);
    public Records select(String type, int logsPerRequest);
    public Records selectAll();
    public void delete(Records records);
    public void clear();
}
