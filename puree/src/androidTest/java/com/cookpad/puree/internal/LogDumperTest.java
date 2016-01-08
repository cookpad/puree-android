package com.cookpad.puree.internal;

import com.cookpad.puree.outputs.PureeOutput;
import com.cookpad.puree.storage.Records;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.runner.AndroidJUnit4;

import java.util.HashMap;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class LogDumperTest {

    @Test
    public void testRunLogDumper() throws Exception {
        LogDumper.out(new Records());
        LogDumper.out(new HashMap<Class<?>, List<PureeOutput>>());
    }
}
