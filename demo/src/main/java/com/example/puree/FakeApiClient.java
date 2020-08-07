package com.example.puree;

import com.google.gson.JsonArray;

import android.util.Log;

import java.util.List;

public class FakeApiClient {
    private static final String TAG = FakeApiClient.class.getSimpleName();

    public interface Callback {

        void success();

        void fail();
    }

    public void sendLog(final List<String> jsonLogs, final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);

                    JsonArray jsonLogsArray = new JsonArray();
                    for (String jsonLog: jsonLogs) {
                        jsonLogsArray.add(jsonLog);
                    }

                    Log.e(TAG, jsonLogsArray.toString());

                    callback.success();
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage(), e);
                    callback.fail();
                }
            }
        }).start();
    }
}
