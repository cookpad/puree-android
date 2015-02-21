package com.cookpad.puree;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cookpad.puree.outputs.PureeOutput;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class PureeConfigurationTest {
    @Test
    public void checkDefaultValue() {
        Context context = InstrumentationRegistry.getContext();
        PureeConfiguration conf = new PureeConfiguration.Builder(context)
                .build();

        assertThat(conf.getApplicationContext(), notNullValue());
        assertThat(conf.getGson(), notNullValue());

        Map<Key, List<PureeOutput>> sourceOutputMap = conf.getSourceOutputMap();
        assertThat(sourceOutputMap.size(), is(0));
    }
}
