package com.cookpad.puree.outputs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.cookpad.puree.PureeConfiguration;
import com.cookpad.puree.PureeFilter;
import com.cookpad.puree.PureeLog;
import com.cookpad.puree.PureeLogger;
import com.cookpad.puree.async.AsyncResult;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class PureeBufferedOutputTest {

    Context context;

    BlockingQueue<String> logs = new ArrayBlockingQueue<>(3);

    PureeLogger logger;

    static class PvLog implements PureeLog {

        String name;

        public PvLog(String name) {
            this.name = name;
        }
    }

    @ParametersAreNonnullByDefault
    static abstract class BufferedOutputBase extends PureeBufferedOutput {

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

    @ParametersAreNonnullByDefault
    class BufferedOutput extends BufferedOutputBase {

        @Override
        public void emit(JsonArray jsonArray, AsyncResult result) {
            for (JsonElement item : jsonArray) {
                logs.add(item.toString());
            }
            result.success();
        }
    }

    @ParametersAreNonnullByDefault
    class TruncateBufferedOutput extends BufferedOutput {

        @Nonnull
        @Override
        public OutputConfiguration configure(OutputConfiguration conf) {
            conf.setFlushIntervalMillis(5000);
            return conf;
        }
    }

    @ParametersAreNonnullByDefault
    class DiscardedBufferedOutput extends BufferedOutputBase {

        @Override
        public void emit(JsonArray jsonArray, AsyncResult result) {
            throw new AssertionFailedError("not reached");
        }
    }

    @ParametersAreNonnullByDefault
    class BufferedOutputToTestFailFirst extends BufferedOutputBase {

        int counter = 0;

        @Override
        public void emit(JsonArray jsonArray, AsyncResult result) {
            if (counter++ == 0) {
                result.fail();
            } else {
                for (JsonElement item : jsonArray) {
                    logs.add(item.toString());
                }
                result.success();
            }
        }
    }


    @ParametersAreNonnullByDefault
    public static class DiscardFilter implements PureeFilter {

        @Nullable
        @Override
        public JsonObject apply(JsonObject jsonLog) {
            return null;
        }
    }

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
    }

    @After
    public void tearDown() throws Exception {
        if (logger != null) {
            logger.discardBufferedLogs();
        }
    }

    void initializeLogger(PureeOutput output) {
        logger = new PureeConfiguration.Builder(context)
                .register(PvLog.class, output)
                .build()
                .createPureeLogger();
        logger.discardBufferedLogs();
    }

    @Test
    public void testPureeBufferedOutput() throws Exception {
        initializeLogger(new BufferedOutput());

        logger.send(new PvLog("foo"));
        logger.send(new PvLog("bar"));
        logger.send(new PvLog("baz"));
        logger.flush();

        Thread.sleep(100);

        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"foo\"}"));
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"bar\"}"));
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"baz\"}"));
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is(nullValue()));
    }

    @Test
    public void testPureeBufferedOutputWithDiscardFilter() throws Exception {
        initializeLogger(new DiscardedBufferedOutput().withFilters(new DiscardFilter()));

        logger.send(new PvLog("foo"));
        logger.send(new PvLog("bar"));
        logger.send(new PvLog("baz"));
        logger.flush();

        Thread.sleep(100);

        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is(nullValue()));
    }

    @Test
    public void testBufferedOutputToTestFailFirst() throws Exception {
        BufferedOutputToTestFailFirst output = new BufferedOutputToTestFailFirst();
        initializeLogger(output);

        logger.send(new PvLog("foo"));
        logger.send(new PvLog("bar"));
        logger.send(new PvLog("baz"));
        logger.flush();

        Thread.sleep(100);

        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"foo\"}"));
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"bar\"}"));
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"baz\"}"));
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is(nullValue()));
    }

    @Test
    public void testTrancateBufferedLogs() throws Exception {
        initializeLogger(new TruncateBufferedOutput());

        logger.send(new PvLog("foo"));
        logger.send(new PvLog("bar"));
        logger.send(new PvLog("baz"));

        Thread.sleep(100);

        logger.truncateBufferedLogs(2);

        logger.flush();

        Thread.sleep(100);
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"bar\"}"));
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"baz\"}"));
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is(nullValue()));
    }
}
