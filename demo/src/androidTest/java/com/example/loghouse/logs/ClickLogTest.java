package com.example.loghouse.logs;

import android.test.AndroidTestCase;

import com.cookpad.android.loghouse.LogSpec;
import com.cookpad.android.loghouse.plugins.OutBufferedLogcat;
import com.example.loghouse.DemoApplication;
import com.example.loghouse.logs.plugins.OutBufferedDisplay;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ClickLogTest extends AndroidTestCase {
    public void testFormat() {
        OutBufferedDisplay.register(new OutBufferedDisplay.Callback() {
            @Override
            public void onEmit(List<JSONObject> serializedLogs) {
                // do nothing
            }
        });

        new LogSpec(DemoApplication.buildConfiguration(getContext()))
                .logs(new ClickLog("MainActivity", "Hello", OutBufferedDisplay.TYPE),
                        new ClickLog("MainActivity", "World", OutBufferedDisplay.TYPE),
                        new PvLog("MainActivity"))
                .target(OutBufferedDisplay.TYPE)
                .shouldBe(new LogSpec.Matcher() {
                    @Override
                    public void expect(List<JSONObject> serializedLogs) throws JSONException {
                        assertEquals(2, serializedLogs.size());
                        JSONObject serializedLog = serializedLogs.get(0);
                        assertEquals("MainActivity", serializedLog.getString("page"));
                        assertEquals("Hello", serializedLog.getString("label"));
                        assertTrue(serializedLog.has("event_time"));
                    }
                });
    }
}
