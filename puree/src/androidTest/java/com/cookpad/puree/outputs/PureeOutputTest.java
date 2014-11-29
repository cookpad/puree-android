package com.cookpad.puree.outputs;

import android.test.AndroidTestCase;

import com.cookpad.puree.PureeFilter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicReference;

public class PureeOutputTest extends AndroidTestCase {

    public void testFilter_discardLog() {
        final PureeOutput output = new PureeOutput() {
            @Override
            public void emit(JSONObject jsonLog) {
                // because first filter discards log.
                fail("log should be discarded by first filter");
            }

            @Override
            public OutputConfiguration configure(OutputConfiguration conf) {
                return conf;
            }

            @Override
            public String type() {
                return "output";
            }
        };
        output.registerFilter(new PureeFilter() {
            @Override
            public JSONObject apply(JSONObject jsonLog) throws JSONException {
                // discard log
                return null;
            }
        });
        output.registerFilter(new PureeFilter() {
            @Override
            public JSONObject apply(JSONObject jsonLog) throws JSONException {
                return jsonLog.put("event_time", System.currentTimeMillis());
            }
        });

        output.receive(new JSONObject());
    }

    public void testFilter_multipleModifications() throws JSONException {

        final AtomicReference<JSONObject> result = new AtomicReference<>();

        final PureeOutput output = new PureeOutput() {
            @Override
            public void emit(JSONObject jsonLog) {
                result.set(jsonLog);
            }

            @Override
            public OutputConfiguration configure(OutputConfiguration conf) {
                return conf;
            }

            @Override
            public String type() {
                return "output";
            }
        };
        output.registerFilter(new PureeFilter() {
            @Override
            public JSONObject apply(JSONObject jsonLog) throws JSONException {
                final JSONObject jsonObject = new JSONObject();
                return jsonObject.put("filter1", "foo");
            }
        });
        output.registerFilter(new PureeFilter() {
            @Override
            public JSONObject apply(JSONObject jsonLog) throws JSONException {
                final JSONObject jsonObject = new JSONObject();
                if (jsonLog.has("filter1")) {
                    jsonObject.put("filter1", jsonLog.getString("filter1"));
                }
                return jsonLog.put("filter2", "bar");
            }
        });

        output.receive(new JSONObject());

        final JSONObject resultObject = result.get();
        assertTrue("value from \"filter1\" not found", resultObject.has("filter1"));
        assertEquals("", "foo", resultObject.getString("filter1"));
        assertTrue("value from \"filter2\" not found", resultObject.has("filter2"));
        assertEquals("bar", resultObject.getString("filter2"));
    }
}
