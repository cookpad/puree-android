package com.example.puree.logs.filters;

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
    public String apply(String jsonLog) {
        return (samplingRate < Math.random() ? null : jsonLog);
    }
}
