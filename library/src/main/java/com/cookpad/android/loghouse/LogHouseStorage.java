package com.cookpad.android.loghouse;

public interface LogHouseStorage {
    public void insert(String serializedLog);
    public void select();
    public void delete();
}
