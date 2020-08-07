package com.cookpad.puree;

import com.cookpad.puree.internal.LogDumper;
import com.cookpad.puree.outputs.PureeOutput;
import com.cookpad.puree.storage.PureeSQLiteStorage;
import com.cookpad.puree.storage.PureeStorage;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PureeConfiguration {

    private final Context context;

    private final PureeSerializer pureeSerializer;

    private final Map<Class<?>, List<PureeOutput>> sourceOutputMap;

    private final PureeStorage storage;

    private final ScheduledExecutorService executor;

    public Context getContext() {
        return context;
    }

    public Map<Class<?>, List<PureeOutput>> getSourceOutputMap() {
        return sourceOutputMap;
    }

    public PureeStorage getStorage() {
        return storage;
    }

    public List<PureeOutput> getRegisteredOutputPlugins(Class<?> logClass) {
        return sourceOutputMap.get(logClass);
    }

    public PureeLogger createPureeLogger() {
        return new PureeLogger(sourceOutputMap, pureeSerializer, storage, executor);
    }

    PureeConfiguration(Context context, Map<Class<?>, List<PureeOutput>> sourceOutputMap, PureeSerializer pureeSerializer,
            PureeStorage storage, ScheduledExecutorService executor) {
        this.context = context;
        this.pureeSerializer = pureeSerializer;
        this.sourceOutputMap = sourceOutputMap;
        this.storage = storage;
        this.executor = executor;

    }

    /**
     * Print mapping of SOURCE -&gt; FILTER... OUTPUT.
     */
    public void printMapping() {
        LogDumper.out(sourceOutputMap);
    }

    public static class Builder {
        private Context context;

        private PureeSerializer pureeSerializer;

        private Map<Class<?>, List<PureeOutput>> sourceOutputMap = new HashMap<>();

        private PureeStorage storage;

        private ScheduledExecutorService executor;

        /**
         * Start building a new {@link com.cookpad.puree.PureeConfiguration} instance.
         *
         * @param context {@link Context}.
         */
        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        /**
         * Specify the {@link PureeSerializer} to serialize logs.
         *
         * @param pureeSerializer {@link PureeSerializer}.
         * @return {@link com.cookpad.puree.PureeConfiguration.Builder}.
         */
        public Builder pureeSerializer(PureeSerializer pureeSerializer) {
            this.pureeSerializer = pureeSerializer;
            return this;
        }


        /**
         * Specify a source class of logs, which returns {@link Source} an
         * {@link Source#to(PureeOutput)} must be called to register an output plugin.
         *
         * @param logClass log class.
         * @return {@link Source}.
         */
        public Source source(Class<?> logClass) {
            return new Source(this, logClass);
        }

        public Builder register(Class<?> logClass, PureeOutput output) {
            List<PureeOutput> outputs = sourceOutputMap.get(logClass);
            if (outputs == null) {
                outputs = new ArrayList<>();
            }
            outputs.add(output);
            sourceOutputMap.put(logClass, outputs);
            return this;
        }

        public Builder storage(PureeStorage storage) {
            this.storage = storage;
            return this;
        }

        public Builder executor(ScheduledExecutorService executor) {
            this.executor = executor;
            return this;
        }

        /**
         * Create the {@link com.cookpad.puree.PureeConfiguration} instance.
         *
         * @return {@link com.cookpad.puree.PureeConfiguration}.
         */
        public PureeConfiguration build() {
            if (pureeSerializer == null) {
                throw new IllegalStateException("A PureeSerializer is required to build PureeConfiguration");
            }

            if (storage == null) {
                storage = new PureeSQLiteStorage(context);
            }

            if (executor == null) {
                executor = newBackgroundExecutor();
            }
            return new PureeConfiguration(context, sourceOutputMap, pureeSerializer, storage, executor);
        }
    }

    static ScheduledExecutorService newBackgroundExecutor() {
        return Executors.newScheduledThreadPool(1, new BackgroundThreadFactory());
    }

    static class BackgroundThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "puree");
            thread.setPriority(Thread.MIN_PRIORITY);
            return thread;
        }
    }
}
