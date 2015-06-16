package com.example.puree.logs.filters;

import com.google.gson.JsonObject;

import com.cookpad.puree.PureeFilter;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SamplingFilter implements PureeFilter {
    private final float samplingRate;

    public SamplingFilter(float samplingRate) {
        this.samplingRate = samplingRate;
    }

    @Nullable
    @Override
    public JsonObject apply(JsonObject jsonLog) {
        return (samplingRate < Math.random() ? null : jsonLog);
    }
}
