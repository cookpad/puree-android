Puree [![Build Status](https://travis-ci.org/cookpad/puree-android.svg?branch=master)](https://travis-ci.org/cookpad/puree-android)  [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Puree-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1170)
====

# Description

Puree is a log collector which provides the following features:

- Filtering: Enable to interrupt process before sending log. You can add common params to logs, or the sampling of logs.
- Buffering: Store logs to buffers and send them later.
- Batching: Send logs in a single request with `PureeBufferedOutput`.
- Retrying: Retry to send logs after backoff time if sending logs fails.

![](./images/overview.png)

Puree helps you unify your logging infrastructure.

## Usage

### Initialize

Configure Puree with `PureeConfiguration` in `Application#onCreate()`, which registers
pairs of what and where.

```java
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        Puree.initialize(buildConfiguration(this));
    }

    public static PureeConfiguration buildConfiguration(Context context) {
        PureeFilter addEventTimeFilter = new AddEventTimeFilter();
        return new PureeConfiguration.Builder(context)
                .register(ClickLog.class, new OutLogcat())
                .register(ClickLog.class, new OutBufferedLogcat().withFilters(addEventTimeListener))
                .build();
    }
}
```

### Definition of PureeLog objects

A log class is required to implement `PureeLog`, which is a marker interface just like as `Serializable`,
to serialize logs with `Gson`.

```java
public class ClickLog implements PureeLog {
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

If you need beffering, you can use `PureeBufferedOutput`.

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
        jsonLog.put("event_time", System.currentTimeMillis());
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

## Installation

This is published on `jcenter` and you can use Puree as:

```groovy
// build.gradle
buildscript {
    repositories {
        jcenter()
    }
    ...
}

// app/build.gradle
dependencies {
    compile 'com.cookpad:puree:3.0.0'
}
```

# See Also

* [Puree - mobile application log collector - Cookpad Developers' blog (Japanese)](http://techlife.cookpad.com/entry/2014/11/25/132008)
* https://github.com/cookpad/puree-ios - Puree for iOS

# Copyright

Copyright (c) 2014 Cookpad Inc. https://github.com/cookpad

See [LICENSE.txt](LICENSE.txt) for the license.



