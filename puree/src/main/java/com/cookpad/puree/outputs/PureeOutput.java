package com.cookpad.puree.outputs;

import com.cookpad.puree.OutputConfiguration;
import com.cookpad.puree.PureeFilter;
import com.cookpad.puree.storage.PureeStorage;

import org.json.JSONArray;
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

    public void initialize(PureeStorage storage) {
        this.storage = storage;
        this.conf = configure(new OutputConfiguration());
    }

    public void receive(JSONObject serializedLog) {
        try {
            final JSONObject filteredLog = applyFilters(serializedLog);
            if (filteredLog == null) {
                return;
            }

            emit(filteredLog);
        } catch (JSONException ignored) {
        }
    }

    protected JSONObject applyFilters(JSONObject serializedLog) throws JSONException {
        if (filters == null || filters.isEmpty()) {
            return serializedLog;
        }

        JSONObject filteredLog = new JSONObject();
        for (PureeFilter filter : filters) {
            filteredLog = filter.apply(serializedLog);
        }
        return filteredLog;
    }

    public void flush() {
        // do nothing because PureeOutput don't have any buffers.
    }

    public abstract String type();

    public abstract OutputConfiguration configure(OutputConfiguration conf);

    public abstract void emit(JSONObject serializedLog);
}

