package com.example.puree;

import com.google.gson.JsonArray;

import android.util.Log;

public class FakeApiClient {
    private static final String TAG = FakeApiClient.class.getSimpleName();

    public interface Callback {
        public void success();
        public void fail();
    }

    public void sendLog(final JsonArray jsonLogs, final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Log.e(TAG, jsonLogs.toString());
                    callback.success();
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage(), e);
                    callback.fail();
                }
            }
        }).start();
    }
}
