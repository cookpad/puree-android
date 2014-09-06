package com.example.loghouse.logs;

import android.test.AndroidTestCase;

import com.cookpad.android.loghouse.LogSpec;
import com.cookpad.android.loghouse.plugins.OutBufferedLogcat;
import com.cookpad.android.loghouse.plugins.OutLogcat;
import com.example.loghouse.DemoApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ClickLogTest extends AndroidTestCase {
    public void testCheckFormat() {
        new LogSpec(DemoApplication.buildConfiguration(getContext()))
                .logs(new ClickLog("MainActivity", "Hello", OutBufferedLogcat.TYPE),
                        new ClickLog("MainActivity", "World", OutBufferedLogcat.TYPE),
                        new ClickLog("MainActivity", "World", OutLogcat.TYPE))
                .target(OutBufferedLogcat.TYPE)
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
