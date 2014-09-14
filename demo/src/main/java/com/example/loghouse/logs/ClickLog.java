package com.example.loghouse.logs;

import com.cookpad.android.loghouse.Log;
import com.cookpad.android.loghouse.plugins.OutBufferedLogcat;
import com.example.loghouse.logs.plugins.OutDisplay;
import com.google.gson.annotations.SerializedName;

public class ClickLog extends Log {
    @SerializedName("page")
    private String page;
    @SerializedName("label")
    private String label;

    public String type() {
        return OutDisplay.TYPE;
    }

    public ClickLog(String page, String label) {
        this.page = page;
        this.label = label;
    }
}
