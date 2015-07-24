package com.cookpad.puree;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.cookpad.puree.outputs.PureeOutput;
import com.cookpad.puree.storage.PureeSQLiteStorage;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class PureeTest {

    static class DummyPureeLogger extends PureeLogger {

        public DummyPureeLogger(Context context) {
            super(new HashMap<Class<?>, List<PureeOutput>>(), new Gson(), new PureeSQLiteStorage(context), 1000);
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
        Context context = InstrumentationRegistry.getTargetContext();
        Puree.setPureeLogger(new DummyPureeLogger(context));
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

    @Test
    public void testDiscardOldBufferedLogs() throws Exception {
        Puree.discardOldBufferedLogs();
    }
}
