package com.cookpad.puree;

import com.google.gson.JsonObject;

import com.cookpad.puree.outputs.OutputConfiguration;
import com.cookpad.puree.outputs.PureeOutput;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@ParametersAreNonnullByDefault
@RunWith(AndroidJUnit4.class)
public class PureeConfigurationTest {
    @Test
    public void checkDefaultValue() {
        Context context = ApplicationProvider.getApplicationContext();
        PureeConfiguration conf = new PureeConfiguration.Builder(context)
                .build();

        assertThat(conf.getContext(), notNullValue());
        assertThat(conf.getGson(), notNullValue());

        Map<?, ?> sourceOutputMap = conf.getSourceOutputMap();
        assertThat(sourceOutputMap.size(), is(0));
    }

    @Test
    public void build() {
        Context context = ApplicationProvider.getApplicationContext();
        PureeConfiguration conf = new PureeConfiguration.Builder(context)
                .source(FooLog.class).to(new OutFoo())
                .source(FooLog.class).filters(new FooFilter()).to(new OutFoo())
                .source(FooLog.class).filter(new FooFilter()).filter(new BarFilter()).to(new OutBar())
                .source(BarLog.class).filter(new FooFilter()).to(new OutFoo())
                .build();

        {
            List<PureeOutput> outputs = conf.getRegisteredOutputPlugins(FooLog.class);
            assertThat(outputs, is(not(nullValue())));
            assertThat(outputs.size(), is(3));
        }
        {
            List<PureeOutput> outputs = conf.getRegisteredOutputPlugins(BarLog.class);
            assertThat(outputs.size(), is(1));
            assertThat(outputs.get(0).getClass().getName(), is(OutFoo.class.getName()));
            List<PureeFilter> filters = outputs.get(0).getFilters();
            assertThat(filters.size(), is(1));
            assertThat(filters.get(0).getClass().getName(), is(FooFilter.class.getName()));
        }
    }

    @Test
    public void build2() {
        Context context = ApplicationProvider.getApplicationContext();
        PureeConfiguration conf = new PureeConfiguration.Builder(context)
                .register(FooLog.class, new OutFoo())
                .register(FooLog.class, new OutFoo().withFilters(new FooFilter()))
                .register(FooLog.class, new OutBar().withFilters(new FooFilter(), new BarFilter()))
                .register(BarLog.class, new OutFoo().withFilters(new FooFilter()))
                .build();

        {
            List<PureeOutput> outputs = conf.getRegisteredOutputPlugins(FooLog.class);
            assertThat(outputs.size(), is(3));
        }
        {
            List<PureeOutput> outputs = conf.getRegisteredOutputPlugins(BarLog.class);
            assertThat(outputs.size(), is(1));
            assertThat(outputs.get(0).getClass().getName(), is(OutFoo.class.getName()));
            List<PureeFilter> filters = outputs.get(0).getFilters();
            assertThat(filters.size(), is(1));
            assertThat(filters.get(0).getClass().getName(), is(FooFilter.class.getName()));
        }
    }


    private static class FooLog implements PureeLog {
    }

    private static class BarLog implements PureeLog {
    }

    private static class FooFilter implements PureeFilter {
        @Override
        public JsonObject apply(JsonObject jsonLog) {
            return null;
        }
    }

    private static class BarFilter implements PureeFilter {

        @Override
        public JsonObject apply(JsonObject jsonLog) {
            return null;
        }
    }

    private static class OutFoo extends PureeOutput {

        @Nonnull
        @Override
        public String type() {
            return "out_foo";
        }

        @Nonnull
        @Override
        public OutputConfiguration configure(OutputConfiguration conf) {
            return conf;
        }

        @Override
        public void emit(JsonObject jsonLog) {

        }
    }

    private static class OutBar extends PureeOutput {

        @Nonnull
        @Override
        public String type() {
            return "out_bar";
        }

        @Nonnull
        @Override
        public OutputConfiguration configure(OutputConfiguration conf) {
            return conf;
        }

        @Override
        public void emit(JsonObject jsonLog) {

        }
    }
}
