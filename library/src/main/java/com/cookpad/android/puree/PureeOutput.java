package com.cookpad.android.puree;

import com.cookpad.android.puree.storage.PureeStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class PureeOutput {
    protected OutputConfiguration conf;
    protected PureeStorage storage;
    protected List<PureeFilter> filters = new ArrayList<>();
    protected EmitCallback emitCallback = EmitCallback.DEFAULT;

    public void registerFilter(PureeFilter filter) {
        filters.add(filter);
    }

    public void setEmitCallback(EmitCallback emitCallback) {
        this.emitCallback = emitCallback;
    }

    public void initialize(PureeStorage storage) {
        this.storage = storage;
        this.conf = configure(new OutputConfiguration());
    }

    public void receive(JSONObject serializedLog) {
        try {
            final JSONObject filteredLog = applyFilters(serializedLog);
            emit(filteredLog);
            applyAfterFilters(type(), new ArrayList<JSONObject>() {{
                add(filteredLog);
            }});
        } catch (JSONException e) {
            // do nothing
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

    protected void applyAfterFilters(String type, List<JSONObject> serializedLogs) {
        emitCallback.call(type, serializedLogs);
    }

    public abstract String type();

    public abstract OutputConfiguration configure(OutputConfiguration conf);

    public abstract void emit(JSONObject serializedLog);
}

