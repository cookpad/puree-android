package com.cookpad.puree.internal;

import org.junit.Before;
import org.junit.Test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ProcessNameTest {

    Context context;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void testGetAndroidProcessName() throws Exception {
        assertThat(ProcessName.getAndroidProcessName(context), is(""));
    }

    @Test
    public void testFindProcessNameInLinuxWay() throws Exception {
        assertThat(ProcessName.findProcessNameInLinuxWay(), is("com.cookpad.android.puree.test"));
    }

    @Test
    public void testFindProcessNameInAndroidWay() throws Exception {
        assertThat(ProcessName.findProcessNameInAndroidWay(context), is("com.cookpad.android.puree.test"));
    }

    @Test
    public void testExtractAndroidProcessName() throws Exception {
        assertThat(ProcessName.extractAndroidProcessName("foo:bar"), is("bar"));
        assertThat(ProcessName.extractAndroidProcessName("foo.bar"), is(""));
    }
}
