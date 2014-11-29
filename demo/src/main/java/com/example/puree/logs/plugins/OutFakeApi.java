package com.example.puree.logs.plugins;

import com.cookpad.puree.async.AsyncResult;
import com.cookpad.puree.outputs.OutputConfiguration;
import com.cookpad.puree.outputs.PureeBufferedOutput;
import com.example.puree.FakeApiClient;

import org.json.JSONArray;

public class OutFakeApi extends PureeBufferedOutput {
    public static final String TYPE = "fake_api";

    private static final FakeApiClient CLIENT = new FakeApiClient();

    @Override
    public void emit(JSONArray jsonArray, final AsyncResult result) {
        CLIENT.sendLog(jsonArray, new FakeApiClient.Callback() {
            @Override
            public void success() {
                result.success();
            }

            @Override
            public void fail() {
                result.fail();
            }
        });
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public OutputConfiguration configure(OutputConfiguration conf) {
        return conf;
    }
}
