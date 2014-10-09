package com.cookpad.android.puree;

public class OutputConfiguration {
    private int flushInterval = 2 * 60 * 1000;
    private int logsPerRequest = 100;

    public int getFlushInterval() {
        return flushInterval;
    }

    public void setFlushInterval(int flushInterval) {
        this.flushInterval = flushInterval;
    }

    public int getLogsPerRequest() {
        return logsPerRequest;
    }

    public void setLogsPerRequest(int logsPerRequest) {
        this.logsPerRequest = logsPerRequest;
    }
}
