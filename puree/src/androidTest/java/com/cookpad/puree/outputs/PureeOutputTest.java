package com.cookpad.puree.outputs;

import com.google.gson.JsonObject;

import com.cookpad.puree.PureeFilter;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nonnull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PureeOutputTest {
    @Test
    public void discardLog() {
        final PureeOutput output = new PureeOutput() {
            @Override
            public void emit(JsonObject jsonLog) {
                // because first filter discards log.
                fail("log should be discarded by first filter");
            }

            @Nonnull
            @Override
            public OutputConfiguration configure(OutputConfiguration conf) {
                return conf;
            }

            @Nonnull
            @Override
            public String type() {
                return "output";
            }
        };
        output.registerFilter(new PureeFilter() {
            @Override
            public JsonObject apply(JsonObject jsonLog) {
                // discard log
                return null;
            }
        });
        output.registerFilter(new PureeFilter() {
            @Override
            public JsonObject apply(JsonObject jsonLog) {
                jsonLog.addProperty("event_time", System.currentTimeMillis());
                return jsonLog;
            }
        });

        output.receive(new JsonObject());
    }

    @Test
    public void testFilter_multipleModifications() throws JSONException {

        final AtomicReference<JsonObject> result = new AtomicReference<>();

        final PureeOutput output = new PureeOutput() {
            @Override
            public void emit(JsonObject jsonLog) {
                result.set(jsonLog);
            }

            @Nonnull
            @Override
            public OutputConfiguration configure(OutputConfiguration conf) {
                return conf;
            }

            @Nonnull
            @Override
            public String type() {
                return "output";
            }
        };
        output.registerFilter(new PureeFilter() {
            @Override
            public JsonObject apply(JsonObject jsonLog) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("filter1", "foo");
                return jsonObject;
            }
        });
        output.registerFilter(new PureeFilter() {
            @Override
            public JsonObject apply(JsonObject jsonLog) {
                final JsonObject jsonObject = new JsonObject();
                if (jsonLog.has("filter1")) {
                    jsonObject.addProperty("filter1", jsonLog.get("filter1").getAsString());
                }
                jsonLog.addProperty("filter2", "bar");
                return jsonLog;
            }
        });

        output.receive(new JsonObject());

        final JsonObject resultObject = result.get();
        assertThat(resultObject.has("filter1"), is(true));
        assertThat(resultObject.get("filter1").getAsString(), is("foo"));
        assertThat(resultObject.has("filter2"), is(true));
        assertThat(resultObject.get("filter2").getAsString(), is("bar"));
    }
}
