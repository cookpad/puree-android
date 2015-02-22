Puree [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Puree-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1170)
====

## Description

Puree is a log collector which provides some features like below

- Filtering: Enable to interrupt process before sending log. You can add common params to logs, or the sampling of logs.
- Buffering: Store logs to buffer until log was sent.
- Batching: Enable to send logs by 1 request.
- Retrying: Retry to send logs after backoff time automatically if sending logs fails.

![](./images/overview.png)

Puree helps you unify your logging infrastructure.

## Usage

### Initialize

Configure Puree on application created.

```java
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        Puree.initialize(buildConfiguration(this));
    }

    public static PureeConfiguration buildConfiguration(Context context) {
        PureeFilter addEventTimeFilter = new AddEventTimeFilter();
        return new PureeConfiguration.Builder(context)
                .source(ClickLog.class).to(new OutLogcat())
                .source(ClickLog.class).filter(addEventTimeListener).to(new OutBufferedLogcat())
                .build();
    }
}
```

### Send logs

Log class should extend JsonConvertible.

```java
public class ClickLog extends JsonConvertible {
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

Call `Puree.send` in an arbitrary timing.

```java
Puree.send(new ClickLog("MainActivity", "Hello"));
// => {"page":"MainActivity","label":"Hello"}
```

### Create output plugins

There are two types of output plugins: Non-Buffered, Buffered.

- Non-Buffered output plugins do not buffer data and immediately write out results.
- Buffered output plugins store logs to local storage temporary.

If you don't need buffering, you can use PureeOutput.

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
    public void emit(JSONObject jsonLog) {
        Log.d(TYPE, jsonLog.toString());
    }
}
```

If you need beffering, you can use PureeBufferedOutput.

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
    public void emit(JSONArray jsonArray, final AsyncResult result) {
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

### Create filters

If you need to add common params to each logs, you can use PureeFilter.

```java
public class AddEventTimeFilter implements PureeFilter {
    public JSONObject apply(JSONObject jsonLog) throws JSONException {
        jsonLog.put("event_time", System.currentTimeMillis());
        return jsonLog;
    }
}
```

Puree do nothing if `PureeFilter#apply` returns null.

```java
public class SamplingFilter implements PureeFilter {
    private final float samplingRate;

    public SamplingFilter(float samplingRate) {
        this.samplingRate = samplingRate;
    }

    @Override
    public JSONObject apply(JSONObject jsonLog) throws JSONException {
        return (samplingRate < Math.random() ? null : jsonLog);
    }
}
```

Register filters when initializing Puree.

```java
new PureeConfiguration.Builder(context)
        .source(ClickLog.class).to(new OutLogcat())
        .source(ClickLog.class).filters(addEventTimeFilter).filter(samplingFilter).to(new OutFakeApi())
        .build();
```

## Download

Reference on jcenter as

```
// build.gradle
buildscript {
    repositories {
        jcenter()
    }
    ...

// app/build.gradle
compile 'com.cookpad:puree:1.2.0'
```
