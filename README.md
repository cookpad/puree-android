# Puree [![Build Status](https://travis-ci.org/cookpad/puree-android.svg?branch=master)](https://travis-ci.org/cookpad/puree-android) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Puree-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1170) [![Release](https://jitpack.io/v/cookpad/puree-android.svg)](https://jitpack.io/cookpad/puree-android)

# Description

Puree is a log collector which provides the following features:

- Filtering: Enable to interrupt process before sending log. You can add common params to logs, or the sampling of logs.
- Buffering: Store logs to buffers and send them later.
- Batching: Send logs in a single request with `PureeBufferedOutput`.
- Retrying: Retry to send logs after backoff time if sending logs fails.

![](./images/overview.png)

Puree helps you unify your logging infrastructure.

## Installation

This is published on `jitpack` and you can use Puree as:

```groovy
// build.gradle
buildscript {
    repositories {
        maven { url 'https://jitpack.io' }
    }
    ...
}

// app/build.gradle
dependencies {
    implementation "com.github.cookpad:puree-android:$latestVersion"
}
```

## Usage

### Initialize

Configure Puree with `PureeConfiguration` in `Application#onCreate()`, which registers
pairs of what and where.

```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        Puree.initialize(buildConfiguration(this));
    }

    public static PureeConfiguration buildConfiguration(Context context) {
        PureeFilter addEventTimeFilter = new AddEventTimeFilter();
        return new PureeConfiguration.Builder(context)
                .pureeSerializer(new PureeGsonSerializer())
                .executor(Executors.newScheduledThreadPool(1)) // optional
                .register(ClickLog.class, new OutLogcat())
                .register(ClickLog.class, new OutBufferedLogcat().withFilters(addEventTimeFilter))
                .build();
    }
}
```

See also: [demo/PureeConfigurator.java](demo/src/main/java/com/example/puree/logs/PureeConfigurator.java)

### Definition of PureeLog objects

Puree requires that clients supply an implementation of `PureeSerializer` to be able to serialize the logs. For instance, this is an implementation that uses Gson parser:

```java
public class PureeGsonSerializer implements PureeSerializer {
    private Gson gson = new Gson();

    @Override
    public String serialize(Object object) {
        return gson.toJson(object);
    }
}
```

A log class is just a POJO whose properties are annotated following the requirements of the Json parser that you provided with PureeSerializer.

```java
public class ClickLog {
    @SerializedName("page")
    private String page;
    @SerializedName("label")
    private String label;

    public ClickLog(String page, String label) {
        this.page = page;
        this.label = label;
    }
}
```

You can use `Puree.send()` to send these logs to registered output plugins:

```java
Puree.send(new ClickLog("MainActivity", "Hello"));
// => {"page":"MainActivity","label":"Hello"}
```

### Definition of PureeOutput plugins

There are two types of output plugins: non-buffered and buffered.

- `PureeOutput`: Non-buffered output plugins write logs immediately.
- `PureeBufferedOutput`: Buffered output plugins enqueue logs to a local storage and then flush them in background tasks.

If you don't need buffering, you can use `PureeOutput`.

```java
public class OutLogcat extends PureeOutput {
    private static final String TYPE = "out_logcat";

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public OutputConfiguration configure(OutputConfiguration conf) {
        return conf;
    }

    @Override
    public void emit(JsonObject jsonLog) {
        Log.d(TYPE, jsonLog.toString());
    }
}
```

If you need buffering, you can use `PureeBufferedOutput`.

```java
public class OutFakeApi extends PureeBufferedOutput {
    private static final String TYPE = "out_fake_api";

    private static final FakeApiClient CLIENT = new FakeApiClient();

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public OutputConfiguration configure(OutputConfiguration conf) {
        // you can change settings of this plugin
        // set interval of sending logs. defaults to 2 * 60 * 1000 (2 minutes).
        conf.setFlushIntervalMillis(1000);
        // set num of logs per request. defaults to 100.
        conf.setLogsPerRequest(10);
        // set retry count. if fail to send logs, logs will be sending at next time. defaults to 5.
        conf.setMaxRetryCount(3);
        return conf;
    }

    @Override
    public void emit(JsonArray jsonArray, final AsyncResult result) {
        // you have to call result.success or result.fail()
        // to notify whether if puree can clear logs from buffer
        CLIENT.sendLog(jsonArray, new FakeApiClient.Callback() {
            @Override
            public void success() {
                result.success();
            }

            @Override
            public void fail() {
                result.fail();
            }
        });
    }
}
```

### Definition of Filters

If you need to add common params to each logs, you can use `PureeFilter`:

```java
public class AddEventTimeFilter implements PureeFilter {
    public JsonObject apply(JsonObject jsonLog) {
        jsonLog.addProperty("event_time", System.currentTimeMillis());
        return jsonLog;
    }
}
```

You can make `PureeFilter#apply()` to return `null` to skip sending logs:

```java
public class SamplingFilter implements PureeFilter {
    private final float samplingRate;

    public SamplingFilter(float samplingRate) {
        this.samplingRate = samplingRate;
    }

    @Override
    public JsonObject apply(JsonObject jsonLog) {
        return (samplingRate < Math.random() ? null : jsonLog);
    }
}
```

Then register filters to output plugins on initializing Puree.

```java
new PureeConfiguration.Builder(context)
        .register(ClickLog.class, new OutLogcat())
        .register(ClickLog.class, new OutFakeApi().withFilters(addEventTimeFilter, samplingFilter)
        .build();
```

### Purging old logs
To discard unnecessary recorded logs, purge age can be configured in the PureeBufferedOutput subclass.

```java
public class OutPurge extends PureeBufferedOutput {

    // ..

    @Override
    public OutputConfiguration configure(OutputConfiguration conf) {
        // set to purge buffered logs older than 2 weeks
        conf.setPurgeAgeMillis(2 * 7 * 24 * 60 * 60 * 1000);
        return conf;
    }
}
```

The configured storage must be an instance of `PureeSQLiteStorage` or a subclass of it to support purging of old logs.

```java
new PureeConfiguration.Builder(context)
        .storage(new PureeSQLiteStorage(context))
        .register(ClickLog.class, new OutPurge().withFilters(addEventTimeFilter, samplingFilter)
        .build();
```

## Testing

If you want to mock or ignore `Puree.send()` and `Puree.flush()`, you can use `Puree.setPureeLogger()` to replace the internal
logger. See [PureeTest.java](puree/src/androidTest/java/com/cookpad/puree/PureeTest.java) for details.

## Release Engineering

- Update `CHANGES.md`
- `git tag $latest_version`
- `git push origin $latest_version`

# See Also

- [Puree - mobile application log collector - Cookpad Developers' blog (Japanese)](http://techlife.cookpad.com/entry/2014/11/25/132008)
- https://github.com/cookpad/puree-ios - Puree for iOS

# Copyright

Copyright (c) 2014 Cookpad Inc. https://github.com/cookpad

See [LICENSE.txt](LICENSE.txt) for the license.
