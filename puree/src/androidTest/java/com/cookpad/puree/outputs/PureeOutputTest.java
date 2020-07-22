package com.cookpad.puree.outputs;

import com.cookpad.puree.PureeFilter;

import org.json.JSONException;
import org.json.JSONObject;
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
            public void emit(String jsonLog) {
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
            public String apply(String jsonLog) {
                // discard log
                return null;
            }
        });
        output.registerFilter(new PureeFilter() {
            @Override
            public String apply(String jsonLog) {
                return jsonLog + "event_time" + System.currentTimeMillis();
            }
        });

        output.receive("");
    }

    @Test
    public void testFilter_multipleModifications() throws JSONException {

        final AtomicReference<String> result = new AtomicReference<>();

        final PureeOutput output = new PureeOutput() {
            @Override
            public void emit(String jsonLog) {
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
            public String apply(String jsonLog) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("filter1", "foo");
                    return jsonObject.toString();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        output.registerFilter(new PureeFilter() {
            @Override
            public String apply(String jsonLog) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonLog);
                    if (jsonObject.has("filter1")) {
                        jsonObject.put("filter1", jsonObject.getString("filter1"));
                    }
                    jsonObject.put("filter2", "bar");
                    return jsonObject.toString();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        output.receive("");

        final JSONObject resultObject = new JSONObject(result.get());
        assertThat(resultObject.has("filter1"), is(true));
        assertThat(resultObject.getString("filter1"), is("foo"));
        assertThat(resultObject.has("filter2"), is(true));
        assertThat(resultObject.getString("filter2"), is("bar"));
    }
}
