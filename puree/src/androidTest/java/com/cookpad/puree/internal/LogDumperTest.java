package com.cookpad.puree.internal;

import com.cookpad.puree.outputs.PureeOutput;
import com.cookpad.puree.storage.Records;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class LogDumperTest {

    @Test
    public void testRunLogDumper() throws Exception {
        LogDumper.out(new Records());
        LogDumper.out(new HashMap<Class<?>, List<PureeOutput>>());
    }
}
