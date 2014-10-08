package com.cookpad.android.puree.handlers;

import java.util.ArrayList;
import java.util.List;

public class PureeFilters {
    private List<BeforeEmitFilter> beforeFilters = new ArrayList<>();
    private List<AfterFlushFilter> afterFilters = new ArrayList<>();

    public void registerBeforeFilter(BeforeEmitFilter filter) {
        beforeFilters.add(filter);
    }

    public List<BeforeEmitFilter> getBeforeFilters() {
        return beforeFilters;
    }

    public void registerAfterFilter(AfterFlushFilter filter) {
        afterFilters.add(filter);
    }

    public List<AfterFlushFilter> getAfterFilters() {
        return afterFilters;
    }
}
