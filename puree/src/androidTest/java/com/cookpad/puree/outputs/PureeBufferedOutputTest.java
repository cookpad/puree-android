package com.cookpad.puree.outputs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.cookpad.puree.PureeConfiguration;
import com.cookpad.puree.PureeLog;
import com.cookpad.puree.PureeLogger;
import com.cookpad.puree.async.AsyncResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PureeBufferedOutputTest {

    ArrayList<String> logs = new ArrayList<>();

    BufferedOutput output;

    PureeLogger logger;

    static class PvLog implements PureeLog {

        String name;

        public PvLog(String name) {
            this.name = name;
        }
    }

    @ParametersAreNonnullByDefault
    class BufferedOutput extends PureeBufferedOutput {

        public BufferedOutput(Handler handler) {
            super(handler);
        }

        @Override
        public void emit(JsonArray jsonArray, AsyncResult result) {
            for (JsonElement item : jsonArray) {
                logs.add(item.toString());
            }
            result.success();
        }

        @Nonnull
        @Override
        public String type() {
            return "buffered_output";
        }

        @Nonnull
        @Override
        public OutputConfiguration configure(OutputConfiguration conf) {
            conf.setFlushIntervalMillis(1);
            return conf;
        }
    }

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        Handler handler = new Handler(Looper.getMainLooper());

        output = new BufferedOutput(handler);

        logger = new PureeConfiguration.Builder(context)
                .register(PvLog.class, output)
                .build()
                .createPureeLogger();
        logger.discardBufferedLogs();
    }

    @After
    public void tearDown() throws Exception {
        logger.discardBufferedLogs();
    }

    @Test
    public void testPureeBufferedOutput() throws Exception {
        logger.send(new PvLog("foo"));
        logger.send(new PvLog("bar"));
        logger.send(new PvLog("baz"));
        logger.flush();

        Thread.sleep(1000);

        assertThat(logs.size(), is(3));
        assertThat(logs.get(0), is("{\"name\":\"foo\"}"));
        assertThat(logs.get(1), is("{\"name\":\"bar\"}"));
        assertThat(logs.get(2), is("{\"name\":\"baz\"}"));
    }
}
