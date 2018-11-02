package com.cookpad.puree.outputs;

import android.content.Context;

import com.cookpad.puree.PureeConfiguration;
import com.cookpad.puree.PureeFilter;
import com.cookpad.puree.PureeLog;
import com.cookpad.puree.PureeLogger;
import com.cookpad.puree.async.AsyncResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
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

    static class FooLog implements PureeLog {

        String name;

        public FooLog(String name) {
            this.name = name;
        }
    }

    static class BarLog implements PureeLog {

        String name;

        public BarLog(String name) {
            this.name = name;
        }
    }

    static class BazLog implements PureeLog {

        String name;

        public BazLog(String name) {
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
            conf.setFlushIntervalMillis(10);
            return conf;
        }
    }

    @ParametersAreNonnullByDefault
    class BufferedOutput extends BufferedOutputBase {

        int flushCount = 0;

        @Override
        public void emit(JsonArray jsonArray, AsyncResult result) {
            for (JsonElement item : jsonArray) {
                logs.add(item.toString());
            }
            result.success();
        }

        @Override
        public void flush() {
            flushCount++;
            super.flush();
        }
    }

    @ParametersAreNonnullByDefault
    class BufferedOutputAsync extends BufferedOutput {

        int flushCount = 0;

        @Override
        public void emit(JsonArray jsonArray, final AsyncResult result) {
            for (final JsonElement item : jsonArray) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        logs.add(item.toString());
                        result.success();
                    }
                }).start();
            }
        }

        @Override
        public void flush() {
            flushCount++;
            super.flush();
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
        context = ApplicationProvider.getApplicationContext();
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

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"foo\"}"));
                    assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"bar\"}"));
                    assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"baz\"}"));
                    assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is(nullValue()));
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    fail();
                    countDownLatch.countDown();
                }
            }
        }).start();
        countDownLatch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testTruncateBufferedLogs() throws Exception {
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

    @Test
    public void testPureeBufferedOutput_countFlush() throws Exception {
        BufferedOutput output = new BufferedOutput();

        logger = new PureeConfiguration.Builder(context)
                .register(PvLog.class, output)
                .register(FooLog.class, output)
                .register(BarLog.class, output)
                .register(BazLog.class, output)
                .build()
                .createPureeLogger();

        logger.send(new PvLog("foo"));
        logger.send(new PvLog("bar"));
        logger.send(new PvLog("baz"));

        logger.flush();

        Thread.sleep(100);

        assertThat(output.flushCount, is(lessThanOrEqualTo(2)));
    }

    @Test
    public void testPureeBufferedOutput_countEmit() throws Exception {
        BufferedOutput output = new BufferedOutput();

        logger = new PureeConfiguration.Builder(context)
                .register(PvLog.class, output)
                .register(FooLog.class, output)
                .register(BarLog.class, output)
                .register(BazLog.class, output)
                .build()
                .createPureeLogger();

        logger.send(new PvLog("foo"));
        logger.send(new PvLog("bar"));
        logger.send(new PvLog("baz"));

        logger.flush();

        Thread.sleep(100);

        assertThat(logs.size(), is(lessThanOrEqualTo(3)));

    }

    @Test
    public void testPureeBufferedOutput_countEmitAsync() throws Exception {
        BufferedOutputAsync output = new BufferedOutputAsync();

        logger = new PureeConfiguration.Builder(context)
                .register(PvLog.class, output)
                .register(FooLog.class, output)
                .register(BarLog.class, output)
                .register(BazLog.class, output)
                .build()
                .createPureeLogger();

        logger.flush();

        Thread.sleep(100);

        assertThat(logs.size(), is(0));

        logger.send(new FooLog("foo"));
        logger.send(new BarLog("bar"));
        logger.send(new BazLog("baz"));

        logger.flush();

        Thread.sleep(1000);

        assertThat(logs.size(), is(3));

    }

}
