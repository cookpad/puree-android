package com.example.puree.logs;

import android.test.AndroidTestCase;

import com.cookpad.puree.LogSpec;
import com.cookpad.puree.plugins.OutLogcat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClickLogTest extends AndroidTestCase {
    public void testFormat() {
        new LogSpec(PureeConfigurator.buildConf(getContext()))
                .log(new ClickLog("MainActivity", "ClickLog1"), OutLogcat.TYPE)
                .log(new ClickLog("MainActivity", "ClickLog2"), OutLogcat.TYPE)
                .targetType(OutLogcat.TYPE)
                .shouldBe(new LogSpec.Matcher() {
                    @Override
                    public void expect(JSONArray serializedLogs) throws JSONException {
                        assertEquals(2, serializedLogs.length());
                        JSONObject serializedLog = serializedLogs.getJSONObject(0);
                        assertEquals("MainActivity", serializedLog.getString("page"));
                        assertEquals("ClickLog1", serializedLog.getString("label"));
                        assertTrue(serializedLog.has("event_time"));
                    }
                });
    }
}
