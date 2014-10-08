package com.example.puree.logs;

import com.cookpad.android.puree.PureeBufferedOutput;
import com.cookpad.android.puree.async.AsyncResult;

import org.json.JSONObject;

import java.util.List;

public class OutAsyncSomething extends PureeBufferedOutput {
    private static final String TYPE = "async_something";

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Configuration configure(Configuration conf) {
        return conf;
    }

    @Override
    public void emit(List<JSONObject> serializedLogs, final AsyncResult result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                result.success();
            }
        });
    }
}
