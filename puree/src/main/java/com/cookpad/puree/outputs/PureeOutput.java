package com.cookpad.puree.outputs;

import com.cookpad.puree.PureeFilter;
import com.cookpad.puree.storage.PureeStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class PureeOutput {
    protected OutputConfiguration conf;
    protected PureeStorage storage;
    protected List<PureeFilter> filters = new ArrayList<>();

    public void registerFilter(PureeFilter filter) {
        filters.add(filter);
    }

    public List<PureeFilter> getFilters() {
        return filters;
    }

    public void initialize(PureeStorage storage) {
        this.storage = storage;
        OutputConfiguration defaultConfiguration = new OutputConfiguration();
        this.conf = configure(defaultConfiguration);

        if (conf == null) {
            conf = defaultConfiguration;
        }
    }

    public void receive(JSONObject jsonLog) {
        try {
            final JSONObject filteredLog = applyFilters(jsonLog);
            if (filteredLog == null) {
                return;
            }

            emit(filteredLog);
        } catch (JSONException ignored) {
        }
    }

    protected JSONObject applyFilters(JSONObject jsonLog) throws JSONException {
        JSONObject filteredLog = jsonLog;
        for (PureeFilter filter : filters) {
            filteredLog = filter.apply(filteredLog);
            if (filteredLog == null) {
                return null;
            }
        }
        return filteredLog;
    }

    public void flush() {
        // do nothing because PureeOutput don't have any buffers.
    }

    public abstract String type();

    public abstract OutputConfiguration configure(OutputConfiguration conf);

    public abstract void emit(JSONObject jsonLog);
}

