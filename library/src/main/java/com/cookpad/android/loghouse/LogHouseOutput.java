package com.cookpad.android.loghouse;

import com.cookpad.android.loghouse.handlers.AfterFlushAction;
import com.cookpad.android.loghouse.handlers.BeforeEmitAction;
import com.cookpad.android.loghouse.storage.LogHouseStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class LogHouseOutput {
    protected LogHouseStorage storage;
    protected AfterFlushAction afterFlushAction;
    protected BeforeEmitAction beforeEmitAction;
    protected boolean isTest = false;

    public abstract String type();

    public void setStorage(LogHouseStorage storage) {
        this.storage = storage;
    }

    public void configure(LogHouseConfiguration conf) {
        this.isTest = conf.isTest();
        this.afterFlushAction = conf.getAfterFlushAction();
        this.beforeEmitAction = conf.getBeforeEmitAction();
    }

    public void start(JSONObject serializedLog) {
        try {
            serializedLog = beforeEmitAction.call(serializedLog);
            emit(serializedLog);

            List<JSONObject> serializedLogs = new ArrayList<>();
            serializedLogs.add(serializedLog);
            afterFlushAction.call(type(), serializedLogs);
        } catch (JSONException e) {
            // do nothing
        }
    }

    public abstract void emit(JSONObject serializedLog);
}

