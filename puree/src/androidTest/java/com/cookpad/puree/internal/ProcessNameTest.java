package com.cookpad.puree.internal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assume.assumeTrue;

@RunWith(AndroidJUnit4.class)
public class ProcessNameTest {

    boolean runOnAndroid() {
        return System.getProperty("java.vm.name").equals("Dalvik");
    }

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
        assumeTrue(runOnAndroid());

        assertThat(ProcessName.findProcessNameInLinuxWay(), is("com.cookpad.android.puree.test"));
    }

    @Test
    public void testFindProcessNameInAndroidWay() throws Exception {
        assumeTrue(runOnAndroid());

        assertThat(ProcessName.findProcessNameInAndroidWay(context), is("com.cookpad.android.puree.test"));
    }

    @Test
    public void testExtractAndroidProcessName() throws Exception {
        assertThat(ProcessName.extractAndroidProcessName("foo:bar"), is("bar"));
        assertThat(ProcessName.extractAndroidProcessName("foo.bar"), is(""));
    }
}
