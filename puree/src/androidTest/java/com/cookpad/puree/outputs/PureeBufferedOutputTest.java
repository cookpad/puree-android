package com.cookpad.puree.outputs;

import com.cookpad.puree.PureeConfiguration;
import com.cookpad.puree.PureeFilter;
import com.cookpad.puree.PureeLogger;
import com.cookpad.puree.PureeSerializer;
import com.cookpad.puree.async.AsyncResult;
import com.cookpad.puree.storage.PureeSQLiteStorage;
import com.cookpad.puree.storage.PureeStorage;
import com.cookpad.puree.storage.Record;
import com.cookpad.puree.storage.Records;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PureeBufferedOutputTest {
    private PureeSerializer pureeSerializer = new PureeSerializer() {
        @Override
        public String serialize(Object log) {
            PvLog pvLog = (PvLog) log;
            return "{\"name\":\""+pvLog.name+"\"}";
        }
    };

    Context context;

    BlockingQueue<String> logs = new ArrayBlockingQueue<>(3);

    PureeLogger logger;

    static class PvLog {

        String name;

        public PvLog(String name) {
            this.name = name;
        }
    }

    static class InMemoryPureeStorage implements PureeStorage {
        private static class Entry {
            int id;
            String type;
            String jsonLog;

            Entry(int id, String type, String jsonLog) {
                this.id = id;
                this.type = type;
                this.jsonLog = jsonLog;
            }
        }

        private int id = 0;
        private List<Entry> entries = new ArrayList<>();

        @Override
        public void insert(String type, String jsonLog) {
            entries.add(new Entry(id++, type, jsonLog));
        }

        @Override
        public Records select(String type, int logsPerRequest) {
            Records records = new Records();
            int read = 0;
            while (read < entries.size() && records.size() < logsPerRequest) {
                Entry entry = entries.get(read);
                records.add(new Record(entry.id, entry.type, entry.jsonLog));
                read++;
            }

            return records;
        }

        @Override
        public Records selectAll() {
            Records records = new Records();
            for (Entry entry : entries) {
                records.add(new Record(entry.id, entry.type, entry.jsonLog));
            }
            return records;
        }

        @Override
        public void delete(Records records) {
            Set<Integer> ids = new HashSet<>();
            for (Record record : records) {
                ids.add(record.getId());
            }
            Iterator<Entry> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Entry entry = iterator.next();
                if (ids.contains(entry.id)) {
                    iterator.remove();
                }
            }
        }

        @Override
        public void truncateBufferedLogs(int maxRecords) {
            Iterator<Entry> iterator = entries.iterator();
            while (iterator.hasNext() && entries.size() > maxRecords) {
                iterator.remove();
            }
        }

        @Override
        public void clear() {
            entries.clear();
        }

        @Override
        public boolean lock() {
            return true;
        }

        @Override
        public void unlock() {

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
        public void emit(List<String> jsonLogs, AsyncResult result) {
            logs.addAll(jsonLogs);
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
        public void emit(List<String> jsonLogs, final AsyncResult result) {
            for (final String item : jsonLogs) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        logs.add(item);
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
        public void emit(List<String> jsonLogs, AsyncResult result) {
            throw new AssertionFailedError("not reached");
        }
    }

    @ParametersAreNonnullByDefault
    class BufferedOutputToTestFailFirst extends BufferedOutputBase {

        int counter = 0;

        @Override
        public void emit(List<String> jsonLogs, AsyncResult result) {
            if (counter++ == 0) {
                result.fail();
            } else {
                logs.addAll(jsonLogs);
                result.success();
            }
        }
    }


    @ParametersAreNonnullByDefault
    public static class DiscardFilter implements PureeFilter {

        @Nullable
        @Override
        public String apply(String jsonLog) {
            return null;
        }
    }

    class BufferedOutputPurge extends BufferedOutput{
        private final long purgeAgeMillis;

        public BufferedOutputPurge(long purgeAgeMillis) {
            this.purgeAgeMillis = purgeAgeMillis;
        }

        @Nonnull
        @Override
        public OutputConfiguration configure(OutputConfiguration conf) {
            conf.setPurgeAgeMillis(purgeAgeMillis);
            return conf;
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
                .pureeSerializer(pureeSerializer)
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
    public void testReverseOrderOption() throws Exception {
        logger = new PureeConfiguration.Builder(context)
                .register(PvLog.class, new BufferedOutput())
                .pureeSerializer(pureeSerializer)
                .storage(new PureeSQLiteStorage(context, true))
                .build()
                .createPureeLogger();
        logger.discardBufferedLogs();

        logger.send(new PvLog("foo"));
        logger.send(new PvLog("bar"));
        logger.send(new PvLog("baz"));
        logger.flush();

        Thread.sleep(100);

        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"baz\"}"));
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"bar\"}"));
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"foo\"}"));
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
                .pureeSerializer(pureeSerializer)
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
                .pureeSerializer(pureeSerializer)
                .register(PvLog.class, output)
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
                .pureeSerializer(pureeSerializer)
                .build()
                .createPureeLogger();

        logger.flush();

        Thread.sleep(100);

        assertThat(logs.size(), is(0));

        logger.send(new PvLog("foo"));
        logger.send(new PvLog("bar"));
        logger.send(new PvLog("baz"));

        logger.flush();

        Thread.sleep(1000);

        assertThat(logs.size(), is(3));

    }

    @Test
    public void testPureeBufferedOutput_purge() throws Exception {
        initializeLogger(new BufferedOutputPurge(1000));

        logger.send(new PvLog("foo"));
        Thread.sleep(1000);
        logger.send(new PvLog("bar"));
        Thread.sleep(900);
        logger.send(new PvLog("baz"));

        logger.flush();

        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"bar\"}"));
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"baz\"}"));
    }

    @Test
    public void testPureeBufferedOutput_purgeInvalidAge() throws Exception {
        initializeLogger(new BufferedOutputPurge(-1));

        logger.send(new PvLog("foo"));
        Thread.sleep(1000);
        logger.send(new PvLog("bar"));
        Thread.sleep(900);
        logger.send(new PvLog("baz"));

        logger.flush();

        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"foo\"}"));
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"bar\"}"));
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"baz\"}"));
    }

    @Test
    public void testPureeBufferedOutput_purgeNotSupported() throws Exception {
        logger = new PureeConfiguration.Builder(context)
                .register(PvLog.class, new BufferedOutputPurge(1000))
                .pureeSerializer(pureeSerializer)
                .storage(new InMemoryPureeStorage())
                .build()
                .createPureeLogger();
        logger.discardBufferedLogs();

        logger.send(new PvLog("foo"));
        Thread.sleep(1000);
        logger.send(new PvLog("bar"));
        Thread.sleep(900);
        logger.send(new PvLog("baz"));

        logger.flush();

        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"foo\"}"));
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"bar\"}"));
        assertThat(logs.poll(100, TimeUnit.MILLISECONDS), is("{\"name\":\"baz\"}"));
    }
}
