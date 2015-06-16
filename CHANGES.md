# v3.0.0

Puree v3 is not compatible with v2, which uses [Gson](https://github.com/google/gson) for the fundamental JSON framework,
 instead of [org.json.JSONObject](http://developer.android.com/reference/org/json/package-summary.html) used before v3.

`Gson` has `JsonObject` class which is functionally compatible with `org.json.JSONObject` so migration is really easy.

Here is the diff between v2 and v3 for the SamplingFilter example:

```diff
     @Override
-    public JSONObject apply(JSONObject jsonLog) throws JSONException {
+    public JsonObject apply(JsonObject jsonLog) {
         return (samplingRate < Math.random() ? null : jsonLog);
     }
 }
```

The main development branch is here: https://github.com/cookpad/puree-android/pull/32

* https://github.com/cookpad/puree-android/compare/v2.0.0...v3.0.0

# v2.0.0 2015/2/26

Puree v2 is not compatible with v1; you can now connect a log classes with output plugins.

The main development branch is here: https://github.com/cookpad/puree-android/pull/22

* https://github.com/cookpad/puree-android/compare/v1.0.0...v2.0.0

# v1.0.0 2014/11/25

* https://github.com/cookpad/puree-android/compare/v0.0.8...v1.0.0

# v0.0.8 2014/11/5

* https://github.com/cookpad/puree-android/compare/v0.0.6...v0.0.8

# v0.0.6 2014/11/4

* The first tagged version

