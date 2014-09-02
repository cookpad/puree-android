package com.example.loghouse.logs;

import android.test.AndroidTestCase;

import com.cookpad.android.loghouse.test.LogSpec;
import com.example.loghouse.AddRequiredParamsAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ClickLogTest extends AndroidTestCase {
    public void testCheckFormat() {
        new LogSpec(getContext())
                .action(new AddRequiredParamsAction())
                .logs(new ClickLog("MainActivity", "Hello"), new ClickLog("MainActivity", "World"))
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
