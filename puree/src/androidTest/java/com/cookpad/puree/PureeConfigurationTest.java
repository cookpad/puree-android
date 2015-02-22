package com.cookpad.puree;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cookpad.puree.outputs.OutputConfiguration;
import com.cookpad.puree.outputs.PureeOutput;

import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
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

    @Test
    public void build() {
        Context context = InstrumentationRegistry.getContext();
        PureeConfiguration conf = new PureeConfiguration.Builder(context)
                .source(FooLog.class).to(new OutFoo())
                .source(FooLog.class).filters(new FooFilter()).to(new OutFoo())
                .source(FooLog.class).filter(new FooFilter()).filter(new BarFilter()).to(new OutBar())
                .source(BarLog.class).filter(new FooFilter()).to(new OutFoo())
                .build();

        Map<Key, List<PureeOutput>> sourceOutputMap = conf.getSourceOutputMap();
        assertThat(sourceOutputMap.size(), is(2));

        {
            List<PureeOutput> outputs = sourceOutputMap.get(Key.from(FooLog.class));
            assertThat(outputs.size(), is(3));
        }
        {
            List<PureeOutput> outputs = sourceOutputMap.get(Key.from(BarLog.class));
            assertThat(outputs.size(), is(1));
            assertThat(outputs.get(0).getClass().getName(), is(OutFoo.class.getName()));
            List<PureeFilter> filters = outputs.get(0).getFilters();
            assertThat(filters.size(), is(1));
            assertThat(filters.get(0).getClass().getName(), is(FooFilter.class.getName()));
        }
    }

    private static class FooLog extends JsonConvertible {
    }

    private static class BarLog extends JsonConvertible {
    }

    private static class FooFilter implements PureeFilter {
        @Override
        public JSONObject apply(JSONObject jsonLog) throws JSONException {
            return null;
        }
    }

    private static class BarFilter implements PureeFilter {
        @Override
        public JSONObject apply(JSONObject jsonLog) throws JSONException {
            return null;
        }
    }

    private static class OutFoo extends PureeOutput {
        @Override
        public String type() {
            return null;
        }

        @Override
        public OutputConfiguration configure(OutputConfiguration conf) {
            return null;
        }

        @Override
        public void emit(JSONObject jsonLog) {

        }
    }

    private static class OutBar extends PureeOutput {
        @Override
        public String type() {
            return null;
        }

        @Override
        public OutputConfiguration configure(OutputConfiguration conf) {
            return null;
        }

        @Override
        public void emit(JSONObject jsonLog) {

        }
    }
}
