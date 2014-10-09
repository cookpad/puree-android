package com.example.puree.logs;

import android.test.AndroidTestCase;

import com.cookpad.android.puree.LogSpec;
import com.example.puree.DemoApplication;
import com.example.puree.logs.ClickLog;
import com.example.puree.logs.PvLog;
import com.example.puree.logs.plugins.OutBufferedDisplay;

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

        new LogSpec(PureeConfigurator.buildConf(getContext()))
                .logs(new ClickLog("MainActivity", "ClickLog1", OutBufferedDisplay.TYPE),
                        new ClickLog("MainActivity", "ClickLog2", OutBufferedDisplay.TYPE),
                        new PvLog("MainActivity"))
                .target(OutBufferedDisplay.TYPE)
                .shouldBe(new LogSpec.Matcher() {
                    @Override
                    public void expect(List<JSONObject> serializedLogs) throws JSONException {
                        assertEquals(2, serializedLogs.size());
                        JSONObject serializedLog = serializedLogs.get(0);
                        assertEquals("MainActivity", serializedLog.getString("page"));
                        assertEquals("ClickLog1", serializedLog.getString("label"));
                        assertTrue(serializedLog.has("event_time"));
                    }
                });
    }
}
