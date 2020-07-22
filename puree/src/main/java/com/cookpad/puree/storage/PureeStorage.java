package com.cookpad.puree.storage;

public interface PureeStorage {

    public void insert(String type, String jsonLog);
    public Records select(String type, int logsPerRequest);
    public Records selectAll();
    public void delete(Records records);
    public void truncateBufferedLogs(int maxRecords);
    public void clear();
    public boolean lock();
    public void unlock();
}
