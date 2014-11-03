package com.example.puree.logs.filters;

import com.cookpad.puree.PureeFilter;

import org.json.JSONException;
import org.json.JSONObject;

public class SamplingFilter implements PureeFilter {
    private final float samplingRate;

    public SamplingFilter(float samplingRate) {
        this.samplingRate = samplingRate;
    }

    @Override
    public JSONObject apply(JSONObject serializedLog) throws JSONException {
        return (samplingRate < Math.random() ? null : serializedLog);
    }
}
