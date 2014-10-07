package com.example.loghouse.logs;

import com.cookpad.android.loghouse.SerializableLog;
import com.example.loghouse.logs.plugins.OutDisplay;
import com.google.gson.annotations.SerializedName;

public class ClickLog extends SerializableLog {
    @SerializedName("page")
    private String page;
    @SerializedName("label")
    private String label;

    private String type = OutDisplay.TYPE;

    public String type() {
        return type;
    }

    public ClickLog(String page, String label, String type) {
        this.page = page;
        this.label = label;
        this.type = type;
    }
}
