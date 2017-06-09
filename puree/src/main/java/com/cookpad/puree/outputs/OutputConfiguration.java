package com.cookpad.puree.outputs;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class OutputConfiguration {
    private long initialDelayMills = 2 * 60 * 1000; // 2 minutes;
    private int flushIntervalMillis = 2 * 60 * 1000; // 2 minutes
    private int logsPerRequest = 100;
    private int maxRetryCount = 5;

    OutputConfiguration() {
    }

    public long getInitialDelayMillis() {
        return initialDelayMills;
    }

    public void setInitialDelayMills(long initialDelayMills) {
        this.initialDelayMills = initialDelayMills;
    }

    public int getFlushIntervalMillis() {
        return flushIntervalMillis;
    }

    public void setFlushIntervalMillis(int flushIntervalMillis) {
        this.flushIntervalMillis = flushIntervalMillis;
    }

    public int getLogsPerRequest() {
        return logsPerRequest;
    }

    public void setLogsPerRequest(int logsPerRequest) {
        this.logsPerRequest = logsPerRequest;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }
}
