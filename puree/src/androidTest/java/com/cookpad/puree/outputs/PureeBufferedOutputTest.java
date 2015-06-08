package com.cookpad.puree.outputs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.cookpad.puree.JsonConvertible;
import com.cookpad.puree.Puree;
import com.cookpad.puree.PureeConfiguration;
import com.cookpad.puree.async.AsyncResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PureeBufferedOutputTest {

    ArrayList<String> logs = new ArrayList<>();

    BufferedOutput output;

    static class PvLog extends JsonConvertible {

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
        Puree.initialize(new PureeConfiguration.Builder(context)
                .register(PvLog.class, output)
                .build());
        Puree.clear();
    }

    @After
    public void tearDown() throws Exception {
        Puree.clear();
    }

    @Test
    public void testPureeBufferedOutput() throws Exception {
        Puree.send(new PvLog("foo"));
        Puree.send(new PvLog("bar"));
        Puree.send(new PvLog("baz"));

        final CountDownLatch latch = new CountDownLatch(1);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Puree.flush();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        }.execute();

        assertThat(latch.await(1, TimeUnit.SECONDS), is(true));

        assertThat(logs.size(), is(3));
        assertThat(logs.get(0), is("{\"name\":\"foo\"}"));
        assertThat(logs.get(1), is("{\"name\":\"bar\"}"));
        assertThat(logs.get(2), is("{\"name\":\"baz\"}"));
    }
}
