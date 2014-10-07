package com.example.loghouse.logs;

import android.test.AndroidTestCase;

import com.cookpad.android.loghouse.LogSpec;
import com.cookpad.android.loghouse.plugins.OutLogcat;
import com.example.loghouse.DemoApplication;
import com.example.loghouse.logs.plugins.OutBufferedDisplay;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PvLogTest extends AndroidTestCase {
    public void testFormat() {
        new LogSpec(DemoApplication.buildConfiguration(getContext()))
                .logs(new PvLog("MainActivity"),
                        new ClickLog("MainActivity", "PvLog1", OutBufferedDisplay.TYPE),
                        new PvLog("MainActivity"))
                .target(OutLogcat.TYPE)
                .shouldBe(new LogSpec.Matcher() {
                    @Override
                    public void expect(List<JSONObject> serializedLogs) throws JSONException {
                        assertEquals(2, serializedLogs.size());
                        JSONObject serializedLog = serializedLogs.get(0);
                        assertEquals("MainActivity", serializedLog.getString("screen_name"));
                        assertTrue(serializedLog.has("event_time"));
                    }
                });
    }
}
