package com.cookpad.puree;

import android.content.Context;

import com.cookpad.puree.outputs.PureeOutput;
import com.cookpad.puree.storage.PureeSQLiteStorage;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class PureeTest {

    static class DummyPureeLogger extends PureeLogger {

        public DummyPureeLogger(Context context) {
            super(new HashMap<Class<?>, List<PureeOutput>>(), new Gson(), new PureeSQLiteStorage(context),
                    Executors.newScheduledThreadPool(1));
        }


        @Override
        public void send(PureeLog log) {

        }
    }

    static class PvLog implements PureeLog {

        @SuppressWarnings("unused")
        String name = "foo";
    }


    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
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

}
