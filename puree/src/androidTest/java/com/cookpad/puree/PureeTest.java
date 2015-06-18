package com.cookpad.puree;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.cookpad.puree.outputs.PureeOutput;
import com.cookpad.puree.storage.PureeStorage;
import com.cookpad.puree.storage.Records;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class PureeTest {

    static class DummyPureeStorage implements PureeStorage {

        @Override
        public void insert(String type, JsonObject jsonLog) {

        }

        @Override
        public Records select(String type, int logsPerRequest) {
            return new Records();
        }

        @Override
        public Records selectAll() {
            return new Records();
        }

        @Override
        public void delete(Records records) {

        }

        @Override
        public void clear() {

        }
    }

    static class DummyPureeLogger extends PureeLogger {

        public DummyPureeLogger() {
            super(new HashMap<Class<?>, List<PureeOutput>>(), new Gson(), new DummyPureeStorage());
        }


        @Override
        public void send(PureeLog log) {

        }
    }

    static class PvLog implements PureeLog {

        String name = "foo";
    }


    @Before
    public void setUp() throws Exception {
        Puree.setPureeLogger(new DummyPureeLogger());
    }

    @Test
    public void testSend() throws Exception {
        Puree.send(new PvLog());
    }

    @Test
    public void testFlush() throws Exception {
        Puree.flush();
    }

    @Test
    public void testDump() throws Exception {
        Puree.dump();
    }

    @Test
    public void testDiscardBufferedLogs() throws Exception {
        Puree.discardBufferedLogs();
    }
}
