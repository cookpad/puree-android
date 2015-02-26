package com.cookpad.puree.outputs;

import android.support.test.runner.AndroidJUnit4;

import com.cookpad.puree.PureeFilter;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class PureeOutputTest {
    @Test
    public void discardLog() {
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

    @Test
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
        assertThat(resultObject.has("filter1"), is(true));
        assertThat(resultObject.getString("filter1"), is("foo"));
        assertThat(resultObject.has("filter2"), is(true));
        assertThat(resultObject.getString("filter2"), is("bar"));
    }
}
